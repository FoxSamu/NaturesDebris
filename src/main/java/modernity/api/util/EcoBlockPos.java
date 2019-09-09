/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 11 - 2019
 */

package modernity.api.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Used in world generation to prevent large GC lag spikes. Use of immutable blockpos in worldgen is one of the most
 * performance-expensive tasks, making Minecraft's performance bad. This is what the maker of Optifine suggests.
 */
public final class EcoBlockPos extends BlockPos.MutableBlockPos implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger( "EcoBlockPos" );

    // Use vectors to keep thread safeness
    private static final Vector<PosInstance> FREE = new Vector<>();
    private static final Vector<PosInstance> USED = new Vector<>();
    private final PosInstance inst = new PosInstance( this );
    private static final PosInstance NULL_INST = new PosInstance( null );

    private boolean retained;

    private StackTraceElement retainer;
    private Thread retainerThread;


    // Use a small thread that controls cleaning up old instances
    private static void restartCleanup() {
        Thread cleanupThread = new Thread( () -> {
            while( true ) {
                cleanup();
                try {
                    Thread.sleep( 1000 );
                } catch( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }, "EcoBlockPos Cleanup Thread" );
        cleanupThread.setDaemon( true );
        cleanupThread.start();
        cleanupThread.setUncaughtExceptionHandler( ( t, e ) -> {
            LOGGER.error( "EcoBlockPos cleanup failed!", e );
            restartCleanup();
        } );
    }

    static {
        restartCleanup();
    }

    private static int warningLimit = 170;

    private EcoBlockPos() {
    }

    public static synchronized EcoBlockPos retain() {
        PosInstance pos;
        // MAYBE: Synchronize FREE? Possible exceptions may occur (never occured for now)
        if( FREE.isEmpty() ) {
            pos = new EcoBlockPos().inst;
            pos.pos.retained = true;
            pos.pos.retainerThread = Thread.currentThread();

            USED.add( pos );
        } else {
            pos = FREE.remove( 0 );
            if( pos == null || pos.pos.retained ) pos = new EcoBlockPos().inst;
            pos.pos.retained = true;
            pos.pos.retainerThread = Thread.currentThread();
            pos.pos.setPos( 0, 0, 0 );
            USED.add( pos );
        }
        pos.pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        pos.pos.retained = true;
        return pos.pos;
    }

    public static synchronized EcoBlockPos construct() {
        EcoBlockPos pos = new EcoBlockPos();
        USED.add( pos.inst );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        pos.retainerThread = Thread.currentThread();
        pos.retained = true;
        return pos;
    }

    public static synchronized EcoBlockPos retain( int x, int y, int z ) {
        EcoBlockPos pos = retain();
        pos.setPos( x, y, z );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static synchronized EcoBlockPos retain( double x, double y, double z ) {
        EcoBlockPos pos = retain();
        pos.setPos( x, y, z );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static synchronized EcoBlockPos retain( Vec3i pos1 ) {
        EcoBlockPos pos = retain();
        pos.setPos( pos1 );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static EcoBlockPos construct( int x, int y, int z ) {
        EcoBlockPos pos = construct();
        pos.setPos( x, y, z );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static EcoBlockPos construct( double x, double y, double z ) {
        EcoBlockPos pos = construct();
        pos.setPos( x, y, z );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static EcoBlockPos construct( Vec3i pos1 ) {
        EcoBlockPos pos = construct();
        pos.setPos( pos1 );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    public static synchronized void cleanup() {
        // No synchronization on FREE: the vector synchronizes itself when clearing
        if( FREE.size() > 200 ) {
            int size = FREE.size();
            killFree();
            LOGGER.info( "Removed " + size + " free EcoBlockPos instances..." );
        }
        int removed = 0;
        // Synchronize on USED to prevent modification during cleanup
        synchronized( USED ) {
            while( USED.contains( null ) ) {
                if( USED.remove( null ) ) removed++;
            }
            while( USED.contains( NULL_INST ) ) {
                if( USED.remove( NULL_INST ) ) removed++;
            }
            if( removed > 10 ) {
                LOGGER.info( "Removed " + removed + " null EcoBlockPos references..." );
            }
            killFromDeadThreads();
            if( USED.size() >= warningLimit + 30 ) {
                LOGGER.warn( "Using a lot of EcoBlockPos instances! Amount of used instances is " + USED.size() + "... Execute '/mddebug dumpEBPRetainers' command for info about in-use retainer methods..." );
                warningLimit = USED.size();
            }
        }
    }

    /**
     * Feeds all free instances to the garbage collector by deleting them from the pool.
     */
    public static synchronized void killFree() {
        FREE.clear();
    }

    /**
     * Cleanup old unreleased {@link EcoBlockPos} instances that have been retained on a now interrupted or dead thread.
     * Since instances should not be used on multiple threads, we can safely clean these up.
     */
    public static synchronized void killFromDeadThreads() {
        int removed = 0;
        for( int i = 0; i < USED.size(); i++ ) {
            PosInstance inst = USED.get( i );
            if( inst.pos == null ) continue;
            Thread t = inst.pos.retainerThread;
            if( t.isInterrupted() || ! t.isAlive() ) {
                USED.remove( i );
                if( USED.isEmpty() ) break;
                i--;
                removed++;
            }
        }

        if( removed > 0 ) {
            LOGGER.info( "Removed " + removed + " EcoBlockPos instances from a dead or interrupted thread" );
        }
    }

    /**
     * Puts this blockpos back into the pool
     */
    public synchronized final void release() {
        if( USED.contains( inst ) ) {
            USED.remove( inst );
            FREE.add( inst );
            retained = false;
        }
    }

    /**
     * Feeds this blockpos to the garbage collector by deleting it's reference.
     */
    public final synchronized void kill() {
        retained = false;
        USED.remove( inst );
    }

    // These methods are used to return a value and release a blockpos at the same time (convenience)...

    public final synchronized boolean release( boolean t ) {
        release();
        return t;
    }

    public final synchronized byte release( byte t ) {
        release();
        return t;
    }

    public final synchronized short release( short t ) {
        release();
        return t;
    }

    public final synchronized int release( int t ) {
        release();
        return t;
    }

    public final synchronized long release( long t ) {
        release();
        return t;
    }

    public final synchronized char release( char t ) {
        release();
        return t;
    }

    public final synchronized double release( double t ) {
        release();
        return t;
    }

    public final synchronized float release( float t ) {
        release();
        return t;
    }

    public final synchronized <T> T release( T t ) {
        release();
        return t;
    }

    /**
     * Releases this instance and returns the current coordinates as an immutable block pos.
     * @return The block pos with the coordinates of the released instance.
     */
    public final synchronized BlockPos immutableAndRelease() {
        BlockPos immutable = toImmutable();
        release();
        return immutable;
    }

    public final synchronized boolean kill( boolean t ) {
        kill();
        return t;
    }

    public final synchronized byte kill( byte t ) {
        kill();
        return t;
    }

    public final synchronized short kill( short t ) {
        kill();
        return t;
    }

    public final synchronized int kill( int t ) {
        kill();
        return t;
    }

    public final synchronized long kill( long t ) {
        kill();
        return t;
    }

    public final synchronized char kill( char t ) {
        kill();
        return t;
    }

    public final synchronized double kill( double t ) {
        kill();
        return t;
    }

    public final synchronized float kill( float t ) {
        kill();
        return t;
    }

    public final synchronized <T> T kill( T t ) {
        kill();
        return t;
    }

    public final synchronized BlockPos immutableAndKill() {
        BlockPos immutable = toImmutable();
        kill();
        return immutable;
    }

    private void checkStatus() {
        if( ! retained ) // Prevent released or killed instances from being used
            throw new IllegalStateException( "Use of EcoBlockPos after releasing. This instance was originally retained at: " + retainer );
        // Make sure that this instance is only used on the thread it's retained on. EcoBlockPos instances must not be used in multithreading...
        if( Thread.currentThread() != retainerThread )
            throw new IllegalStateException( "EcoBlockPos leaked from thread! EcoBlockPos instances must not be used in multiple threads... \n - Retainer thread: " + retainerThread + "\n - Current thread " + Thread.currentThread() );
    }

    public EcoBlockPos moveDown() {
        checkStatus();
        return move( EnumFacing.DOWN, 1 );
    }

    public EcoBlockPos moveUp() {
        checkStatus();
        return move( EnumFacing.UP, 1 );
    }

    public EcoBlockPos moveEast() {
        checkStatus();
        return move( EnumFacing.EAST, 1 );
    }

    public EcoBlockPos moveWest() {
        checkStatus();
        return move( EnumFacing.WEST, 1 );
    }

    public EcoBlockPos moveNorth() {
        checkStatus();
        return move( EnumFacing.NORTH, 1 );
    }

    public EcoBlockPos moveSouth() {
        checkStatus();
        return move( EnumFacing.SOUTH, 1 );
    }

    public EcoBlockPos moveDown( int offset ) {
        checkStatus();
        return move( EnumFacing.DOWN, offset );
    }

    public EcoBlockPos moveUp( int offset ) {
        checkStatus();
        return move( EnumFacing.UP, offset );
    }

    public EcoBlockPos moveEast( int offset ) {
        checkStatus();
        return move( EnumFacing.EAST, offset );
    }

    public EcoBlockPos moveWest( int offset ) {
        checkStatus();
        return move( EnumFacing.WEST, offset );
    }

    public EcoBlockPos moveNorth( int offset ) {
        checkStatus();
        return move( EnumFacing.NORTH, offset );
    }

    public EcoBlockPos moveSouth( int offset ) {
        checkStatus();
        return move( EnumFacing.SOUTH, offset );
    }

    public EcoBlockPos addPos( BlockPos pos ) {
        checkStatus();
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    public EcoBlockPos addPos( int x, int y, int z ) {
        checkStatus();
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public EcoBlockPos copy() {
        checkStatus();
        EcoBlockPos pos = retain( this );
        pos.retainer = Thread.currentThread().getStackTrace()[ 2 ];
        return pos;
    }

    @Override
    public EcoBlockPos setPos( Vec3i vec ) {
        checkStatus();
        return (EcoBlockPos) super.setPos( vec );
    }

    @Override
    public EcoBlockPos setPos( Entity entityIn ) {
        checkStatus();
        return (EcoBlockPos) super.setPos( entityIn );
    }

    @Override
    public EcoBlockPos setPos( int xIn, int yIn, int zIn ) {
        checkStatus();
        return (EcoBlockPos) super.setPos( xIn, yIn, zIn );
    }

    @Override
    public EcoBlockPos setPos( double xIn, double yIn, double zIn ) {
        checkStatus();
        return (EcoBlockPos) super.setPos( xIn, yIn, zIn );
    }

    @Override
    public BlockPos add( double x, double y, double z ) {
        checkStatus();
        return super.add( x, y, z );
    }

    @Override
    public BlockPos add( int x, int y, int z ) {
        checkStatus();
        return super.add( x, y, z );
    }

    @Override
    public BlockPos offset( EnumFacing facing, int n ) {
        checkStatus();
        return super.offset( facing, n );
    }

    @Override
    public BlockPos rotate( Rotation rotationIn ) {
        checkStatus();
        return super.rotate( rotationIn );
    }

    @Override
    public int getX() {
        checkStatus();
        return super.getX();
    }

    @Override
    public int getY() {
        checkStatus();
        return super.getY();
    }

    @Override
    public int getZ() {
        checkStatus();
        return super.getZ();
    }

    @Override
    public EcoBlockPos move( EnumFacing facing ) {
        checkStatus();
        return (EcoBlockPos) super.move( facing );
    }

    @Override
    public EcoBlockPos move( EnumFacing facing, int n ) {
        checkStatus();
        return (EcoBlockPos) super.move( facing, n );
    }

    @Override
    public void setY( int yIn ) {
        checkStatus();
        super.setY( yIn );
    }

    @Override
    public BlockPos toImmutable() {
        checkStatus();
        return super.toImmutable();
    }

    @Override
    public BlockPos add( Vec3i vec ) {
        checkStatus();
        return super.add( vec );
    }

    @Override
    public BlockPos subtract( Vec3i vec ) {
        checkStatus();
        return super.subtract( vec );
    }

    @Override
    public BlockPos up() {
        checkStatus();
        return super.up();
    }

    @Override
    public BlockPos up( int n ) {
        checkStatus();
        return super.up( n );
    }

    @Override
    public BlockPos down() {
        checkStatus();
        return super.down();
    }

    @Override
    public BlockPos down( int n ) {
        checkStatus();
        return super.down( n );
    }

    @Override
    public BlockPos north() {
        checkStatus();
        return super.north();
    }

    @Override
    public BlockPos north( int n ) {
        checkStatus();
        return super.north( n );
    }

    @Override
    public BlockPos south() {
        checkStatus();
        return super.south();
    }

    @Override
    public BlockPos south( int n ) {
        checkStatus();
        return super.south( n );
    }

    @Override
    public BlockPos west() {
        checkStatus();
        return super.west();
    }

    @Override
    public BlockPos west( int n ) {
        checkStatus();
        return super.west( n );
    }

    @Override
    public BlockPos east() {
        checkStatus();
        return super.east();
    }

    @Override
    public BlockPos east( int n ) {
        checkStatus();
        return super.east( n );
    }

    @Override
    public BlockPos offset( EnumFacing facing ) {
        checkStatus();
        return super.offset( facing );
    }

    @Override
    public BlockPos crossProduct( Vec3i vec ) {
        checkStatus();
        return super.crossProduct( vec );
    }

    @Override
    public long toLong() {
        checkStatus();
        return super.toLong();
    }

    @Override
    public double getDistance( int xIn, int yIn, int zIn ) {
        checkStatus();
        return super.getDistance( xIn, yIn, zIn );
    }

    @Override
    public double distanceSq( double toX, double toY, double toZ ) {
        checkStatus();
        return super.distanceSq( toX, toY, toZ );
    }

    @Override
    public double distanceSqToCenter( double xIn, double yIn, double zIn ) {
        checkStatus();
        return super.distanceSqToCenter( xIn, yIn, zIn );
    }

    @Override
    public double distanceSq( Vec3i to ) {
        checkStatus();
        return super.distanceSq( to );
    }

    public static int getFree() {
        return FREE.size();
    }

    public static int getUsed() {
        return USED.size();
    }

    public static synchronized void resetAll() {
        int used = getUsed();
        int free = getFree();
        LOGGER.info( "Removed " + used + " used and " + free + " free EcoBlockPos instances." );
    }

    public static List<StackTraceElement> getRetainersOfUsed() {
        ArrayList<StackTraceElement> elements = Lists.newArrayList();
        for( PosInstance pos : USED ) {
            if( pos == null ) continue;
            if( pos.pos == null ) continue;
            elements.add( pos.pos.retainer );
        }
        return elements;
    }

    public static void dumpRetainersInLog() {
        LOGGER.info( "RETAINER DUMP:" );
        for( PosInstance pos : USED ) {
            if( pos == null ) continue;
            if( pos.pos == null ) continue;
            LOGGER.info( pos.pos.retainer + " in thread " + pos.pos.retainerThread );
        }
    }

    @Override
    public void close() {
        release();
    }

    /**
     * Wrapper class that replaces using {@link #equals} by using {@code ==} for the {@link ArrayList}. This must
     * prevent removing the wrong {@link EcoBlockPos} instance from the 'used' pool when calling 'release'...
     */
    private static class PosInstance {
        final EcoBlockPos pos;

        public PosInstance( EcoBlockPos pos ) {
            this.pos = pos;
        }

        @Override
        public boolean equals( Object obj ) {
            if( obj instanceof PosInstance ) return ( (PosInstance) obj ).pos == pos;
            return false;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode( pos );
        }
    }

    static class EBPIterator implements Iterator<EcoBlockPos> {

        private final EcoBlockPos pos = EcoBlockPos.retain();
        private final int fromx;
        private final int fromy;
        private final int tox;
        private final int toy;
        private final int toz;

        EBPIterator( int fromx, int fromy, int fromz, int tox, int toy, int toz ) {
            this.fromx = fromx;
            this.fromy = fromy;
            this.tox = tox;
            this.toy = toy;
            this.toz = toz;
            pos.setPos( fromx, fromy, fromz );
        }

        @Override
        public boolean hasNext() {
            return pos.x != tox && pos.y != toy && pos.z != toz;
        }

        public void release() {
            pos.release();
        }

        @Override
        public EcoBlockPos next() {
            if( ! hasNext() ) throw new UnsupportedOperationException( "No more iterations!" );
            if( pos.x != tox ) pos.x++;
            else {
                pos.x = fromx;
                if( pos.y != toy ) pos.y++;
                else {
                    pos.y = fromy;
                    pos.z++;
                }
            }
            return pos;
        }
    }

    public static class EBPIterable implements Iterable<EcoBlockPos>, AutoCloseable {

        private final EBPIterator iterator;
        private boolean gotiterator;

        public EBPIterable( int fromx, int fromy, int fromz, int tox, int toy, int toz ) {
            iterator = new EBPIterator( fromx, fromy, fromz, tox, toy, toz );
        }

        @Override
        public void close() {
            iterator.release();
        }

        @Override
        public Iterator<EcoBlockPos> iterator() {
            if( gotiterator ) throw new IllegalStateException( "Only one iterator should be obtained." );
            gotiterator = true;
            return iterator;
        }
    }

    public static EBPIterable allIn( BlockPos from, BlockPos to ) {
        return new EBPIterable( from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ() );
    }
}
