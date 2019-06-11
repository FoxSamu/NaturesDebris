/*
 * Copyright (c) 2019 RGSW.
 * This file belongs to a separate library, made for the Modernity.
 * Licensed under the Apache Licence v2.0. Do not redistribute.
 *
 * Date: 6 - 11 - 2019
 */

package net.rgsw;

import net.rgsw.tasks.queue.TaskQueue;
import net.rgsw.tasks.task.ITaskResult;
import net.rgsw.tasks.task.ITaskRunnable;
import net.rgsw.tasks.task.NoPriority;
import net.rgsw.tasks.task.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Deprecated
class CopyFilesThreaded {
    private static String from;
    private static String to;
    private static TaskQueue<Res, NoPriority> queue = new TaskQueue<>();

    public static void main( String[] args ) throws IOException {
        if( args.length < 2 ) {
            throw new RuntimeException( "Args: <from> <to> [folder]..." );
        }


        from = args[ 0 ];
        to = args[ 1 ];
//        System.out.println( from );
//        System.out.println( to );
//        System.out.println( transform( new File( from, "abc" ) ) );

        queue.setDaemon( false );
        queue.start();

        ArrayList<String> folders = new ArrayList<>( Arrays.asList( args ).subList( 2, args.length ) );
        if( folders.isEmpty() ) {
            copyFolder( new File( from ), new File( to ) );
        } else {
            for( String str : folders ) {
                copy( str );
            }
        }

        queue.onQueueEmpty( TaskQueue::stopRunning );
    }

    public static void copy( String name ) {
        File fromFile = new File( from, name );
        File toFile = new File( to, name );

        if( fromFile.isFile() ) {
            copyFile( fromFile, toFile );
        } else {
            copyFolder( fromFile, toFile );
        }
    }

    public static File transform( File fromFile ) {
        String f = fromFile.toString();
        if( f.startsWith( from ) ) {
            f = f.replace( from, to );
        }
        return new File( f );
    }

    public static void copyFolder( File from, File to ) {
        if( from.exists() && from.isDirectory() ) {
            File[] files = from.listFiles();
            assert files != null;
            for( File f : files ) {
                if( f.isFile() ) {
                    copyFile( f, transform( f ) );
                } else {
                    copyFolder( f, transform( f ) );
                }
            }
        }
    }

    public static void copyFile( File from, File to ) {
        queue.addTask( new Task<>( queue, new CopyFile( from, to ), NoPriority.NONE ) );
    }

    private static class Res implements ITaskResult {
        @Override
        public boolean isComplete() {
            return true;
        }
    }

    private static class CopyFile implements ITaskRunnable<Res> {
        public final File from;
        public final File to;

        private CopyFile( File from, File to ) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Res doTask() {
            try {
                if( from.exists() && ! from.isDirectory() ) {
                    System.out.printf( "[%s] Copying %s to %s\n", Thread.currentThread().getName(), from, to );
                    if( ! to.exists() ) {
                        to.getParentFile().mkdirs();
                        to.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream( to );
                    FileInputStream fis = new FileInputStream( from );
                    int i;

                    while( ( i = fis.read() ) >= 0 ) {
                        fos.write( i );
                    }
                    fis.close();
                    fos.close();
                    System.out.printf( "[%s] Copied successfully\n", Thread.currentThread().getName() );
                }
                return new Res();
            } catch( Throwable e ) {
                System.err.printf( "[%s] Failed to copy\n", Thread.currentThread().getName() );
                e.printStackTrace();
                return null;
            }
        }
    }
}
