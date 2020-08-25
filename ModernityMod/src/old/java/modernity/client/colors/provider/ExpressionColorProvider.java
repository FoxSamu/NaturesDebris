/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.colors.provider;


// TODO Re-evaluate
//public class ExpressionColorProvider implements IColorProvider {
//    private final Expression expr;
//    private final IColorProvider[] providers;
//    private final ThreadLocal<int[]> colorCache;
//    private final int xIndex;
//    private final int yIndex;
//    private final int zIndex;
//
//    public ExpressionColorProvider( Expression expr, IColorProvider[] providers ) {
//        this.expr = expr;
//        this.providers = providers;
//        this.colorCache = ThreadLocal.withInitial( () -> new int[ providers.length ] );
//
//        xIndex = expr.indexOfVariable( "x" );
//        yIndex = expr.indexOfVariable( "y" );
//        zIndex = expr.indexOfVariable( "z" );
//    }
//
//    private double computeRed( BlockPos pos ) {
//        int[] cache = colorCache.get();
//        for( int i = 0; i < cache.length; i++ ) {
//            int col = cache[ i ];
//            expr.setVariable( i, ColorUtil.red( col ) );
//        }
//        expr.setVariable( xIndex, pos.getX() );
//        expr.setVariable( yIndex, pos.getY() );
//        expr.setVariable( zIndex, pos.getZ() );
//        return expr.evaluate();
//    }
//
//    private double computeGreen( BlockPos pos ) {
//        int[] cache = colorCache.get();
//        for( int i = 0; i < cache.length; i++ ) {
//            int col = cache[ i ];
//            expr.setVariable( i, ColorUtil.green( col ) );
//        }
//        expr.setVariable( xIndex, pos.getX() );
//        expr.setVariable( yIndex, pos.getY() );
//        expr.setVariable( zIndex, pos.getZ() );
//        return expr.evaluate();
//    }
//
//    private double computeBlue( BlockPos pos ) {
//        int[] cache = colorCache.get();
//        for( int i = 0; i < cache.length; i++ ) {
//            int col = cache[ i ];
//            expr.setVariable( i, ColorUtil.blue( col ) );
//        }
//        expr.setVariable( xIndex, pos.getX() );
//        expr.setVariable( yIndex, pos.getY() );
//        expr.setVariable( zIndex, pos.getZ() );
//        return expr.evaluate();
//    }
//
//    @Override
//    public int getColor( @Nullable IEnviromentBlockReader world, BlockPos pos ) {
//        int[] cache = colorCache.get();
//        for( int i = 0; i < providers.length; i++ ) {
//            int color = providers[ i ].getColor( world, pos );
//            cache[ i ] = color;
//        }
//        double r = computeRed( pos );
//        double g = computeGreen( pos );
//        double b = computeBlue( pos );
//        return ColorUtil.rgb( r, g, b );
//    }
//
//    @Override
//    public void initForSeed( long seed ) {
//        for( IColorProvider provider : providers ) {
//            if( provider != null ) provider.initForSeed( seed );
//        }
//    }
//}
