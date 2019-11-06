package net.rgsw.io;

import java.io.*;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * An implementation of the Binary Map Format as explained below.
 * <p/>
 * <h2>BMF</h2>
 * A Binary Map Format (BMF) file stores zero or more binaries and maps them to long keys.
 * <p/>
 * A BMF file starts with 4-byte header indicating the amount of data in the file. This is the amount of sectors that
 * the file stores.
 * <p/>
 * The header is directly followed by all the binary data stored in the file. This data is divided into sectors of a
 * specific size (this size may differ per context but it's not stored in the file itself). Each stored binary is spread
 * over zero or more sectors. The sectors in the BMF file may or may not be in order of the stored binaries.
 * <p/>
 * After the data follows a footer that stores the entry table. The entry table maps the long keys to an ordered list of
 * sector indices. This specifies which sectors have to be concatenated in which order to obtain the original binary
 * again. The entry table starts with a 32-bits integer (4 bytes) indicating the amount of entries, which is followed by
 * that amount of entries.<br/>
 *
 * Each entry starts with a 8 bytes storing the key of that entry. Those bytes are followed by the index list. An index
 * list consists of 32-bits signed integers.<br/>
 *
 * The first integer of the index list is the amount of sectors the corresponding entry uses. Then follows the list of
 * sector indices. The index -1 refers to an empty sector. The implementation does not use empty sectors for saving, but
 * it supports loading empty sectors. The index -2 indicates an amount of consecutive indices after the previous index.
 * Another integer indicates the amount. For example: <code>15 -2 5</code> references sector 15 followed by 5 other
 * consecutive sectors, and is the same as <code>15 16 17 18 19 20</code>.
 * <p/>
 * <h2>Implementation</h2>
 * Sectors can be in any order so that a binary can be easily grow or shrink in size. When a binary grows in size and it
 * needs another sector, that sector is just appended at the end of the file. Likewise, when a binary shrinks in size,
 * the last sectors are moved to the gap that's created. If sectors were in order of the binaries, all sectors had to be
 * shifted left or right when a binary shrinks or grows respectively. That would mean that all those sectors had to be
 * loaded into memory and then needed to be written again at another place. The random order is therefore much more
 * efficient.
 * <p/>
 * This BMF implementation assigns an unique ID to each sector, which is independent of the index. When the entry table
 * is loaded, each sector index is replaced with the corresponding sector ID. When the entry table is saved, the IDs are
 * remapped to indices again. When a sector is moved, its index changes, while its ID does not change. If indices were
 * used instead of IDs, the entry table had to be remapped every time a sector moves and it would take more
 * performance.
 * <p/>
 * The data of each binary can be compressed with GZIP or DEFLATE, or can be kept uncompressed. All binaries have the
 * same compression type (which can be differ per context but is not saved in the file). It is also possible to have
 * different compression types for each binary, but this must be done by compressing each binary before streaming it
 * into a file.
 * <p/>
 * The implementation uses a {@link RandomAccessFile} to read and write files.
 * <p/>
 * <h2>Usage</h2>
 * A {@code BMFFile} can be instantiated with an overload {@link #create} or {@link #open}. When using {@link #open}, it
 * automatically attempts to load the file.
 * <p/>
 * There are 4 methods for managing entries stored in the file:
 * <ul>
 * <li>{@link #readEntry} creates an {@link InputStream} to read a specific entry. When no such entry exists, it will
 * return an empty {@link InputStream}.</li>
 * <li>{@link #writeEntry} creates an {@link OutputStream} to write a specific entry. When no such entry exists, it
 * will create a new entry.</li>
 * <li>{@link #removeEntry} removes a specific entry. When no such entry exists, this does nothing.</li>
 * <li>{@link #containsEntry} checks if a specific entry exist.</li>
 * </ul>
 * <p/>
 * The file can be written to disk with {@link #flush} or {@link #close}. Both methods save and optimize the file, but
 * {@link #close} also cleans up buffered sectors and minimizes the internal sector array, assuming that the instance
 * will not be used for a while. Try to use {@link #flush} when you plan to write more to the file right after saving as
 * this keeps buffered sectors.<br/>
 *
 * The underlying I/O-stream of an instance automatically closes when that instance is collected by the garbage
 * collector (see {@link Object#finalize}).
 * <p/>
 * A {@code BMFFile} can be used in a try-with-resources statement.
 */
public class BMFFile implements Flushable, Closeable {
    private final int sectorSize;
    private final int sectorSizeMask;
    private final int sectorSizeExponent;

    public static final int MAX_SECTORS = Integer.MAX_VALUE >>> 1;

    private final File file;
    private final RandomAccessFile io;

    private final HashMap<Long, Entry> entries = new HashMap<>();
    private Sector[] sectors;

    // All the sectors get an unique ID on load, which we use in loaded entries to reference this sector instead of
    // using their indices. So when we move a sector to another index, we don't need to find and update the entry that
    // uses that sector as the ID does not change.
    private final HashMap<Integer, Integer> idsToIndices = new HashMap<>();

    private final Compression compressionType;


    private BMFFile( File file, int initCapacity, Compression compression, int sectorSize ) throws IOException {
        if( file == null ) throw new NullPointerException( "File must not be null" );
        if( compression == null ) throw new NullPointerException( "Compression type must not be null" );
        if( sectorSize < 16 || ( sectorSize & sectorSize - 1 ) != 0 ) {
            throw new IllegalArgumentException( "Sector must be a power of 2 and at least 16" );
        }
        if( initCapacity < 0 ) {
            throw new IllegalArgumentException( "Initial capacity must be zero or positive" );
        }

        this.file = file;
        this.sectors = new Sector[ initCapacity ];
        this.compressionType = compression;
        this.sectorSize = sectorSize;
        this.sectorSizeMask = sectorSize - 1;
        this.sectorSizeExponent = Integer.numberOfTrailingZeros( sectorSize );

        if( ! file.exists() ) {
            if( file.getParentFile() != null ) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        io = new RandomAccessFile( file, "rw" );
    }

    private synchronized void readIDTable() throws IOException {
        io.seek( 0 );

        int sectorCount = io.readInt();

        // Init sectors
        discardAllSectors();
        ensureSectorCount( sectorCount );
        for( int i = 0; i < sectorCount; i++ ) {
            Sector sector = new Sector( getAFreeSectorID() );
            sector.index = i;
            sectors[ i ] = sector;

            idsToIndices.put( sector.id, sector.index );
        }

        // Read entry table
        io.seek( 4 + (long) sectorCount * sectorSize );

        entries.clear();
        int entryCount = io.readInt();
        for( int i = 0; i < entryCount; i++ ) {
            long id = io.readLong();
            Entry entry = new Entry( id );

            entry.sectors = readIndices();
            entries.put( id, entry );
        }
    }


    private synchronized void writeIDTable() throws IOException {
        io.seek( 0 );
        int sectorCount = getSectorCount();
        io.writeInt( sectorCount );

        io.seek( 4 + sectorCount * sectorSize );

        // Only save entries that are not null
        int entryCount = 0;
        Set<Long> keys = entries.keySet();
        for( long key : keys ) {
            Entry e = entries.get( key );
            if( e != null ) {
                entryCount++;
            }
        }

        // Write entry table
        io.writeInt( entryCount );

        int n = 0;
        for( long key : keys ) {
            Entry entry = entries.get( key );
            if( entry != null ) {
                n++;
                if( n > entryCount ) {
                    throw new IOException( "Writing too many entries!" );
                }
                io.writeLong( entry.id );
                writeIndices( entry.sectors );
            }
        }
    }

    private int[] readIndices() throws IOException {
        int len = io.readInt();
        int[] ids = new int[ len ];
        int curr = 0, last = - 1, n;

        while( curr < len ) {
            n = io.readInt();
            if( n < - 2 ) {
                throw new IOException( "Invalid operator: " + n );
            } else if( n == - 2 ) {
                if( last == - 1 ) {
                    throw new IOException( "Unexpected index range" );
                }
                int size = io.readInt();
                if( size < 0 ) {
                    throw new IOException( "Negative range size" );
                }
                for( int i = 0; i < size; i++ ) {
                    last++;
                    ids[ curr ] = getSectorID( last );
                    curr++;
                }
                last = - 1;
            } else {
                last = n;
                ids[ curr ] = getSectorID( n );
                curr++;
            }
        }
        return ids;
    }

    private void writeIndices( int[] ids ) throws IOException {
        int len = ids.length, curr = 0, n;
        io.writeInt( len );

        while( curr < len ) {
            n = getSectorIndex( ids[ curr ] );
            io.writeInt( n );
            curr++;

//            int streak = 0;
//            for( int i = curr; i < ids.length; i++ ) {
//                int n2 = getSectorIndex( ids[ i ] );
//                if( n + 1 == n2 ) {
//                    streak++;
//                    n = n2;
//                } else {
//                    break;
//                }
//            }
//
//            if( streak > 1 ) {
//                io.writeInt( - 2 );
//                io.writeInt( streak );
//                curr += streak;
//            }
        }
    }




    private synchronized void loadSector( Sector sector ) throws IOException {
        checkSector( sector );
        if( sector.type == SectorType.UNLOADED ) {
            seekSector( sector.index );
            sector.buffer = new byte[ sectorSize ];
            io.readFully( sector.buffer );
            sector.type = SectorType.LOADED;
        }
    }

    private synchronized void saveSector( Sector sector ) throws IOException {
        checkSector( sector );
        if( sector.type == SectorType.DIRTY ) {
            seekSector( sector.index );
            io.write( sector.buffer );
            sector.type = SectorType.LOADED;
        }
    }

    private synchronized void unloadSector( Sector sector ) throws IOException {
        checkSector( sector );
        saveSector( sector );
        sector.type = SectorType.UNLOADED;
        sector.buffer = null;
    }

    private synchronized Sector getSectorByID( int id ) {
        return sectors[ idsToIndices.get( id ) ];
    }

    private synchronized int getSectorID( int index ) {
        return index < 0 ? 0 : sectors[ index ].id;
    }

    private synchronized int getSectorIndex( int id ) {
        return id == 0 ? - 1 : idsToIndices.get( id );
    }

    private synchronized void moveSector( Sector sector, int newIndex ) throws IOException {
        checkSector( sector );
        if( sectors[ newIndex ] != null ) {
            throw new IOException( "Cannot move sector #" + sector.index + " to #" + newIndex + " as a sector already exists there." );
        }

        if( sector.type == SectorType.UNLOADED ) {
            loadSector( sector );
        }
        sector.type = SectorType.DIRTY;

        sectors[ sector.index ] = null;
        sectors[ newIndex ] = sector;
        sector.index = newIndex;
        idsToIndices.put( sector.id, newIndex );
    }

    private synchronized void removeSector( Sector sector ) throws IOException {
        checkSector( sector );
        sectors[ sector.index ] = null;
        idsToIndices.remove( sector.id );
    }

    private synchronized Sector newSector( int index ) throws IOException {
        if( index >= MAX_SECTORS ) {
            throw new IOException( "Maximum sector count reached!" );
        }
        Sector sector = sectors[ index ] = new Sector( getAFreeSectorID(), index, sectorSize );
        idsToIndices.put( sector.id, sector.index );
        return sector;
    }

    private synchronized Sector newSector() throws IOException {
        for( int i = 0; i < sectors.length; i++ ) {
            if( sectors[ i ] == null ) {
                return newSector( i );
            }
        }
        ensureSectorCount( sectors.length );
        return newSector( sectors.length - 1 );
    }

    private synchronized void saveAllSectors() throws IOException {
        optimize();
        for( Sector sector : sectors ) {
            if( sector != null ) {
                saveSector( sector );
            }
        }
    }

    private synchronized void unloadAllSectors() throws IOException {
        optimize();
        for( Sector sector : sectors ) {
            if( sector != null ) {
                unloadSector( sector );
            }
        }
    }

    private synchronized void discardAllSectors() {
        for( int i = 0; i < sectors.length; i++ ) {
            sectors[ i ] = null;
        }
    }




    private synchronized void createEntrySectors( Entry entry ) throws IOException {
        for( int i = 0; i < entry.sectors.length; i++ ) {
            int sectorID = entry.sectors[ i ];
            if( sectorID == 0 ) {
                Sector sector = newSector();
                entry.sectors[ i ] = sector.id;
            }
        }
    }

    private synchronized void setEntrySectorsAndRemoveOthers( Entry entry, List<Sector> sectors, int count ) throws IOException {
        entry.sectors = new int[ count ];
        for( int i = 0; i < count; i++ ) {
            entry.sectors[ i ] = sectors.get( i ).id;
        }
        for( int i = count; i < sectors.size(); i++ ) {
            removeSector( sectors.get( i ) );
        }
    }

    private synchronized SectorOutputStream createOut( Entry entry ) throws IOException {
        createEntrySectors( entry );

        Sector[] sectors = new Sector[ entry.sectors.length ];
        for( int i = 0; i < sectors.length; i++ ) {
            sectors[ i ] = getSectorByID( entry.sectors[ i ] );
            if( sectors[ i ] == null ) {
                throw new IOException( "Trying to write to a referenced, but not existing sector @" + entry.sectors[ i ] );
            }
        }

        return new SectorOutputStream( sectors, entry );
    }

    private synchronized SectorInputStream createIn( Entry entry ) throws IOException {
        createEntrySectors( entry );

        Sector[] sectors = new Sector[ entry.sectors.length ];
        for( int i = 0; i < sectors.length; i++ ) {
            sectors[ i ] = getSectorByID( entry.sectors[ i ] );
            if( sectors[ i ] == null ) {
                throw new IOException( "Trying to read from a referenced, but not existing sector @" + entry.sectors[ i ] );
            }
        }

        return new SectorInputStream( sectors, entry );
    }

    private synchronized Entry newEntry( long key ) throws IOException {
        if( entries.containsKey( key ) ) {
            throw new IOException( "Cannot create an entry for key " + key + " as an entry with that key already exists" );
        }
        Entry e = new Entry( key );
        e.sectors = new int[ 0 ];
        entries.put( key, e );
        return e;
    }

    /**
     * Opens an {@link InputStream} to read the entry with the specified key from. This returns a zero-length stream
     * when no entry exists with the specified key. The returned input stream is {@linkplain BufferedInputStream
     * buffered}.
     *
     * @param key The key of the entry to write to.
     * @return The input stream.
     *
     * @throws IOException If an I/O error occurs, or when the entry is already being streamed.
     */
    public synchronized InputStream readEntry( long key ) throws IOException {
        Entry entry = entries.get( key );
        if( entry == null ) return new EmptyInputStream();
        if( entry.used ) {
            throw new IOException( "Entry #" + key + " is already being streamed, it cannot be streamed twice" );
        }
        return new BufferedInputStream( compressionType.decompressorFactory.wrap( createIn( entry ) ) );
    }

    /**
     * Opens an {@link OutputStream} to write to the entry with the specified key. This creates a new entry when no
     * entry exists with the specified key. The returned output stream is {@linkplain BufferedOutputStream buffered}.
     *
     * @param key The key of the entry to write to.
     * @return The output stream.
     *
     * @throws IOException If an I/O error occurs, or when the entry is already being streamed.
     */
    public synchronized OutputStream writeEntry( long key ) throws IOException {
        Entry entry = entries.get( key );
        if( entry == null ) {
            entry = newEntry( key );
        }
        if( entry.used ) {
            throw new IOException( "Entry #" + key + " is already being streamed, it cannot be streamed twice" );
        }
        return new BufferedOutputStream( compressionType.compressorFactory.wrap( createOut( entry ) ) );
    }

    /**
     * Checks whether an entry exists with the specific key.
     *
     * @param key The key to check.
     * @return True when an entry exists with the specified key, false otherwise.
     */
    public synchronized boolean containsEntry( long key ) {
        return entries.containsKey( key );
    }

    /**
     * Checks whether a specific entry can be streamed. This returns false when an entry is already being streamed.
     *
     * @param key The key of the entry to check.
     * @return True when the entry does not exist, or when the existing entry is not already being streamed, false
     *     otherwise
     */
    public synchronized boolean canStreamEntry( long key ) {
        return entries.containsKey( key );
    }

    /**
     * Removes the entry for the specified key, if it exists.
     *
     * @param key The key of the entry to remove.
     * @throws IOException When the entry is being streamed.
     */
    public synchronized void removeEntry( long key ) throws IOException {
        Entry e = entries.get( key );
        if( e != null && e.used ) {
            throw new IOException( "Entry #" + key + " is being streamed, it cannot be removed" );
        }
        entries.remove( key );
    }




    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file.
     *
     * @param file        The file path to save the file with the new structure at
     * @param compression The new compression type
     * @param sectorSize  The new sector size
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( File file, Compression compression, int sectorSize ) throws IOException {
        BMFFile converted = create( file, sectorSize, compression );
        for( long key : entries.keySet() ) {
            InputStream in = readEntry( key );
            OutputStream out = converted.writeEntry( key );

            int b;
            while( ( b = in.read() ) >= 0 ) out.write( b );
            in.close();
            out.close();
        }
        return converted;
    }

    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file.
     *
     * @param file       The file path to save the file with the new structure at
     * @param sectorSize The new sector size
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( File file, int sectorSize ) throws IOException {
        return convert( file, compressionType, sectorSize );
    }

    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file.
     *
     * @param file        The file path to save the file with the new structure at
     * @param compression The new compression type
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( File file, Compression compression ) throws IOException {
        return convert( file, compression, sectorSize );
    }

    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file. The new
     * file will write to the same file as this file.
     *
     * @param compression The new compression type
     * @param sectorSize  The new sector size
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( Compression compression, int sectorSize ) throws IOException {
        return convert( file, compression, sectorSize );
    }

    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file.
     *
     * @param sectorSize The new sector size
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( int sectorSize ) throws IOException {
        return convert( file, compressionType, sectorSize );
    }

    /**
     * Converts this BMF file to a file with a different structure. This creates a new file instance with the new
     * structure and then copies all the entries that are saved in this instance to the new instance. The structure of
     * this instance will not change, and any change made to the restructured file will not apply to this file.
     *
     * @param compression The new compression type
     * @return A new file instance with the specified compression type and sector size.
     *
     * @throws IOException When an I/O error occurs.
     */
    public synchronized BMFFile convert( Compression compression ) throws IOException {
        return convert( file, compression, sectorSize );
    }




    /**
     * Returns the type of compression used to compress this file.
     */
    public Compression getCompressionType() {
        return compressionType;
    }

    /**
     * Returns the abstract path of this file.
     */
    public File getFile() {
        return file;
    }




    private synchronized void ensureSectorCount( int count ) {
        if( count >= sectors.length ) {
            resizeSectorArray( count + 16 );
        }
    }

    private synchronized void resizeSectorArray( int newSize ) {
        int oldSize = sectors.length;
        Sector[] old = sectors;
        sectors = new Sector[ newSize ];
        System.arraycopy( old, 0, sectors, 0, oldSize < newSize ? oldSize : newSize );
    }

    private synchronized void cleanSectorArray() {
        resizeSectorArray( getSectorCount() );
    }

    private synchronized int getAFreeSectorID() {
        int highestID = 0;
        for( Sector sector : sectors ) {
            if( sector != null && sector.id > highestID ) {
                highestID = sector.id;
            }
        }
        return highestID + 1;
    }

    private synchronized void seekSector( int index ) throws IOException {
        io.seek( 4 + sectorSize * index );
    }

    private synchronized int getSectorCount() {
        int count = 0;
        for( Sector s : sectors ) {
            if( s != null ) {
                count = s.index;
            }
        }
        return count + 1;
    }

    private synchronized Sector checkSector( Sector sector ) throws IOException {
        if( sector == null ) {
            throw new NullPointerException( "Can't check null sector" );
        }
        if( sectors[ sector.index ] != sector ) {
            throw new IOException( "Sector @" + sector.id + "#" + sector.index + " does not match the sector in the array (@" + sectors[ sector.index ].id + ")" );
        }
        return sector;
    }




    private synchronized void optimize() throws IOException {
        Set<Integer> usedSectorIDs = new HashSet<>();
        for( Entry entry : entries.values() ) {
            for( int id : entry.sectors ) {
                usedSectorIDs.add( id );
            }
        }
        ArrayList<Sector> toRemove = new ArrayList<>();
        for( Sector sector : sectors ) {
            if( sector == null ) continue;
            if( ! usedSectorIDs.contains( sector.id ) ) {
                toRemove.add( sector );
            }
        }
        for( Sector sector : toRemove ) {
            removeSector( sector );
        }

        int count = getSectorCount();
        for( int i = 0; i < count; i++ ) {
            Sector sector = sectors[ i ];
            if( sector == null ) {
                int replacerIndex = count - 1;
                Sector replacer = sectors[ replacerIndex ];
                moveSector( replacer, i );
                count = getSectorCount();
            }
        }
    }

    /**
     * Optimizes and saves all sectors, the header and the entry table to the file system. Unlike {@link #close}, this
     * does not release all buffered sectors. Use this when changes are going to be made right after saving.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public synchronized void flush() throws IOException {
        saveAllSectors();
        writeIDTable();
    }

    /**
     * Optimizes and saves all sectors, the header and the entry table to the file system. Additionally to {@link
     * #flush}, this also releases all buffered sectors and resizes the internal sector array to an optimal size. Use
     * this when this instance does not need to be used for a while.
     *
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public synchronized void close() throws IOException {
        unloadAllSectors();
        writeIDTable();
        cleanSectorArray();
    }

    /**
     * Attempts to load the file from the file system. When already opened, the file will be reloaded and any made
     * changes are discared.
     *
     * @return True if the file is successfully loaded, false otherwise.
     *
     * @throws IOException If an I/O error occurs.
     */
    public synchronized boolean open() throws IOException {
        io.seek( 0 );
        int b = io.read();
        if( b >= 0 ) {
            readIDTable();
            return true;
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        io.close();
    }

    public void printDebug( PrintStream out ) {
        out.println( "===========================================================================================" );
        out.printf( "Sector count: %d\n", getSectorCount() );
        out.println( "Sectors:" );
        for( Sector s : sectors ) {
            if( s == null ) {
                out.println( "- NULL" );
            } else {
                out.printf( "- #%X@%d: %s", s.id, s.index, s.type );
                if( s.buffer != null ) {
                    out.print( ": " );
                    out.print( new String( s.buffer ) );
                }
                out.println();
            }
        }
        out.println( "Entries:" );
        for( Map.Entry<Long, Entry> entry : entries.entrySet() ) {
            out.printf( "- #%X: %s\n", entry.getKey(), Arrays.toString( entry.getValue().sectors ) );
        }
        out.println( "===========================================================================================" );
    }


    public static BMFFile create( File file, int initialCapacity, Compression compression, int sectorSize ) throws IOException {
        return new BMFFile( file, initialCapacity, compression, sectorSize );
    }

    public static BMFFile open( File file, int initialCapacity, Compression compression, int sectorSize ) throws IOException {
        BMFFile bmf = create( file, initialCapacity, compression, sectorSize );
        bmf.open();
        return bmf;
    }

    public static BMFFile createUncompressed( File file ) throws IOException {
        return create( file, 16, Compression.NONE, 1024 );
    }

    public static BMFFile openUncompressed( File file ) throws IOException {
        return open( file, 16, Compression.NONE, 1024 );
    }

    public static BMFFile createUncompressed( File file, int sectorSize ) throws IOException {
        return create( file, 16, Compression.NONE, sectorSize );
    }

    public static BMFFile openUncompressed( File file, int sectorSize ) throws IOException {
        return open( file, 16, Compression.NONE, sectorSize );
    }

    public static BMFFile createGZipped( File file ) throws IOException {
        return create( file, 16, Compression.GZIP, 1024 );
    }

    public static BMFFile openGZipped( File file ) throws IOException {
        return open( file, 16, Compression.GZIP, 1024 );
    }

    public static BMFFile createGZipped( File file, int sectorSize ) throws IOException {
        return create( file, 16, Compression.GZIP, sectorSize );
    }

    public static BMFFile openGZipped( File file, int sectorSize ) throws IOException {
        return open( file, 16, Compression.GZIP, sectorSize );
    }

    public static BMFFile createDeflated( File file ) throws IOException {
        return create( file, 16, Compression.DEFLATE, 1024 );
    }

    public static BMFFile openDeflated( File file ) throws IOException {
        return open( file, 16, Compression.DEFLATE, 1024 );
    }

    public static BMFFile createDeflated( File file, int sectorSize ) throws IOException {
        return create( file, 16, Compression.DEFLATE, sectorSize );
    }

    public static BMFFile openDeflated( File file, int sectorSize ) throws IOException {
        return open( file, 16, Compression.DEFLATE, sectorSize );
    }

    public static BMFFile create( File file, Compression compression ) throws IOException {
        return create( file, 16, compression, 1024 );
    }

    public static BMFFile open( File file, Compression compression ) throws IOException {
        return open( file, 16, compression, 1024 );
    }

    public static BMFFile create( File file, int sectorSize, Compression compression ) throws IOException {
        return create( file, 16, compression, sectorSize );
    }

    public static BMFFile open( File file, int sectorSize, Compression compression ) throws IOException {
        return open( file, 16, compression, sectorSize );
    }




    private static class Entry {
        private final long id;
        private int[] sectors;
        private boolean used;

        private Entry( long id ) {
            this.id = id;
            this.sectors = new int[ 0 ];
        }
    }

    private static class Sector {
        private final int id;
        private int index = - 1;
        private SectorType type = SectorType.UNLOADED;

        private byte[] buffer;

        private Sector( int id ) {
            this.id = id;
        }

        private Sector( int id, int index, int sectorSize ) {
            this.id = id;
            this.index = index;
            this.type = SectorType.DIRTY;
            this.buffer = new byte[ sectorSize ];
        }
    }

    private class EmptyInputStream extends InputStream {
        @Override
        public int read() {
            return - 1;
        }
    }

    private class SectorInputStream extends InputStream {

        private final Sector[] sectors;
        private final Entry entry;
        private final long length;
        private long index;
        private boolean closed;

        private SectorInputStream( Sector[] sectors, Entry entry ) {
            this.sectors = sectors;
            this.length = sectors.length * sectorSize;
            this.entry = entry;
            entry.used = true;
        }

        @Override
        public int read() throws IOException {
            synchronized( BMFFile.this ) {
                if( index < length ) {
                    int sectorIndex = (int) ( index >>> sectorSizeExponent );
                    int sectorByte = (int) ( index & sectorSizeMask );
                    index++;

                    Sector sector = sectors[ sectorIndex ];
                    loadSector( sector );
                    if( sector.buffer == null ) {
                        throw new IOException( "Cannot read from loaded sector with null buffer" );
                    }

                    return sector.buffer[ sectorByte ] & 0xFF;
                } else {
                    return - 1;
                }
            }
        }

        @Override
        public void close() throws IOException {
            synchronized( BMFFile.this ) {
                if( closed ) {
                    throw new IOException( "Already closed!" );
                }
                entry.used = false;
                closed = true;
            }
        }
    }

    private class SectorOutputStream extends OutputStream {
        private final ArrayList<Sector> sectors;
        private final Entry entry;
        private long index;
        private long length;
        private int sectorsWritten;
        private boolean closed;

        private SectorOutputStream( Sector[] sectors, Entry entry ) {
            this.sectors = new ArrayList<>( Arrays.asList( sectors ) );
            this.entry = entry;
            entry.used = true;
        }

        @Override
        public void write( int b ) throws IOException {
            extendIfNecessary();
            int sectorIndex = (int) ( index >>> sectorSizeExponent );
            int sectorByte = (int) ( index & sectorSizeMask );
            index++;

            Sector sector = sectors.get( sectorIndex );

            if( sector.type == SectorType.UNLOADED ) {
                sector.buffer = new byte[ sectorSize ];
            }
            if( sector.buffer == null ) {
                throw new IOException( "Writing to loaded sector without buffer" );
            }
            sector.type = SectorType.DIRTY;
            sector.buffer[ sectorByte ] = (byte) b;
        }

        private void extendIfNecessary() throws IOException {
            if( index >= length ) {
                sectorsWritten++;
                length += sectorSize;
                if( sectorsWritten >= sectors.size() ) {
                    sectors.add( newSector() );
                }
            }
        }

        @Override
        public void close() throws IOException {
            if( closed ) {
                throw new IOException( "Already closed!" );
            }
            entry.used = false;
            closed = true;
            setEntrySectorsAndRemoveOthers( entry, sectors, sectorsWritten );
        }
    }

    private enum SectorType {
        UNLOADED,
        LOADED,
        DIRTY
    }

    /**
     * Enumeration of implemented compression types.
     */
    public enum Compression {
        /** No compression: entries are stored in a raw binary format. */
        NONE( o -> o, i -> i ),
        /** GZip compression: entries are stored in a GZipped binary. */
        GZIP( GZIPOutputStream::new, GZIPInputStream::new ),
        /** DEFLATE compression: entries are stored in a DEFLATE-compressed binary */
        DEFLATE( DeflaterOutputStream::new, InflaterInputStream::new );

        private final StreamFactory<OutputStream> compressorFactory;
        private final StreamFactory<InputStream> decompressorFactory;

        Compression( StreamFactory<OutputStream> compressorFactory, StreamFactory<InputStream> decompressorFactory ) {
            this.compressorFactory = compressorFactory;
            this.decompressorFactory = decompressorFactory;
        }
    }

    @FunctionalInterface
    private interface StreamFactory<T> {
        T wrap( T stream ) throws IOException;
    }
}
