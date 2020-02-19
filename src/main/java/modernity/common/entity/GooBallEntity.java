/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   02 - 19 - 2020
 * Author: rgsw
 */

package modernity.common.entity;

import modernity.api.util.NBTUtil;
import modernity.common.Modernity;
import modernity.common.item.MDItems;
import modernity.common.net.SSpawnEntityPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class GooBallEntity extends ThrownItemEntity {
    private static final DataParameter<Integer> BOUNCES = EntityDataManager.createKey( GooBallEntity.class, DataSerializers.VARINT );
    private static final DataParameter<Integer> MAX_BOUNCES = EntityDataManager.createKey( GooBallEntity.class, DataSerializers.VARINT );
    private static final DataParameter<Boolean> HANGING = EntityDataManager.createKey( GooBallEntity.class, DataSerializers.BOOLEAN );

    private int hangingTime;

    private int bounces;
    private int maxBounces;

    private boolean poisonous;

    @SuppressWarnings( "unchecked" )
    public GooBallEntity( EntityType type, World world ) {
        super( type, world );
    }

    public GooBallEntity( double x, double y, double z, World world ) {
        super( MDEntityTypes.GOO_BALL, x, y, z, world );
        this.maxBounces = world.rand.nextInt( 3 ) + 2;
    }

    public GooBallEntity( LivingEntity ent, World world ) {
        super( MDEntityTypes.GOO_BALL, ent, world );
        this.maxBounces = world.rand.nextInt( 3 ) + 2;
    }

    public void setBounces( int bounces ) {
        dataManager.set( BOUNCES, bounces );
    }

    public int getBounces() {
        return dataManager.get( BOUNCES );
    }

    public int getMaxBounces() {
        return dataManager.get( MAX_BOUNCES );
    }

    private boolean bounce() {
        setBounces( getBounces() + 1 );
        return getBounces() < getMaxBounces();
    }

    private void unbounce() {
        setBounces( getBounces() - 1 );
    }

    public GooBallEntity setPoisonous() {
        poisonous = true;
        return this;
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register( BOUNCES, 0 );
        getDataManager().register( MAX_BOUNCES, rand.nextInt( 7 ) );
        getDataManager().register( HANGING, false );
    }

    @Override
    protected Item getThrownItem() {
        return poisonous ? MDItems.POISONOUS_GOO_BALL : MDItems.GOO_BALL;
    }

    @OnlyIn( Dist.CLIENT )
    private IParticleData getParticle() {
        ItemStack stack = getItemStack();
        if( ! stack.isEmpty() ) {
            return new ItemParticleData( ParticleTypes.ITEM, stack );
        } else {
            return new ItemParticleData( ParticleTypes.ITEM, new ItemStack( getThrownItem() ) );
        }
    }

    @Override
    @OnlyIn( Dist.CLIENT )
    public void handleStatusUpdate( byte id ) {
        if( id == 3 ) {
            IParticleData particle = getParticle();

            for( int i = 0; i < 8; ++ i ) {
                world.addParticle( particle, posX, posY, posZ, 0, 0, 0 );
            }
        }
        if( id == 4 ) {
            for( int i = 0; i < 16; ++ i ) {
                world.addParticle(
                    ParticleTypes.ENTITY_EFFECT,
                    posX + ( rand.nextDouble() - rand.nextDouble() ),
                    posY + ( rand.nextDouble() - rand.nextDouble() ),
                    posZ + ( rand.nextDouble() - rand.nextDouble() ),
                    0.7,
                    1,
                    0
                );
            }
        }
    }

    private double bounciness( double add ) {
        return add + rand.nextDouble() * 0.1 + 0.3;
    }

    @Override
    public void tick() {
        if( dataManager.get( HANGING ) ) {
            hangingTime--;
            if( ! world.isRemote ) {
                if( hangingTime < 0 ) {
                    world.setEntityState( this, (byte) 3 );
                    remove();

                    ItemStack stack = getItemStack();
                    if( ! stack.isEmpty() && stack.getItem() == MDItems.GOO_BALL ) {
                        world.addEntity( new ItemEntity( world, posX, posY, posZ, stack ) );
                    } else {
                        world.addEntity( new ItemEntity( world, posX, posY, posZ, new ItemStack( MDItems.GOO_BALL ) ) );
                    }
                }
            }

            setMotion( 0, - 0.005, 0 );
        }
        super.tick();


        if( world.isRemote && rand.nextInt( 3 ) == 0 ) {
            world.addParticle(
                ParticleTypes.ENTITY_EFFECT,
                posX + ( rand.nextDouble() - rand.nextDouble() ) * 0.1,
                posY + ( rand.nextDouble() - rand.nextDouble() ) * 0.1,
                posZ + ( rand.nextDouble() - rand.nextDouble() ) * 0.1,
                0.7,
                1,
                0
            );
        }
    }

    @Override
    protected void onImpact( RayTraceResult result ) {
        if( dataManager.get( HANGING ) ) {
            setMotion( Vec3d.ZERO );
            return;
        }
        if( result.getType() == RayTraceResult.Type.ENTITY ) {
            Entity entity = ( (EntityRayTraceResult) result ).getEntity();
            entity.attackEntityFrom( DamageSource.causeThrownDamage( this, getThrower() ), 0 );

            if( poisonous && ! world.isRemote ) {
                List<Entity> entities = world.getEntitiesInAABBexcluding( null, getBoundingBox().grow( 1 ), ent -> ent instanceof LivingEntity );
                for( Entity e : entities ) {
                    ( (LivingEntity) e ).addPotionEffect( new EffectInstance( Effects.POISON, 50 ) );
                }

                if( entity instanceof LivingEntity ) {
                    ( (LivingEntity) entity ).addPotionEffect( new EffectInstance( Effects.POISON, 50 ) );
                }
            }

            world.setEntityState( this, (byte) 4 );

            if( bounce() ) {
                Vec3d norm = result.getHitVec().subtract( entity.getPositionVec() );
                doBounce( norm, true );
            } else if( ! world.isRemote ) {
                world.setEntityState( this, (byte) 3 );
                remove();

                ItemStack stack = getItemStack();
                if( ! stack.isEmpty() ) {
                    world.addEntity( new ItemEntity( world, posX, posY, posZ, stack ) );
                } else {
                    world.addEntity( new ItemEntity( world, posX, posY, posZ, new ItemStack( getThrownItem() ) ) );
                }
            }
        } else if( result.getType() == RayTraceResult.Type.BLOCK ) {
            if( poisonous && ! world.isRemote ) {
                List<Entity> entities = world.getEntitiesInAABBexcluding( null, getBoundingBox().grow( 1 ), ent -> ent instanceof LivingEntity );
                for( Entity e : entities ) {
                    ( (LivingEntity) e ).addPotionEffect( new EffectInstance( Effects.POISON, 50 ) );
                }
            }

            world.setEntityState( this, (byte) 4 );

            if( bounce() ) {
                BlockRayTraceResult rtr = (BlockRayTraceResult) result;
                doBounce( new Vec3d( rtr.getFace().getDirectionVec() ), false );
            } else {
                if( ! world.isRemote ) {
                    dataManager.set( HANGING, true );
                    hangingTime = 100;
                    setMotion( Vec3d.ZERO );
                    posX = result.getHitVec().x;
                    posY = result.getHitVec().y;
                    posZ = result.getHitVec().z;
                }
            }
        }
    }

    private void doBounce( Vec3d norm, boolean removeY ) {
        if( removeY )
            norm = new Vec3d( norm.x, 0, norm.z ).normalize();

        Vec3d motion = getMotion();

        double velAlongNorm = norm.dotProduct( motion );
        if( velAlongNorm > 0 ) {
            unbounce();
            return;
        }

        Vec3d newMotion = motion.add( norm.scale( velAlongNorm * - bounciness( 1 ) ) );
        setMotion( newMotion );
    }

    @Override
    public void writeAdditional( CompoundNBT compound ) {
        super.writeAdditional( compound );
        compound.putInt( "bounces", bounces );
        compound.putInt( "maxBounces", maxBounces );
        compound.putInt( "hangingTime", hangingTime );
        compound.putBoolean( "poisonous", poisonous );
    }

    @Override
    public void readAdditional( CompoundNBT compound ) {
        super.readAdditional( compound );
        bounces = compound.getInt( "bounces" );
        maxBounces = compound.getInt( "maxBounces" );
        hangingTime = NBTUtil.getOrDefault( compound, "hangingTime", - 1 );
        poisonous = NBTUtil.getOrDefault( compound, "poisonous", false );

        dataManager.set( HANGING, hangingTime > 0 );
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return Modernity.network().toPlayClientPacket( new SSpawnEntityPacket( this, 0 ) );
    }
}
