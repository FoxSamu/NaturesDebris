/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   01 - 24 - 2020
 * Author: rgsw
 */

package modernity.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import modernity.common.container.inventory.ICleaningInventory;
import modernity.common.tileentity.CleanerTileEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Predicate;

public class CleaningRecipe implements IRecipe<ICleaningInventory> {
    private static final ItemStack ICON = ItemStack.EMPTY;
    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookTime;
    protected final int fluidAmount;
    protected final FluidMatcher requiredFluid;

    public CleaningRecipe( IRecipeType<?> type, ResourceLocation id, String group, Ingredient ingr, ItemStack result, float xp, int cookingTime, int fluidAmount, FluidMatcher requiredFluid ) {
        this.fluidAmount = fluidAmount;
        this.requiredFluid = requiredFluid;
        this.type = type;
        this.id = id;
        this.group = group;
        this.ingredient = ingr;
        this.result = result;
        this.experience = xp;
        this.cookTime = cookingTime;
    }

    @Override
    public ItemStack getIcon() {
        return ICON;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return MDRecipeSerializers.CLEANING;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    public FluidMatcher getRequiredFluid() {
        return requiredFluid;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches( ICleaningInventory inv, World world ) {
        return ingredient.test( inv.getStackInSlot( CleanerTileEntity.INPUT_INDEX ) )
                   && requiredFluid.test( inv.getFluid() )
                   && inv.getFluidAmount() >= fluidAmount;
    }

    @Override
    public ItemStack getCraftingResult( ICleaningInventory inv ) {
        return result.copy();
    }

    @Override
    public boolean canFit( int width, int height ) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingrs = NonNullList.create();
        ingrs.add( ingredient );
        return ingrs;
    }

    public float getExperience() {
        return experience;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @SuppressWarnings( "unchecked" )
    public enum FluidType {
        FLUID {
            @Override
            boolean valid( Fluid fluid, Object kind ) {
                return fluid == kind;
            }

            @Override
            void write( Object obj, PacketBuffer buf ) {
                Fluid fluid = (Fluid) obj;
                int id = ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getID( fluid );
                buf.writeInt( id );
            }

            @Override
            Object read( PacketBuffer buf ) {
                int id = buf.readInt();
                return ( (ForgeRegistry<Fluid>) ForgeRegistries.FLUIDS ).getValue( id );
            }

            @Override
            Collection<Fluid> getMatchingFluids( Object kind ) {
                return Collections.singletonList( (Fluid) kind );
            }
        },
        TAG {
            @Override
            boolean valid( Fluid fluid, Object kind ) {
                if( ! ( kind instanceof Tag ) ) return false;
                return fluid.isIn( (Tag<Fluid>) kind );
            }

            @Override
            void write( Object obj, PacketBuffer buf ) {
                String name = ( (Tag<Fluid>) obj ).getId() + "";
                buf.writeString( name );
            }

            @Override
            Object read( PacketBuffer buf ) {
                ResourceLocation loc = new ResourceLocation( buf.readString() );
                return FluidTags.getCollection().get( loc );
            }

            @Override
            Collection<Fluid> getMatchingFluids( Object kind ) {
                return ( (Tag<Fluid>) kind ).getAllElements();
            }
        },
        LIST {
            @Override
            boolean valid( Fluid fluid, Object kind ) {
                if( ! ( kind instanceof FluidMatcher[] ) ) return false;
                FluidMatcher[] matchers = (FluidMatcher[]) kind;

                for( FluidMatcher matcher : matchers ) {
                    if( matcher.test( fluid ) ) return true;
                }
                return false;
            }

            @Override
            void write( Object obj, PacketBuffer buf ) {
                FluidMatcher[] matchers = (FluidMatcher[]) obj;
                buf.writeShort( matchers.length );
                for( FluidMatcher matcher : matchers ) {
                    FluidMatcher.serialize( matcher, buf );
                }
            }

            @Override
            Object read( PacketBuffer buf ) {
                short count = buf.readShort();
                FluidMatcher[] matchers = new FluidMatcher[ count ];
                for( int i = 0; i < count; i++ ) {
                    matchers[ i ] = FluidMatcher.deserialize( buf );
                }
                return matchers;
            }

            @Override
            Collection<Fluid> getMatchingFluids( Object kind ) {
                FluidMatcher[] matchers = (FluidMatcher[]) kind;
                HashSet<Fluid> fluids = new HashSet<>();
                for( FluidMatcher matcher : matchers ) {
                    fluids.addAll( matcher.getMatchingFluids() );
                }
                return fluids;
            }
        };

        abstract boolean valid( Fluid fluid, Object kind );
        abstract void write( Object obj, PacketBuffer buf );
        abstract Object read( PacketBuffer buf );
        abstract Collection<Fluid> getMatchingFluids( Object kind );
    }

    public static class FluidMatcher implements Predicate<Fluid> {
        private final FluidType type;
        private final Object kind;

        private FluidMatcher( FluidType type, Object kind ) {
            this.type = type;
            this.kind = kind;
        }

        @Override
        public boolean test( Fluid fluid ) {
            if( fluid == null ) return false;
            return type.valid( fluid, kind );
        }

        public Collection<Fluid> getMatchingFluids() {
            return type.getMatchingFluids( kind );
        }

        public static void serialize( FluidMatcher matcher, PacketBuffer buf ) {
            buf.writeByte( matcher.type.ordinal() );
            matcher.type.write( matcher.kind, buf );
        }

        public static FluidMatcher deserialize( PacketBuffer buf ) {
            byte id = buf.readByte();
            FluidType type = FluidType.values()[ id ];
            Object kind = type.read( buf );
            return new FluidMatcher( type, kind );
        }

        public static FluidMatcher deserialize( JsonElement element ) {
            if( element.isJsonObject() || element.isJsonNull() ) {
                throw new JsonSyntaxException( "Fluid predicate must be array or string" );
            } else if( element.isJsonPrimitive() ) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                if( ! primitive.isString() ) {
                    throw new JsonSyntaxException( "Fluid predicate must be array or string" );
                }
                String str = primitive.getAsString();
                if( str.startsWith( "#" ) ) {
                    ResourceLocation tagID = new ResourceLocation( str.substring( 1 ) );
                    Tag<Fluid> tag = FluidTags.getCollection().get( tagID );
                    if( tag == null ) {
                        throw new JsonSyntaxException( "Unknown tag: '#" + tagID + "'" );
                    }
                    return tag( tag );
                } else {
                    ResourceLocation fluidID = new ResourceLocation( str );
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue( fluidID );
                    if( fluid == null ) {
                        throw new JsonSyntaxException( "Unknown fluid: '" + fluidID + "'" );
                    }
                    return fluid( fluid );
                }
            } else {
                JsonArray array = element.getAsJsonArray();
                if( array.size() == 0 ) {
                    throw new JsonSyntaxException( "Fluid matcher cannot be empty" );
                }
                FluidMatcher[] matchers = new FluidMatcher[ array.size() ];
                for( int i = 0; i < matchers.length; i++ ) {
                    matchers[ i ] = deserialize( array.get( i ) );
                }
                return new FluidMatcher( FluidType.LIST, matchers );
            }
        }

        public static FluidMatcher fluid( Fluid fluid ) {
            return new FluidMatcher( FluidType.FLUID, fluid );
        }

        public static FluidMatcher tag( Tag<Fluid> tag ) {
            return new FluidMatcher( FluidType.TAG, tag );
        }

        public static FluidMatcher list( FluidMatcher... list ) {
            return new FluidMatcher( FluidType.LIST, list );
        }
    }
}
