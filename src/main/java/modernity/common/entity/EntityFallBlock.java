/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 12 - 2019
 */

package modernity.common.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

import modernity.common.block.base.BlockFall;

import java.util.List;

public class EntityFallBlock extends Entity {
    private IBlockState fallTile = Blocks.SAND.getDefaultState();
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private Tag<Fluid> floatIn;
    private int fallHurtMax = 40;
    private float gravityScale = 1;

    private float fallHurtAmount = 2.0F;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey( EntityFallBlock.class, DataSerializers.BLOCK_POS );

    public EntityFallBlock( World world ) {
        super( MDEntityTypes.FALL_BLOCK, world );
    }

    public EntityFallBlock( World world, double x, double y, double z, IBlockState fallingBlockState ) {
        this( world );

        fallTile = fallingBlockState;
        preventEntitySpawning = true;

        setSize( 0.98F, 0.98F );
        setPosition( x, y + ( 1 - height ) / 2, z );

        motionX = 0;
        motionY = 0;
        motionZ = 0;

        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;

        setOrigin( new BlockPos( this ) );
    }

    public void setFloatIn( Tag<Fluid> floatIn ) {
        this.floatIn = floatIn;
    }

    public Tag<Fluid> getFloatIn() {
        return floatIn;
    }

    public void setGravityScale( float gravityScale ) {
        this.gravityScale = gravityScale;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }

    public void setOrigin( BlockPos origin ) {
        dataManager.set( ORIGIN, origin );
    }

