/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 14 - 2020
 * Author: rgsw
 */

package net.redgalaxy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class SimpleAsyncExecutor implements Executor {
    private final List<Runnable> tasks = Collections.synchronizedList( new ArrayList<>() );

    private final ExecutionThread[] threads;
    private final String nameFormat;
    private final Consumer<Throwable> exceptionHandler;
    private final boolean autoStart;

    public SimpleAsyncExecutor( int threadCount, String nameFormat, Consumer<Throwable> exceptionHandler, boolean autoStart ) {
        this.nameFormat = nameFormat;
        this.exceptionHandler = exceptionHandler;
        this.threads = new ExecutionThread[ threadCount ];
        this.autoStart = autoStart;
    }

    @Override
    public void execute( Runnable command ) {
        if( autoStart ) {
            start();
        }
        tasks.add( command );
    }

    public boolean isActive() {
        for( ExecutionThread thr : threads ) {
            if( thr != null && thr.isAlive() ) return true;
        }
        return false;
    }

    public void start() {
        for( int i = 0; i < threads.length; i++ ) {
            if( threads[ i ] == null || ! threads[ i ].isAlive() ) {
                threads[ i ] = new ExecutionThread( String.format( nameFormat, i ) );
            }
        }
    }

    public void shutdown() {
        for( int i = 0; i < threads.length; i++ ) {
            if( threads[ i ] != null && threads[ i ].isAlive() ) {
                threads[ i ].interrupt();
            }
            threads[ i ] = null;
        }
    }

    private class ExecutionThread extends Thread {
        ExecutionThread( String name ) {
            super( name );
            start();
            setDaemon( true );
        }

        @SuppressWarnings( "InfiniteLoopStatement" )
        @Override
        public void run() {
            while( true ) {
                synchronized( tasks ) {
                    if( ! tasks.isEmpty() ) {
                        Runnable r = tasks.remove( 0 );
                        try {
                            r.run();
                        } catch( Throwable thr ) {
                            exceptionHandler.accept( thr );
                        }
                    }
                }
            }
        }
    }
}
