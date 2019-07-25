/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 25 - 2019
 */

package modernity.api.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public class NoEditList <E> implements List<E> {
    private final List<E> wrapped;

    public NoEditList( List<E> wrapped ) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean contains( Object o ) {
        return wrapped.contains( o );
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr<>( wrapped.iterator() );
    }

    @Override
    public Object[] toArray() {
        return wrapped.toArray();
    }

    @Override
    public <T> T[] toArray( T[] a ) {
        return wrapped.toArray( a );
    }

    @Deprecated
    @Override
    public boolean add( E e ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public boolean remove( Object o ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Override
    public boolean containsAll( Collection<?> c ) {
        return wrapped.containsAll( c );
    }

    @Deprecated
    @Override
    public boolean addAll( Collection<? extends E> c ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public boolean addAll( int index, Collection<? extends E> c ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public boolean removeAll( Collection<?> c ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public boolean retainAll( Collection<?> c ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Override
    public E get( int index ) {
        return wrapped.get( index );
    }

    @Deprecated
    @Override
    public E set( int index, E element ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public void add( int index, E element ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Deprecated
    @Override
    public E remove( int index ) {
        throw new UnsupportedOperationException( "List unmodifiable" );
    }

    @Override
    public int indexOf( Object o ) {
        return wrapped.indexOf( o );
    }

    @Override
    public int lastIndexOf( Object o ) {
        return wrapped.lastIndexOf( o );
    }

    @Override
    public ListIterator<E> listIterator() {
        return new LItr<>( wrapped.listIterator() );
    }

    @Override
    public ListIterator<E> listIterator( int index ) {
        return new LItr<>( wrapped.listIterator( index ) );
    }

    @Override
    public List<E> subList( int fromIndex, int toIndex ) {
        return new NoEditList<>( wrapped.subList( fromIndex, toIndex ) );
    }

    private static class LItr <E> implements ListIterator<E> {
        private final ListIterator<E> wrapped;

        private LItr( ListIterator<E> wrapped ) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public E next() {
            return wrapped.next();
        }

        @Override
        public boolean hasPrevious() {
            return wrapped.hasPrevious();
        }

        @Override
        public E previous() {
            return wrapped.previous();
        }

        @Override
        public int nextIndex() {
            return wrapped.nextIndex();
        }

        @Override
        public int previousIndex() {
            return wrapped.previousIndex();
        }

        @Deprecated
        @Override
        public void remove() {
            throw new UnsupportedOperationException( "List unmodifiable" );
        }

        @Deprecated
        @Override
        public void set( E e ) {
            throw new UnsupportedOperationException( "List unmodifiable" );
        }

        @Deprecated
        @Override
        public void add( E e ) {
            throw new UnsupportedOperationException( "List unmodifiable" );
        }
    }

    private static class Itr <E> implements Iterator<E> {

        private final Iterator<E> wrapped;

        private Itr( Iterator<E> wrapped ) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public E next() {
            return wrapped.next();
        }

        @Deprecated
        @Override
        public void remove() {
            throw new UnsupportedOperationException( "List unmodifiable" );
        }

        @Override
        public void forEachRemaining( Consumer<? super E> action ) {
            wrapped.forEachRemaining( action );
        }
    }
}
