/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package runs;

import modernity.api.tsrg.GlobalTSRG;
import modernity.api.tsrg.TSRGMapping;

import java.util.Scanner;

public class Mappings {
    public static void main( String[] args ) {
        GlobalTSRG.load();
        Scanner scanner = new Scanner( System.in );
        boolean exit = false;
        while( ! exit ) {
            if( scanner.hasNextLine() ) {
                String line = scanner.nextLine();
                if( line.equals( "exit" ) ) {
                    exit = true;
                } else {
                    map( line );
                }
            }
        }
    }

    private static void map( String input ) {
        String[] sep = input.split( " " );
        String className = sep[ 0 ];

        TSRGMapping.ClassMapping classMapping = GlobalTSRG.get( className );
        if( classMapping == null ) {
            System.err.println( "No such class: '" + className + "'" );
            return;
        }

        if( sep.length == 1 ) {
            System.err.println( "No method or field specified" );
            return;
        }

        String key = sep[ 1 ];

        if( sep.length == 3 ) {
            String desc = sep[ 2 ];
            TSRGMapping.MethodKey mk = new TSRGMapping.MethodKey( key, desc );

            TSRGMapping.Mapping obfM = classMapping.methodObf( mk );

            if( obfM != null ) {
                System.out.println( obfM.obfName + " -> " + obfM.deobfName );
                return;
            }

            TSRGMapping.Mapping deobfM = classMapping.methodDeobf( mk );

            if( deobfM != null ) {
                System.out.println( deobfM.deobfName + " -> " + deobfM.obfName );
                return;
            }

            System.err.println( "No such method: '" + mk.name + " " + mk.desc + "'" );
        } else {

            TSRGMapping.Mapping obfM = classMapping.fieldObf( key );

            if( obfM != null ) {
                System.out.println( obfM.obfName + " -> " + obfM.deobfName );
                return;
            }

            TSRGMapping.Mapping deobfM = classMapping.fieldDeobf( key );

            if( deobfM != null ) {
                System.out.println( deobfM.deobfName + " -> " + deobfM.obfName );
                return;
            }

            System.err.println( "No such field: '" + key + "'" );
        }
    }
}
