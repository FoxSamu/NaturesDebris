/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.generator;

import modernity.common.generator.decorate.IDecorationHandler;
import modernity.common.generator.map.IMapGenData;
import modernity.common.generator.map.MapGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.WorldGenRegion;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ProceduralGenSettings<D extends IMapGenData> extends GenerationSettings {
    private ThreadLocal<D> dataLocal;

    private final ArrayList<MapGenerator<? super D>> generators = new ArrayList<>();
    private final ArrayList<IDecorationHandler> decorators = new ArrayList<>();

    private ProceduralChunkGenerator generator;

    public ProceduralGenSettings() {
    }

    public ProceduralGenSettings<D> dataSupplier( Supplier<? extends D> dataSupplier ) {
        this.dataLocal = ThreadLocal.withInitial( dataSupplier );
        return this;
    }

    public ProceduralGenSettings<D> addGenerator( MapGenerator<? super D> gen ) {
        generators.add( gen );
        return this;
    }

    public ProceduralGenSettings<D> addDecorator( IDecorationHandler decorator ) {
        decorators.add( decorator );
        return this;
    }

    public void makeBase( WorldGenRegion region ) {
        D data = dataLocal.get();
        data.init( generator );

        for( MapGenerator<? super D> gen : generators ) {
            gen.generate( region, data );
        }
    }

    void setGenerator( ProceduralChunkGenerator gen ) {
        if( generator != null ) throw new RuntimeException( "Generator already set!" );
        generator = gen;
    }

    public void decorate( WorldGenRegion region ) {
        for( IDecorationHandler decorator : decorators ) {
            decorator.decorate( region, generator );
        }
    }
}
