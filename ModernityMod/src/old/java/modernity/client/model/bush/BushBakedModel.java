/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.client.model.bush;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import modernity.client.model.QuadMaker;
import modernity.common.blockold.plant.BushBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BushBakedModel implements IDynamicBakedModel {

    private static final SideSelector UP = new SideSelector(BushModelProperties.UP);
    private static final SideSelector DOWN = new SideSelector(BushModelProperties.DOWN);
    private static final SideSelector EAST = new SideSelector(BushModelProperties.EAST);
    private static final SideSelector WEST = new SideSelector(BushModelProperties.WEST);
    private static final SideSelector NORTH = new SideSelector(BushModelProperties.NORTH);
    private static final SideSelector SOUTH = new SideSelector(BushModelProperties.SOUTH);
    private static final SideSelector UP_NORTH = new SideSelector(BushModelProperties.UP_NORTH);
    private static final SideSelector UP_SOUTH = new SideSelector(BushModelProperties.UP_SOUTH);
    private static final SideSelector DOWN_NORTH = new SideSelector(BushModelProperties.DOWN_NORTH);
    private static final SideSelector DOWN_SOUTH = new SideSelector(BushModelProperties.DOWN_SOUTH);
    private static final SideSelector EAST_UP = new SideSelector(BushModelProperties.UP_EAST);
    private static final SideSelector EAST_DOWN = new SideSelector(BushModelProperties.DOWN_EAST);
    private static final SideSelector WEST_UP = new SideSelector(BushModelProperties.UP_WEST);
    private static final SideSelector WEST_DOWN = new SideSelector(BushModelProperties.DOWN_WEST);
    private static final SideSelector NORTH_EAST = new SideSelector(BushModelProperties.NORTH_EAST);
    private static final SideSelector NORTH_WEST = new SideSelector(BushModelProperties.NORTH_WEST);
    private static final SideSelector SOUTH_EAST = new SideSelector(BushModelProperties.SOUTH_EAST);
    private static final SideSelector SOUTH_WEST = new SideSelector(BushModelProperties.SOUTH_WEST);

    private static final Predicate<IModelData> ALL = state -> true;

    private final int tint;
    private final boolean ao;
    private final boolean gui3d;
    private final boolean sideLit;
    private final Metrics metrics;
    private final TextureAtlasSprite texture;
    private final TextureAtlasSprite particles;
    private final ItemOverrideList overrides;
    private final TransformationMatrix transform;
    private final IModelTransform transforms;

    private final List<QuadDef> quads = new ArrayList<>();

    public BushBakedModel(TextureAtlasSprite texture, TextureAtlasSprite particles, IModelConfiguration owner, int tint, float rounding, float uvRatio, ItemOverrideList overrides) {
        this.tint = tint;
        this.ao = owner.useSmoothLighting();
        this.gui3d = owner.isShadedInGui();
        this.sideLit = owner.isSideLit();

        this.texture = texture;
        this.particles = particles;
        this.metrics = new Metrics(rounding, uvRatio);
        this.overrides = overrides;
        this.transforms = owner.getCombinedTransform();
        this.transform = transforms.getRotation();

        QuadMaker maker = new QuadMaker();
        maker.fixBleeding(true);

        build(maker);
    }

    private static <T> Predicate<T> merge(Predicate<T> a, Predicate<T> b, boolean invA, boolean invB) {
        if(invA) a = a.negate();
        if(invB) b = b.negate();
        return a.and(b);
    }

    private void build(QuadMaker maker) {
        buildNorth(maker);
        buildSouth(maker);
        buildEast(maker);
        buildWest(maker);
        buildDown(maker);
        buildUp(maker);
    }

    // Keep IDEA away from this code:
    // @formatter:off
    private void buildNorth( QuadMaker maker ) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add( ALL,       north( maker, 0, p1, p1, p2, p2 ) );

        add( WEST,      north( maker, 0, p0, p1, p1, p2, p2, p1, p3, p2 ) );
        add( EAST,      north( maker, 0, p2, p1, p3, p2, p0, p1, p1, p2 ) );
        add( UP,        north( maker, 0, p1, p2, p2, p3, p1, p0, p2, p1 ) );
        add( DOWN,      north( maker, 0, p1, p0, p2, p1, p1, p2, p2, p3 ) );

        add( WEST_DOWN, north( maker, 0, p0, p0, p1, p1, p2, p2, p3, p3 ) );
        add( WEST_UP,   north( maker, 0, p0, p2, p1, p3, p2, p0, p3, p1 ) );
        add( EAST_UP,   north( maker, 0, p2, p2, p3, p3, p0, p0, p1, p1 ) );
        add( EAST_DOWN, north( maker, 0, p2, p0, p3, p1, p0, p2, p1, p3 ) );

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = metrics.bevelInset;

        add( ALL,       north( maker, 0, i, i, 0, b1, b2, b2, b3, t1, t0, t2, t1 ) );
        add( ALL,       north( maker, i, 0, 0, i, b1, b0, b2, b1, t1, t2, t2, t3 ) );
        add( ALL,       north( maker, i, i, 0, 0, b0, b1, b1, b2, t2, t1, t3, t2 ) );
        add( ALL,       north( maker, 0, 0, i, i, b2, b1, b3, b2, t0, t1, t1, t2 ) );

        add( EAST,      north( maker, 0, i, i, 0, b2, b2, p3, b3, p0, t0, t1, t1 ) );
        add( EAST,      north( maker, i, 0, 0, i, b2, b0, p3, b1, p0, t2, t1, t3 ) );
        add( WEST,      north( maker, 0, i, i, 0, p0, b2, b1, b3, t2, t0, p3, t1 ) );
        add( WEST,      north( maker, i, 0, 0, i, p0, b0, b1, b1, t2, t2, p3, t3 ) );

        add( UP,        north( maker, i, i, 0, 0, b0, b2, b1, p3, t2, p0, t3, t1 ) );
        add( UP,        north( maker, 0, 0, i, i, b2, b2, b3, p3, t0, p0, t1, t1 ) );
        add( DOWN,      north( maker, i, i, 0, 0, b0, p0, b1, b1, t2, t2, t3, p3 ) );
        add( DOWN,      north( maker, 0, 0, i, i, b2, p0, b3, b1, t0, t2, t1, p3 ) );


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = metrics.cornerInset;

        add( ALL,       north( maker, 0, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, d0, d0, t0, t1, t1, t1, t1, t0 ) );
        add( ALL,       north( maker, i, j, i, 0, b0, b2, c0, c3, b1, b3, b1, b2, t2, t0, t2, t1, t3, t1, d3, d0 ) );
        add( ALL,       north( maker, j, i, 0, i, c0, c0, b0, b1, b1, b1, b1, b0, t2, t2, t2, t3, d3, d3, t3, t2 ) );
        add( ALL,       north( maker, i, 0, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t0, t2, d0, d3, t1, t3, t1, t2 ) );


        float m = metrics.sideLower;
        float n = metrics.sideUpper;

        add( ALL,       north( maker, m, m, n, n, p1, p0, p2, p3, p1, p0, p2, p3 ) );
        add( ALL,       north( maker, n, n, m, m, p1, p0, p2, p3, p1, p0, p2, p3 ) );
    }

    private void buildSouth( QuadMaker maker ) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add( ALL,       south( maker, 1, p1, p1, p2, p2 ) );

        add( WEST,      south( maker, 1, p0, p1, p1, p2 ) );
        add( EAST,      south( maker, 1, p2, p1, p3, p2 ) );
        add( UP,        south( maker, 1, p1, p2, p2, p3, p1, p0, p2, p1 ) );
        add( DOWN,      south( maker, 1, p1, p0, p2, p1, p1, p2, p2, p3 ) );

        add( WEST_DOWN, south( maker, 1, p0, p0, p1, p1, p0, p2, p1, p3 ) );
        add( WEST_UP,   south( maker, 1, p0, p2, p1, p3, p0, p0, p1, p1 ) );
        add( EAST_UP,   south( maker, 1, p2, p2, p3, p3, p2, p0, p3, p1 ) );
        add( EAST_DOWN, south( maker, 1, p2, p0, p3, p1, p2, p2, p3, p3 ) );

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = 1 - metrics.bevelInset;

        add( ALL,       south( maker, 1, i, i, 1, b1, b2, b2, b3, t1, t0, t2, t1 ) );
        add( ALL,       south( maker, i, 1, 1, i, b1, b0, b2, b1, t1, t2, t2, t3 ) );
        add( ALL,       south( maker, i, i, 1, 1, b0, b1, b1, b2, t0, t1, t1, t2 ) );
        add( ALL,       south( maker, 1, 1, i, i, b2, b1, b3, b2, t2, t1, t3, t2 ) );

        add( EAST,      south( maker, 1, i, i, 1, b2, b2, p3, b3, t2, t0, p3, t1 ) );
        add( EAST,      south( maker, i, 1, 1, i, b2, b0, p3, b1, t2, t2, p3, t3 ) );
        add( WEST,      south( maker, 1, i, i, 1, p0, b2, b1, b3, p0, t0, t1, t1 ) );
        add( WEST,      south( maker, i, 1, 1, i, p0, b0, b1, b1, p0, t2, t1, t3 ) );

        add( UP,        south( maker, i, i, 1, 1, b0, b2, b1, p3, t0, p0, t1, t1 ) );
        add( UP,        south( maker, 1, 1, i, i, b2, b2, b3, p3, t2, p0, t3, t1 ) );
        add( DOWN,      south( maker, i, i, 1, 1, b0, p0, b1, b1, t0, t2, t1, p3 ) );
        add( DOWN,      south( maker, 1, 1, i, i, b2, p0, b3, b1, t2, t2, t3, p3 ) );


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = 1 - metrics.cornerInset;

        add( ALL,       south( maker, 1, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, t2, t0, t2, t1, t3, t1, d3, d0 ) );
        add( ALL,       south( maker, i, j, i, 1, b0, b2, c0, c3, b1, b3, b1, b2, d0, d0, t0, t1, t1, t1, t1, t0 ) );
        add( ALL,       south( maker, j, i, 1, i, c0, c0, b0, b1, b1, b1, b1, b0, t0, t2, d0, d3, t1, t3, t1, t2 ) );
        add( ALL,       south( maker, i, 1, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t2, t2, t2, t3, d3, d3, t3, t2 ) );


        float m = metrics.sideLower;
        float n = metrics.sideUpper;

        add( ALL,       south( maker, m, m, n, n, p1, p0, p2, p3, p1, p0, p2, p3 ) );
        add( ALL,       south( maker, n, n, m, m, p1, p0, p2, p3, p1, p0, p2, p3 ) );
    }

    private void buildEast( QuadMaker maker ) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add( ALL,        east( maker, 1, p1, p1, p2, p2 ) );

        add( NORTH,      east( maker, 1, p0, p1, p1, p2, p2, p1, p3, p2 ) );
        add( SOUTH,      east( maker, 1, p2, p1, p3, p2, p0, p1, p1, p2 ) );
        add( UP,         east( maker, 1, p1, p2, p2, p3, p1, p0, p2, p1 ) );
        add( DOWN,       east( maker, 1, p1, p0, p2, p1, p1, p2, p2, p3 ) );

        add( DOWN_NORTH, east( maker, 1, p0, p0, p1, p1, p2, p2, p3, p3 ) );
        add( UP_NORTH,   east( maker, 1, p0, p2, p1, p3, p2, p0, p3, p1 ) );
        add( UP_SOUTH,   east( maker, 1, p2, p2, p3, p3, p0, p0, p1, p1 ) );
        add( DOWN_SOUTH, east( maker, 1, p2, p0, p3, p1, p0, p2, p1, p3 ) );

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = 1 - metrics.bevelInset;

        add( ALL,        east( maker, 1, i, i, 1, b1, b2, b2, b3, t1, t0, t2, t1 ) );
        add( ALL,        east( maker, i, 1, 1, i, b1, b0, b2, b1, t1, t2, t2, t3 ) );
        add( ALL,        east( maker, i, i, 1, 1, b0, b1, b1, b2, t2, t1, t3, t2 ) );
        add( ALL,        east( maker, 1, 1, i, i, b2, b1, b3, b2, t0, t1, t1, t2 ) );

        add( SOUTH,      east( maker, 1, i, i, 1, b2, b2, p3, b3, p0, t0, t1, t1 ) );
        add( SOUTH,      east( maker, i, 1, 1, i, b2, b0, p3, b1, p0, t2, t1, t3 ) );
        add( NORTH,      east( maker, 1, i, i, 1, p0, b2, b1, b3, t2, t0, p3, t1 ) );
        add( NORTH,      east( maker, i, 1, 1, i, p0, b0, b1, b1, t2, t2, p3, t3 ) );

        add( UP,         east( maker, i, i, 1, 1, b0, b2, b1, p3, t2, p0, t3, t1 ) );
        add( UP,         east( maker, 1, 1, i, i, b2, b2, b3, p3, t0, p0, t1, t1 ) );
        add( DOWN,       east( maker, i, i, 1, 1, b0, p0, b1, b1, t2, t2, t3, p3 ) );
        add( DOWN,       east( maker, 1, 1, i, i, b2, p0, b3, b1, t0, t2, t1, p3 ) );


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = 1 - metrics.cornerInset;

        add( ALL,        east( maker, 1, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, d0, d0, t0, t1, t1, t1, t1, t0 ) );
        add( ALL,        east( maker, i, j, i, 1, b0, b2, c0, c3, b1, b3, b1, b2, t2, t0, t2, t1, t3, t1, d3, d0 ) );
        add( ALL,        east( maker, j, i, 1, i, c0, c0, b0, b1, b1, b1, b1, b0, t2, t2, t2, t3, d3, d3, t3, t2 ) );
        add( ALL,        east( maker, i, 1, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t0, t2, d0, d3, t1, t3, t1, t2 ) );
    }

    private void buildWest( QuadMaker maker ) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add( ALL,        west( maker, 0, p1, p1, p2, p2 ) );

        add( NORTH,      west( maker, 0, p0, p1, p1, p2 ) );
        add( SOUTH,      west( maker, 0, p2, p1, p3, p2 ) );
        add( UP,         west( maker, 0, p1, p2, p2, p3, p1, p0, p2, p1 ) );
        add( DOWN,       west( maker, 0, p1, p0, p2, p1, p1, p2, p2, p3 ) );

        add( DOWN_NORTH, west( maker, 0, p0, p0, p1, p1, p0, p2, p1, p3 ) );
        add( UP_NORTH,   west( maker, 0, p0, p2, p1, p3, p0, p0, p1, p1 ) );
        add( UP_SOUTH,   west( maker, 0, p2, p2, p3, p3, p2, p0, p3, p1 ) );
        add( DOWN_SOUTH, west( maker, 0, p2, p0, p3, p1, p2, p2, p3, p3 ) );

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = metrics.bevelInset;

        add( ALL,        west( maker, 0, i, i, 0, b1, b2, b2, b3, t1, t0, t2, t1 ) );
        add( ALL,        west( maker, i, 0, 0, i, b1, b0, b2, b1, t1, t2, t2, t3 ) );
        add( ALL,        west( maker, i, i, 0, 0, b0, b1, b1, b2, t0, t1, t1, t2 ) );
        add( ALL,        west( maker, 0, 0, i, i, b2, b1, b3, b2, t2, t1, t3, t2 ) );

        add( SOUTH,      west( maker, 0, i, i, 0, b2, b2, p3, b3, t2, t0, p3, t1 ) );
        add( SOUTH,      west( maker, i, 0, 0, i, b2, b0, p3, b1, t2, t2, p3, t3 ) );
        add( NORTH,      west( maker, 0, i, i, 0, p0, b2, b1, b3, p0, t0, t1, t1 ) );
        add( NORTH,      west( maker, i, 0, 0, i, p0, b0, b1, b1, p0, t2, t1, t3 ) );

        add( UP,         west( maker, i, i, 0, 0, b0, b2, b1, p3, t0, p0, t1, t1 ) );
        add( UP,         west( maker, 0, 0, i, i, b2, b2, b3, p3, t2, p0, t3, t1 ) );
        add( DOWN,       west( maker, i, i, 0, 0, b0, p0, b1, b1, t0, t2, t1, p3 ) );
        add( DOWN,       west( maker, 0, 0, i, i, b2, p0, b3, b1, t2, t2, t3, p3 ) );


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = metrics.cornerInset;

        add( ALL,        west( maker, 0, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, t2, t0, t2, t1, t3, t1, d3, d0 ) );
        add( ALL,        west( maker, i, j, i, 0, b0, b2, c0, c3, b1, b3, b1, b2, d0, d0, t0, t1, t1, t1, t1, t0 ) );
        add( ALL,        west( maker, j, i, 0, i, c0, c0, b0, b1, b1, b1, b1, b0, t0, t2, d0, d3, t1, t3, t1, t2 ) );
        add( ALL,        west( maker, i, 0, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t2, t2, t2, t3, d3, d3, t3, t2 ) );
    }

    private void buildUp( QuadMaker maker ) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add( ALL,          up( maker, 1, p1, p1, p2, p2 ) );

        add( WEST,         up( maker, 1, p0, p1, p1, p2 ) );
        add( EAST,         up( maker, 1, p2, p1, p3, p2 ) );
        add( SOUTH,        up( maker, 1, p1, p2, p2, p3 ) );
        add( NORTH,        up( maker, 1, p1, p0, p2, p1 ) );

        add( NORTH_WEST,   up( maker, 1, p0, p0, p1, p1 ) );
        add( SOUTH_WEST,   up( maker, 1, p0, p2, p1, p3 ) );
        add( SOUTH_EAST,   up( maker, 1, p2, p2, p3, p3 ) );
        add( NORTH_EAST,   up( maker, 1, p2, p0, p3, p1 ) );

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = 1 - metrics.bevelInset;

        add( ALL,          up( maker, 1, i, i, 1, b1, b2, b2, b3, t1, t2, t2, t3 ) );
        add( ALL,          up( maker, i, 1, 1, i, b1, b0, b2, b1, t1, t0, t2, t1 ) );
        add( ALL,          up( maker, i, i, 1, 1, b0, b1, b1, b2, t0, t1, t1, t2 ) );
        add( ALL,          up( maker, 1, 1, i, i, b2, b1, b3, b2, t2, t1, t3, t2 ) );

        add( EAST,         up( maker, 1, i, i, 1, b2, b2, p3, b3, t2, t2, p3, t3 ) );
        add( EAST,         up( maker, i, 1, 1, i, b2, b0, p3, b1, t2, t0, p3, t1 ) );
        add( WEST,         up( maker, 1, i, i, 1, p0, b2, b1, b3, p0, t2, t1, t3 ) );
        add( WEST,         up( maker, i, 1, 1, i, p0, b0, b1, b1, p0, t0, t1, t1 ) );

        add( SOUTH,        up( maker, i, i, 1, 1, b0, b2, b1, p3, t0, t2, t1, p3 ) );
        add( SOUTH,        up( maker, 1, 1, i, i, b2, b2, b3, p3, t2, t2, t3, p3 ) );
        add( NORTH,        up( maker, i, i, 1, 1, b0, p0, b1, b1, t0, p0, t1, t1 ) );
        add( NORTH,        up( maker, 1, 1, i, i, b2, p0, b3, b1, t2, p0, t3, t1 ) );


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = 1 - metrics.cornerInset;

        add( ALL,          up( maker, 1, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, t2, t2, t2, t3, d3, d3, t3, t2 ) );
        add( ALL,          up( maker, i, j, i, 1, b0, b2, c0, c3, b1, b3, b1, b2, t0, t2, d0, d3, t1, t3, t1, t2 ) );
        add( ALL,          up( maker, j, i, 1, i, c0, c0, b0, b1, b1, b1, b1, b0, d0, d0, t0, t1, t1, t1, t1, t0 ) );
        add( ALL,          up( maker, i, 1, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t2, t0, t2, t1, t3, t1, d3, d0 ) );


        float m = metrics.sideLower;
        float n = metrics.sideUpper;

        add( ALL,          up( maker, m, n, n, m, p0, p1, p3, p2, p0, p1, p3, p2 ) );
        add( ALL,          up( maker, n, m, m, n, p0, p1, p3, p2, p0, p1, p3, p2 ) );
        add( ALL,          up( maker, m, m, n, n, p1, p0, p2, p3, p1, p0, p2, p3 ) );
        add( ALL,          up( maker, n, n, m, m, p1, p0, p2, p3, p1, p0, p2, p3 ) );
    }
    // @formatter:on

    private void buildDown(QuadMaker maker) {
        float p0 = 0;
        float p1 = metrics.sideLower;
        float p2 = metrics.sideUpper;
        float p3 = 1;

        add(ALL, down(maker, 0, p1, p1, p2, p2));

        add(WEST, down(maker, 0, p0, p1, p1, p2));
        add(EAST, down(maker, 0, p2, p1, p3, p2));
        add(SOUTH, down(maker, 0, p1, p2, p2, p3, p1, p0, p2, p1));
        add(NORTH, down(maker, 0, p1, p0, p2, p1, p1, p2, p2, p3));

        add(NORTH_WEST, down(maker, 0, p0, p0, p1, p1, p0, p2, p1, p3));
        add(SOUTH_WEST, down(maker, 0, p0, p2, p1, p3, p0, p0, p1, p1));
        add(SOUTH_EAST, down(maker, 0, p2, p2, p3, p3, p2, p0, p3, p1));
        add(NORTH_EAST, down(maker, 0, p2, p0, p3, p1, p2, p2, p3, p3));

        float b0 = metrics.bevelPLower;
        float b1 = metrics.sideLower;
        float b2 = metrics.sideUpper;
        float b3 = metrics.bevelPUpper;

        float t0 = metrics.bevelTLower;
        float t1 = metrics.sideLower;
        float t2 = metrics.sideUpper;
        float t3 = metrics.bevelTUpper;

        float i = metrics.bevelInset;

        add(ALL, down(maker, 0, i, i, 0, b1, b2, b2, b3, t1, t0, t2, t1));
        add(ALL, down(maker, i, 0, 0, i, b1, b0, b2, b1, t1, t2, t2, t3));
        add(ALL, down(maker, i, i, 0, 0, b0, b1, b1, b2, t0, t1, t1, t2));
        add(ALL, down(maker, 0, 0, i, i, b2, b1, b3, b2, t2, t1, t3, t2));

        add(EAST, down(maker, 0, i, i, 0, b2, b2, p3, b3, t2, t0, p3, t1));
        add(EAST, down(maker, i, 0, 0, i, b2, b0, p3, b1, t2, t2, p3, t3));
        add(WEST, down(maker, 0, i, i, 0, p0, b2, b1, b3, p0, t0, t1, t1));
        add(WEST, down(maker, i, 0, 0, i, p0, b0, b1, b1, p0, t2, t1, t3));

        add(SOUTH, down(maker, i, i, 0, 0, b0, b2, b1, p3, t0, p0, t1, t1));
        add(SOUTH, down(maker, 0, 0, i, i, b2, b2, b3, p3, t2, p0, t3, t1));
        add(NORTH, down(maker, i, i, 0, 0, b0, p0, b1, b1, t0, t2, t1, p3));
        add(NORTH, down(maker, 0, 0, i, i, b2, p0, b3, b1, t2, t2, t3, p3));


        float c0 = metrics.cornerPLower;
        float c3 = metrics.cornerPUpper;

        float d0 = metrics.cornerTLower;
        float d3 = metrics.cornerTUpper;

        float j = metrics.cornerInset;

        add(ALL, down(maker, 0, i, j, i, b2, b2, b2, b3, c3, c3, b3, b2, t2, t0, t2, t1, t3, t1, d3, d0));
        add(ALL, down(maker, i, j, i, 0, b0, b2, c0, c3, b1, b3, b1, b2, d0, d0, t0, t1, t1, t1, t1, t0));
        add(ALL, down(maker, j, i, 0, i, c0, c0, b0, b1, b1, b1, b1, b0, t0, t2, d0, d3, t1, t3, t1, t2));
        add(ALL, down(maker, i, 0, i, j, b2, b0, b2, b1, b3, b1, c3, c0, t2, t2, t2, t3, d3, d3, t3, t2));


        float m = metrics.sideLower;
        float n = metrics.sideUpper;

        add(ALL, down(maker, m, n, n, m, p0, p1, p3, p2, p0, p1, p3, p2));
        add(ALL, down(maker, n, m, m, n, p0, p1, p3, p2, p0, p1, p3, p2));
        add(ALL, down(maker, m, m, n, n, p1, p0, p2, p3, p1, p0, p2, p3));
        add(ALL, down(maker, n, n, m, m, p1, p0, p2, p3, p1, p0, p2, p3));
    }

    private BakedQuad up(QuadMaker maker, float y11, float y12, float y22, float y21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.UP).tint(tint);
        maker.pos(p11, y11, q11).tex(u11, v11).end();
        maker.pos(p12, y12, q12).tex(u12, v12).end();
        maker.pos(p22, y22, q22).tex(u22, v22).end();
        maker.pos(p21, y21, q21).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad up(QuadMaker maker, float y11, float y12, float y22, float y21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.UP).tint(tint);
        maker.pos(p1, y11, q1).tex(u1, v1).end();
        maker.pos(p1, y12, q2).tex(u1, v2).end();
        maker.pos(p2, y22, q2).tex(u2, v2).end();
        maker.pos(p2, y21, q1).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad up(QuadMaker maker, float y, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return up(maker, y, y, y, y, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad up(QuadMaker maker, float y, float p1, float q1, float p2, float q2) {
        return up(maker, y, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private BakedQuad down(QuadMaker maker, float y11, float y12, float y22, float y21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.DOWN).tint(tint);
        maker.pos(p12, y12, q12).tex(u11, v11).end();
        maker.pos(p11, y11, q11).tex(u12, v12).end();
        maker.pos(p21, y21, q21).tex(u22, v22).end();
        maker.pos(p22, y22, q22).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad down(QuadMaker maker, float y11, float y12, float y22, float y21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.DOWN).tint(tint);
        maker.pos(p1, y12, q2).tex(u1, v1).end();
        maker.pos(p1, y11, q1).tex(u1, v2).end();
        maker.pos(p2, y21, q1).tex(u2, v2).end();
        maker.pos(p2, y22, q2).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad down(QuadMaker maker, float y, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return down(maker, y, y, y, y, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad down(QuadMaker maker, float y, float p1, float q1, float p2, float q2) {
        return down(maker, y, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private BakedQuad north(QuadMaker maker, float z11, float z12, float z22, float z21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.NORTH).tint(tint);
        maker.pos(p22, q22, z22).tex(u11, v11).end();
        maker.pos(p21, q21, z21).tex(u12, v12).end();
        maker.pos(p11, q11, z11).tex(u22, v22).end();
        maker.pos(p12, q12, z12).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad north(QuadMaker maker, float z11, float z12, float z22, float z21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.NORTH).tint(tint);
        maker.pos(p2, q2, z22).tex(u1, v1).end();
        maker.pos(p2, q1, z21).tex(u1, v2).end();
        maker.pos(p1, q1, z11).tex(u2, v2).end();
        maker.pos(p1, q2, z12).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad north(QuadMaker maker, float z, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return north(maker, z, z, z, z, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad north(QuadMaker maker, float z, float p1, float q1, float p2, float q2) {
        return north(maker, z, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private BakedQuad south(QuadMaker maker, float z11, float z12, float z22, float z21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.SOUTH).tint(tint);
        maker.pos(p12, q12, z12).tex(u11, v11).end();
        maker.pos(p11, q11, z11).tex(u12, v12).end();
        maker.pos(p21, q21, z21).tex(u22, v22).end();
        maker.pos(p22, q22, z22).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad south(QuadMaker maker, float z11, float z12, float z22, float z21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.SOUTH).tint(tint);
        maker.pos(p1, q2, z12).tex(u1, v1).end();
        maker.pos(p1, q1, z11).tex(u1, v2).end();
        maker.pos(p2, q1, z21).tex(u2, v2).end();
        maker.pos(p2, q2, z22).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad south(QuadMaker maker, float z, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return south(maker, z, z, z, z, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad south(QuadMaker maker, float z, float p1, float q1, float p2, float q2) {
        return south(maker, z, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private BakedQuad east(QuadMaker maker, float x11, float x12, float x22, float x21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.EAST).tint(tint);
        maker.pos(x22, q22, p22).tex(u11, v11).end();
        maker.pos(x21, q21, p21).tex(u12, v12).end();
        maker.pos(x11, q11, p11).tex(u22, v22).end();
        maker.pos(x12, q12, p12).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad east(QuadMaker maker, float x11, float x12, float x22, float x21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.EAST).tint(tint);
        maker.pos(x22, q2, p2).tex(u1, v1).end();
        maker.pos(x21, q1, p2).tex(u1, v2).end();
        maker.pos(x11, q1, p1).tex(u2, v2).end();
        maker.pos(x12, q2, p1).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad east(QuadMaker maker, float x, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return east(maker, x, x, x, x, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad east(QuadMaker maker, float x, float p1, float q1, float p2, float q2) {
        return east(maker, x, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private BakedQuad west(QuadMaker maker, float x11, float x12, float x22, float x21, float p11, float q11, float p12, float q12, float p22, float q22, float p21, float q21, float u11, float v11, float u12, float v12, float u22, float v22, float u21, float v21) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.WEST).tint(tint);
        maker.pos(x12, q12, p12).tex(u11, v11).end();
        maker.pos(x11, q11, p11).tex(u12, v12).end();
        maker.pos(x21, q21, p21).tex(u22, v22).end();
        maker.pos(x22, q22, p22).tex(u21, v21).end();
        return maker.make();
    }

    private BakedQuad west(QuadMaker maker, float x11, float x12, float x22, float x21, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        maker.start(transform, texture, false);
        maker.orientation(Direction.WEST).tint(tint);
        maker.pos(x12, q2, p1).tex(u1, v1).end();
        maker.pos(x11, q1, p1).tex(u1, v2).end();
        maker.pos(x21, q1, p2).tex(u2, v2).end();
        maker.pos(x22, q2, p2).tex(u2, v1).end();
        return maker.make();
    }

    private BakedQuad west(QuadMaker maker, float x, float p1, float q1, float p2, float q2, float u1, float v1, float u2, float v2) {
        return west(maker, x, x, x, x, p1, q1, p2, q2, u1, v1, u2, v2);
    }

    private BakedQuad west(QuadMaker maker, float x, float p1, float q1, float p2, float q2) {
        return west(maker, x, p1, q1, p2, q2, p1, q1, p2, q2);
    }

    private void add(Predicate<IModelData> pred, BakedQuad... quads) {
        this.quads.add(new QuadDef(pred, quads));
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if(side != null) return ImmutableList.of();
        if(state != null && !(state.getBlock() instanceof BushBlock)) {
            return ImmutableList.of();
        }

        ArrayList<BakedQuad> out = new ArrayList<>();
        for(QuadDef def : quads) {
            def.addQuads(extraData, out::add);
        }
        return out;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return ao;
    }

    @Override
    public boolean isGui3d() {
        return gui3d;
    }

    @Override
    public boolean func_230044_c_() {
        return sideLit;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particles;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack mat) {
        return PerspectiveMapWrapper.handlePerspective(this, transforms, type, mat);
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        ModelDataMap data = BushModelProperties.createData();

        if(state.getBlock() instanceof BushBlock) {
            ((BushBlock) state.getBlock()).fillModelData(world, pos, state, data);
        }

        return data;
    }

    private static class QuadDef {
        private final BakedQuad[] quads;
        private final Predicate<IModelData> pred;

        QuadDef(Predicate<IModelData> pred, BakedQuad... quads) {
            this.quads = quads;
            this.pred = pred;
        }

        void addQuads(IModelData data, Consumer<BakedQuad> quadConsumer) {
            if(pred.test(data)) {
                for(BakedQuad quad : quads) {
                    quadConsumer.accept(quad);
                }
            }
        }
    }

    private static class Metrics {
        final float rounding;
        final float uvRatio;

        final float sideLower;
        final float sideUpper;
        final float bevelPLower;
        final float bevelPUpper;
        final float bevelTLower;
        final float bevelTUpper;

        final float bevelInset;
        final float cornerInset;

        final float cornerPLower;
        final float cornerPUpper;

        final float cornerTLower;
        final float cornerTUpper;

        Metrics(float rounding, float uvRatio) {
            rounding /= 16;
            this.rounding = rounding;
            this.uvRatio = uvRatio;

            sideLower = rounding;
            sideUpper = 1 - rounding;

            bevelInset = rounding / 2;
            cornerInset = rounding * 2 / 3;

            cornerPLower = cornerInset;
            cornerPUpper = 1 - cornerInset;

            bevelPLower = rounding / 2;
            bevelPUpper = 1 - rounding / 2;

            bevelTLower = rounding * uvRatio;
            bevelTUpper = 1 - rounding * uvRatio;

            cornerTLower = rounding * uvRatio / 1.5F * 2;
            cornerTUpper = 1 - rounding * uvRatio / 1.5F * 2;
        }
    }

    private static class SideSelector implements Predicate<IModelData> {

        private final ModelProperty<Boolean> property;

        private SideSelector(ModelProperty<Boolean> property) {
            this.property = property;
        }

        private static boolean getValue(IModelData state, ModelProperty<Boolean> bool) {
            return state.hasProperty(bool) && nonnull(state.getData(bool));
        }

        private static boolean nonnull(Boolean value) {
            return value != null && value;
        }

        @Override
        public boolean test(IModelData state) {
            return getValue(state, property);
        }
    }
}
