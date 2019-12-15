/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 7 - 2019
 */

package modernity.api.util;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedCollection <T> implements IPicker<T> {
    private final HashSet<Entry<T>> entries = new HashSet<>();
    private int totalWeight = - 1;

    private final Comparator<Entry<T>> sorter = Comparator.naturalOrder();
    private final Comparator<Entry<T>> inverseSorter = Comparator.reverseOrder();

    public void add( T object, int weight ) {
        check( weight );
        Entry<T> e = findEntry( object );
        if( e != null ) {
            e.weight += weight;
        } else {
            entries.add( new Entry<>( weight, object ) );
        }
        invalidateTotal();
    }

    public void set( T object, int weight ) {
        check( weight );
        Entry<T> e = findEntry( object );
        if( e != null ) {
            e.weight = weight;
        } else {
            entries.add( new Entry<>( weight, object ) );
        }
        invalidateTotal();
    }

    public int get( T object ) {
        Entry<T> e = findEntry( object );
        if( e != null ) {
            return e.weight;
        }
        return 0;
    }

    public boolean has( T object ) {
        return findEntry( object ) != null;
    }

    public T random() {
        return random( new Random() );
    }

    @Override
    public T random( Random rand ) {
        int total = totalWeight();
        if( total <= 0 ) return null;

        int random = rand.nextInt( total );
        for( Entry<T> e : entries ) {
            random -= e.weight;
            if( random < 0 ) {
                return e.object;
            }
        }

        return null;
    }

    public List<T> sorted( boolean inverse ) {
        return entries.stream()
                      .sorted( inverse ? inverseSorter : sorter )
                      .map( e -> e.object )
                      .collect( Collectors.toList() );
    }

    public void clear() {
        entries.clear();
        invalidateTotal();
    }

    private int computeTotalWeight() {
        if( entries.isEmpty() ) return 0;
        int weight = 0;
        for( Entry<T> e : entries ) {
            weight += e.weight;
        }
        return weight;
    }

    public int totalWeight() {
        if( totalWeight > - 1 ) {
            return totalWeight;
        }
        int weight = computeTotalWeight();
        totalWeight = weight;
        return weight;
    }

    private Entry<T> findEntry( T object ) {
        for( Entry<T> e : entries ) {
            if( Objects.equals( e.object, object ) ) {
                return e;
            }
        }
        return null;
    }

    private void check( int weight ) {
        if( weight < 0 ) {
            throw new IllegalArgumentException( "Negative weight" );
        }
    }

    private void invalidateTotal() {
        totalWeight = - 1;
    }

    private static class Entry <T> implements Comparable<Entry<T>> {
        int weight;
        final T object;

        private Entry( int weight, T object ) {
            this.weight = weight;
            this.object = object;
        }

        @Override
        public int hashCode() {
            return object.hashCode();
        }

        @Override
        public boolean equals( Object obj ) {
            return obj instanceof Entry && Objects.equals( object, ( (Entry) obj ).object );
        }

        @Override
        public int compareTo( Entry<T> o ) {
            return Integer.compare( weight, o.weight );
        }
    }
}
