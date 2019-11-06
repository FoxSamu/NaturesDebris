package runs;

import net.rgsw.io.BMFFile;

import java.io.*;

public final class DatabaseFileTest {
    private DatabaseFileTest() {
    }

    private static BMFFile file;

    public static void main( String[] args ) throws IOException {
        open();
        put( 1, "This is some text in BMF" );
        put( 2, "This is other text in BMF" );
        put( 3, "Loooooooooooooooooooooooooooooooooooooooooooooooong text" );
        put( 4, "ShortTxt" );
        put( 5, "A LOT OF RANDOM DATA IS COMING" );
        close();

        open();
        get( 1 );
        get( 2 );
        get( 3 );
        put( 1, "This is a lot of text for entry 1 that is longer than before!" );
        get( 4 );
        get( 2 );
        close();

        open();
        put( 2, "aoksdpqweoimvqwopeibjwoepjeowpibmqbweoibjqwebiqwe" );
        put( 5, "akjsdfjkakjlsdfjklajklsdfljkjklasdjklfkjaskjldfkljakjlsdfkljakljsdfkljajklsdfjkljklasdkljfjklasdf" );
        get( 1 );
        get( 2 );
        get( 5 );
        close();
    }

    public static void open() throws IOException {
        file = BMFFile.open( new File( "database.data" ), 16, BMFFile.Compression.DEFLATE );
    }

    public static void close() throws IOException {
        file.close();
    }

    public static void put( long key, String value ) throws IOException {
        OutputStream stream = file.writeEntry( key );
        DataOutput out = new DataOutputStream( stream );
        out.writeUTF( value );
        stream.close();
    }

    public static void get( long key ) throws IOException {
        InputStream stream = file.readEntry( key );
        DataInput in = new DataInputStream( stream );
        System.out.println( in.readUTF() );
        stream.close();
    }
}
