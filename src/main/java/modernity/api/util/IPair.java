/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.api.util;

public interface IPair <L, R> {
    L getLeft();
    R getRight();
    void setLeft( L left );
    void setRight( R right );

    static <L, R> IPair<L, R> wrap( IPair<L, R> other ) {
        return new IPair<L, R>() {
            @Override
            public L getLeft() {
                return other.getLeft();
            }

            @Override
            public R getRight() {
                return other.getRight();
            }

            @Override
            public void setLeft( L left ) {
                other.setLeft( left );
            }

            @Override
            public void setRight( R right ) {
                other.setRight( right );
            }
        };
    }

    static <L, R> IPair<L, R> wrapReadonly( IPair<L, R> other ) {
        return new IPair<L, R>() {
            @Override
            public L getLeft() {
                return other.getLeft();
            }

            @Override
            public R getRight() {
                return other.getRight();
            }

            @Override
            public void setLeft( L left ) {
                throw new UnsupportedOperationException( "setLeft" );
            }

            @Override
            public void setRight( R right ) {
                throw new UnsupportedOperationException( "setRight" );
            }
        };
    }

    static <L, R> Pair<L, R> pair() {
        return new Pair<>();
    }

    static <L, R> Pair<L, R> pair( L l, R r ) {
        return new Pair<>( l, r );
    }

    static <L, R> Pair<L, R> pair( IPair<L, R> copy ) {
        return new Pair<>( copy );
    }
}
