/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modul;

public interface ModulLogger {
    void debug( String msg );
    void info( String msg );
    void warn( String msg );
    void error( String msg );

    ModulLogger NONE = new ModulLogger() {
        @Override
        public void debug( String msg ) {
        }

        @Override
        public void info( String msg ) {
        }

        @Override
        public void warn( String msg ) {
        }

        @Override
        public void error( String msg ) {
        }
    };
}
