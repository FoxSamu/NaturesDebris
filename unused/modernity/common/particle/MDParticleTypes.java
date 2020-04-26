/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 14 - 2020
 * Author: rgsw
 */

package modernity.common.particle;

import com.google.common.reflect.TypeToken;
import modernity.client.particle.ExtendedDiggingParticle;
import modernity.common.registry.RegistryEventHandler;
import modernity.common.registry.RegistryHandler;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Holder class for Modernity particle types.
 */
public final class MDParticleTypes {
    private static final RegistryHandler<ParticleType<?>> ENTRIES = new RegistryHandler<>( "modernity" );

    public static final BasicParticleType SALT = register( "salt", new BasicParticleType( false ) );
    public static final BasicParticleType FALLEN_LEAF = register( "fallen_leaf", new BasicParticleType( false ), "humus" );
    public static final ParticleType<RgbParticleData> FALLING_LEAF = register( "falling_leaf", new ParticleType<>( false, RgbParticleData.DESERIALIZER ) );
    public static final BasicParticleType RAIN = register( "rain", new BasicParticleType( false ) );
    public static final BasicParticleType HAIL = register( "hail", new BasicParticleType( false ) );
    public static final ParticleType<SoulLightParticleData> SOUL_LIGHT = register( "soul_light", new ParticleType<>( true, SoulLightParticleData.DESERIALIZER ) );
    public static final ParticleType<SoulLightParticleData> SOUL_LIGHT_CLOUD = register( "soul_light_cloud", new ParticleType<>( true, SoulLightParticleData.DESERIALIZER ) );
    public static final BasicParticleType SHADE = register( "shade", new BasicParticleType( false ) );
    public static final ParticleType<RgbParticleData> AMBIENT = register( "ambient", new ParticleType<>( false, RgbParticleData.DESERIALIZER ) );
    public static final BasicParticleType GOO_HANGING = register( "goo_hanging", new BasicParticleType( false ) );
    public static final BasicParticleType GOO_FALLING = register( "goo_falling", new BasicParticleType( false ) );
    public static final BasicParticleType GOO_LANDING = register( "goo_landing", new BasicParticleType( false ) );
    public static final BasicParticleType MOSS_DRIP_HANGING = register( "moss_drip_hanging", new BasicParticleType( false ) );
    public static final BasicParticleType MOSS_DRIP_FALLING = register( "moss_drip_falling", new BasicParticleType( false ) );

    private static <T extends ParticleType<?>> T register( String id, T type, String... aliases ) {
        return ENTRIES.register( id, type, aliases );
    }

    @SuppressWarnings( "unchecked" )
    public static void setup( RegistryEventHandler handler ) {
        TypeToken<ParticleType<?>> token = new TypeToken<ParticleType<?>>() {
        };
        handler.addHandler( (Class<ParticleType<?>>) token.getRawType(), ENTRIES );
    }

    @OnlyIn( Dist.CLIENT )
    public static void setupFactories( ParticleManager manager ) {
//        manager.registerFactory( SALT, SaltParticle.Factory::new );
//        manager.registerFactory( FALLEN_LEAF, LeafParticle.HumusFactory::new );
//        manager.registerFactory( FALLING_LEAF, LeafParticle.FallingLeafFactory::new );
//        manager.registerFactory( RAIN, PrecipitationParticle.DripFactory::new );
//        manager.registerFactory( HAIL, PrecipitationParticle.HailFactory::new );
//        manager.registerFactory( SOUL_LIGHT, new SoulLightParticle.BaseFactory() );
//        manager.registerFactory( SOUL_LIGHT_CLOUD, new SoulLightParticle.CloudFactory() );
//        manager.registerFactory( SHADE, ShadeParticle.Factory::new );
//        manager.registerFactory( AMBIENT, AmbientParticle.Factory::new );
//        manager.registerFactory( GOO_HANGING, DripParticle.Goo.HangingFactory::new );
//        manager.registerFactory( GOO_FALLING, DripParticle.Goo.FallingFactory::new );
//        manager.registerFactory( GOO_LANDING, DripParticle.Goo.LandingFactory::new );
//        manager.registerFactory( MOSS_DRIP_HANGING, DripParticle.HangingMossDrip.HangingFactory::new );
//        manager.registerFactory( MOSS_DRIP_FALLING, DripParticle.HangingMossDrip.FallingFactory::new );

        // Vanilla overrides
        manager.registerFactory( ParticleTypes.BLOCK, new ExtendedDiggingParticle.Factory() );
    }

    private MDParticleTypes() {
    }
}