    @OnlyIn( Dist.CLIENT )
    public BlockPos getOrigin() {
        return dataManager.get( ORIGIN );
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void registerData() {
        dataManager.register( ORIGIN, BlockPos.ORIGIN );
    }

    public boolean canBeCollidedWith() {
        return isAlive();
    }

    public void tick() {
        if( fallTile.isAir( world, new BlockPos( this ) ) ) {
            remove();
        } else {
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;

            Block fallBlock = fallTile.getBlock();

            if( fallTime++ == 0 ) {
                // Remove when starting to fall
                BlockPos currPos = new BlockPos( this );
                if( world.getBlockState( currPos ).getBlock() == fallBlock ) {
                    world.removeBlock( currPos );
                } else if( ! world.isRemote ) {
                    remove();
                    return;
                }
            }

            // Apply gravity
            if( ! hasNoGravity() ) {
                motionY -= 0.04 * gravityScale;
            }

            // Do movement
            this.move( MoverType.SELF, motionX, motionY, motionZ );

            if( ! this.world.isRemote ) {
                BlockPos currPos = new BlockPos( this );

                boolean isConcretePowder = fallTile.getBlock() instanceof BlockConcretePowder;
                boolean makeConcrete = isConcretePowder && world.getFluidState( currPos ).isTagged( FluidTags.WATER );
                boolean doesFloat = false;

                // Stop when colliding concrete powder with water
                double speedSq = motionX * motionX + motionY * motionY + motionZ * motionZ;
                if( isConcretePowder && speedSq > 1.0D ) {
                    RayTraceResult rtr = world.rayTraceBlocks( new Vec3d( prevPosX, prevPosY, prevPosZ ), new Vec3d( posX, posY, posZ ), RayTraceFluidMode.SOURCE_ONLY );

                    if( rtr != null && world.getFluidState( rtr.getBlockPos() ).isTagged( FluidTags.WATER ) ) {
                        currPos = rtr.getBlockPos();
                        makeConcrete = true;
                    }

                    if( floatIn != null && rtr != null && world.getFluidState( rtr.getBlockPos() ).isTagged( floatIn ) ) {
                        currPos = rtr.getBlockPos();
                        doesFloat = true;
                    }
                }

                boolean floatConcrete = doesFloat || makeConcrete;

                if( ! onGround && ! floatConcrete ) {
                    // We're falling
                    if( fallTime > 100 && ! world.isRemote && ( currPos.getY() < 1 || currPos.getY() > 256 ) || fallTime > 600 ) {
                        if( shouldDropItem && world.getGameRules().getBoolean( "doEntityDrops" ) ) {
                            entityDropItem( fallBlock );
                        }

                        remove();
                    }
                } else {
                    // We're on ground or floating in fluid: try to place our block again

                    IBlockState inState = world.getBlockState( currPos );
                    if( world.isAirBlock( new BlockPos( posX, posY - 0.01, posZ ) ) ) {
                        if( ! floatConcrete && BlockFalling.canFallThrough( world.getBlockState( new BlockPos( posX, posY - 0.01, posZ ) ) ) ) {
                            this.onGround = false;
                            return;
                        }
                    }

                    motionX *= 0.7;
                    motionZ *= 0.7;
                    motionY *= - 0.5;

                    if( inState.getBlock() != Blocks.MOVING_PISTON ) {
                        remove();
                        if( ! dontSetBlock ) {
                            boolean didPlace = inState.getMaterial().isReplaceable(); // Can we place here?
                            didPlace = didPlace && ( floatConcrete || ! BlockFall.canFallThrough( world.getBlockState( currPos.down() ) ) ); // Can't we continue falling?
                            didPlace = didPlace && ! ForgeEventFactory.onBlockPlace( // Could we actually place block
                                    this,
                                    new BlockSnapshot( getEntityWorld(), getPosition(), inState ),
                                    EnumFacing.UP
                            );
                            didPlace = didPlace && world.setBlockState( currPos, fallTile, 3 ); // Did we actually place block

                            if( didPlace ) {
                                // End falling
                                if( fallBlock instanceof BlockFall ) {
                                    ( (BlockFall) fallBlock ).onEndFalling( world, currPos, fallTile, inState );
                                }

                                // Restore tile entity
                                if( tileEntityData != null && fallTile.hasTileEntity() ) {
                                    TileEntity te = world.getTileEntity( currPos );
                                    if( te != null ) {
                                        NBTTagCompound nbt = te.write( new NBTTagCompound() );

                                        for( String s : tileEntityData.keySet() ) {
                                            INBTBase inbtbase = tileEntityData.get( s );
                                            if( ! "x".equals( s ) && ! "y".equals( s ) && ! "z".equals( s ) ) {
                                                nbt.put( s, inbtbase.copy() );
                                            }
                                        }

                                        te.read( nbt );
                                        te.markDirty();
                                    }
                                }
                            } else if( shouldDropItem && world.getGameRules().getBoolean( "doEntityDrops" ) ) {
                                entityDropItem( fallBlock );
                            }
                        } else if( fallBlock instanceof BlockFall ) {
                            ( (BlockFall) fallBlock ).onBroken( world, currPos );
                        }
                    }
                }
            }

            // Air friction
            this.motionX *= 0.98;
            this.motionY *= 0.98;
            this.motionZ *= 0.98;
        }
    }

    public void fall( float distance, float damageMultiplier ) {
        if( this.hurtEntities ) {
            int i = MathHelper.ceil( distance - 1.0F );
            if( i > 0 ) {
                List<Entity> list = Lists.newArrayList( world.getEntitiesWithinAABBExcludingEntity( this, getBoundingBox() ) );
                boolean flag = fallTile.isIn( BlockTags.ANVIL );
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

                for( Entity entity : list ) {
                    entity.attackEntityFrom( damagesource, (float) Math.min( MathHelper.floor( (float) i * fallHurtAmount ), fallHurtMax ) );
                }

                if( flag && rand.nextDouble() < 0.05 + i * 0.05D ) {
                    IBlockState state = BlockAnvil.damage( fallTile );
                    if( state == null ) {
                        dontSetBlock = true;
                    } else {
                        fallTile = state;
                    }
                }
            }
        }

    }

    protected void writeAdditional( NBTTagCompound compound ) {
        compound.put( "BlockState", NBTUtil.writeBlockState( fallTile ) );
        compound.putInt( "Time", fallTime );
        compound.putBoolean( "DropItem", shouldDropItem );
        compound.putBoolean( "HurtEntities", hurtEntities );
        compound.putFloat( "FallHurtAmount", fallHurtAmount );
        compound.putFloat( "GravityScale", gravityScale );
        compound.putString( "FloatIn", floatIn.getId().toString() );
        compound.putInt( "FallHurtMax", fallHurtMax );
        if( this.tileEntityData != null ) {
            compound.put( "TileEntityData", tileEntityData );
        }

    }

    protected void readAdditional( NBTTagCompound compound ) {
        fallTile = NBTUtil.readBlockState( compound.getCompound( "BlockState" ) );
        fallTime = compound.getInt( "Time" );
        if( compound.contains( "HurtEntities", 99 ) ) {
            hurtEntities = compound.getBoolean( "HurtEntities" );
            fallHurtAmount = compound.getFloat( "FallHurtAmount" );
            fallHurtMax = compound.getInt( "FallHurtMax" );
        } else if( this.fallTile.isIn( BlockTags.ANVIL ) ) {
            hurtEntities = true;
        }

        if( compound.contains( "DropItem", 99 ) ) {
            shouldDropItem = compound.getBoolean( "DropItem" );
        }

        if( compound.contains( "TileEntityData", 10 ) ) {
            tileEntityData = compound.getCompound( "TileEntityData" );
        }

        if( compound.contains( "GravityScale" ) ) {
            gravityScale = compound.getFloat( "GravityScale" );
        }

        if( compound.contains( "FloatIn" ) ) {
            MinecraftServer server = world.getServer();
            if( server != null ) {
                floatIn = server.getNetworkTagManager().getFluids().get( new ResourceLocation( compound.getString( "FloatIn" ) ) );
            }
        }

        if( fallTile.isAir() ) {
            fallTile = Blocks.SAND.getDefaultState();
        }

    }

    @OnlyIn( Dist.CLIENT )
    public World getWorldObj() {
        return world;
    }

    public void setHurtEntities( boolean hurt ) {
        hurtEntities = hurt;
    }

    @OnlyIn( Dist.CLIENT )
    public boolean canRenderOnFire() {
        return false;
    }

    public void fillCrashReport( CrashReportCategory category ) {
        super.fillCrashReport( category );
        category.addDetail( "Immitating BlockState", fallTile.toString() );
    }

    public IBlockState getFallingTile() {
        return fallTile;
    }

    public boolean ignoreItemEntityData() {
        return true;
    }
}