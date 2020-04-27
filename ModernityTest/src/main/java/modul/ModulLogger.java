/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
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
