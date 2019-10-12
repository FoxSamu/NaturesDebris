/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 10 - 2019
 */

package modernity.api.util;

import java.util.Objects;

public class Pair <L, R> implements IPair<L, R> {

    protected L left;
    protected R right;

    public Pair() {
    }

    public Pair( L left, R right ) {
        this.left = left;
        this.right = right;
    }

    public Pair( IPair<L, R> copyFrom ) {
        this( copyFrom.getLeft(), copyFrom.getRight() );
    }

    @Override
    public L getLeft() {
        return left;
    }

    @Override
    public R getRight() {
        return right;
    }

    @Override
    public void setLeft( L left ) {
        this.left = left;
    }

    @Override
    public void setRight( R right ) {
        this.right = right;
    }

    public void copyLeft( IPair<L, ?> other ) {
        setLeft( other.getLeft() );
    }

    public void copyRight( IPair<?, R> other ) {
        setRight( other.getRight() );
    }

    public void copy( IPair<L, R> other ) {
        setLeft( other.getLeft() );
        setRight( other.getRight() );
    }

    @Override
    public Pair<L, R> clone() {
        return new Pair( this );
    }

    @Override
    public int hashCode() {
        return Objects.hash( left, right );
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj instanceof Pair ) {
            Pair other = (Pair) obj;
            return Objects.equals( left, other.left ) && Objects.equals( right, other.right );
        }
        return false;
    }
}
