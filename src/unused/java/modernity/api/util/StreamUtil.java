/*
 * Copyright (c) 2019 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   11 - 14 - 2019
 * Author: rgsw
 */

package modernity.api.util;

import java.util.Collection;
import java.util.Spliterator;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.*;

public final class StreamUtil {
    private StreamUtil() {
    }

    public static LongStream streamLongs( Spliterator<Long> spliterator, boolean parallel ) {
        return StreamSupport.longStream( new LongSpliterator( spliterator ), parallel );
    }

    public static LongStream streamLongs( Stream<Long> stream ) {
        return streamLongs( stream.spliterator(), stream.isParallel() );
    }

    public static LongStream streamLongs( Collection<Long> collection, boolean parallel ) {
        return streamLongs( collection.spliterator(), parallel );
    }

    public static IntStream streamInts( Spliterator<Integer> spliterator, boolean parallel ) {
        return StreamSupport.intStream( new IntSpliterator( spliterator ), parallel );
    }

    public static IntStream streamInts( Stream<Integer> stream ) {
        return streamInts( stream.spliterator(), stream.isParallel() );
    }

    public static IntStream streamInts( Collection<Integer> collection, boolean parallel ) {
        return streamInts( collection.spliterator(), parallel );
    }

    public static DoubleStream streamDoubles( Spliterator<Double> spliterator, boolean parallel ) {
        return StreamSupport.doubleStream( new DoubleSpliterator( spliterator ), parallel );
    }

    public static DoubleStream streamDoubles( Stream<Double> stream ) {
        return streamDoubles( stream.spliterator(), stream.isParallel() );
    }

    public static DoubleStream streamDoubles( Collection<Double> collection, boolean parallel ) {
        return streamDoubles( collection.spliterator(), parallel );
    }

    private static class LongSpliterator implements Spliterator.OfLong {

        private final Spliterator<Long> wrapping;

        private LongSpliterator( Spliterator<Long> wrapping ) {
            this.wrapping = wrapping;
        }

        @Override
        public OfLong trySplit() {
            return new LongSpliterator( wrapping.trySplit() );
        }

        @Override
        public long estimateSize() {
            return wrapping.estimateSize();
        }

        @Override
        public int characteristics() {
            return wrapping.characteristics();
        }

        @Override
        public boolean tryAdvance( LongConsumer action ) {
            return wrapping.tryAdvance( action::accept );
        }
    }

    private static class IntSpliterator implements Spliterator.OfInt {

        private final Spliterator<Integer> wrapping;

        private IntSpliterator( Spliterator<Integer> wrapping ) {
            this.wrapping = wrapping;
        }

        @Override
        public OfInt trySplit() {
            return new IntSpliterator( wrapping.trySplit() );
        }

        @Override
        public long estimateSize() {
            return wrapping.estimateSize();
        }

        @Override
        public int characteristics() {
            return wrapping.characteristics();
        }

        @Override
        public boolean tryAdvance( IntConsumer action ) {
            return wrapping.tryAdvance( action::accept );
        }
    }

    private static class DoubleSpliterator implements Spliterator.OfDouble {

        private final Spliterator<Double> wrapping;

        private DoubleSpliterator( Spliterator<Double> wrapping ) {
            this.wrapping = wrapping;
        }

        @Override
        public OfDouble trySplit() {
            return new DoubleSpliterator( wrapping.trySplit() );
        }

        @Override
        public long estimateSize() {
            return wrapping.estimateSize();
        }

        @Override
        public int characteristics() {
            return wrapping.characteristics();
        }

        @Override
        public boolean tryAdvance( DoubleConsumer action ) {
            return wrapping.tryAdvance( action::accept );
        }
    }
}
