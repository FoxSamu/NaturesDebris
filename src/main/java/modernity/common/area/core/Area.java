package modernity.common.area.core;

import modernity.common.registry.MDRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public abstract class Area {
    protected final AreaType type;
    protected final World world;
    protected final AreaBox box;
    private long referenceID;

    public Area( AreaType type, World world, AreaBox box ) {
        this.type = type;
        this.world = world;
        this.box = box;
    }

    public final AreaType getType() {
        return type;
    }

    public final AreaBox getBox() {
        return box;
    }

    public final int getRegionX() {
        return (int) ( referenceID >>> 48 & 0xffff );
    }

    public final int getRegionZ() {
        return (int) ( referenceID >>> 32 & 0xffff );
    }

    public final int getRegionLocalKey() {
        return (int) ( referenceID & 0xffffffffL );
    }

    public final long getReferenceID() {
        return referenceID;
    }

    final void setReferenceID( long refID ) {
        referenceID = refID;
    }

    public void write( CompoundNBT nbt, SerializeType type ) {
    }

    public void read( CompoundNBT nbt, SerializeType type ) {
    }

    public boolean isInside( int x, int y, int z ) {
        return box.contains( x, y, z );
    }

    public boolean isInside( double x, double y, double z ) {
        return box.contains( x, y, z );
    }

    public boolean isInside( float x, float y, float z ) {
        return box.contains( x, y, z );
    }

    public boolean isInside( Vec3i vec ) {
        return box.contains( vec );
    }

    public boolean isInside( Vec3d vec ) {
        return box.contains( vec );
    }

    public boolean isInside( Entity e ) {
        return box.contains( e );
    }

    public boolean intersects( AreaBox box ) {
        return getBox().intersects( box );
    }

    public boolean intersects( AxisAlignedBB box ) {
        return getBox().intersects( box );
    }

    public boolean intersects( MutableBoundingBox box ) {
        return getBox().intersects( box );
    }

    public boolean intersects( Area area ) {
        return getBox().intersects( area.getBox() );
    }

    public BlockState getBlockState( BlockPos pos ) {
        return world.getBlockState( getBox().getGlobal( pos ) );
    }

    public IFluidState getFluidState( BlockPos pos ) {
        return world.getFluidState( getBox().getGlobal( pos ) );
    }

    public boolean setBlockState( BlockPos pos, BlockState state ) {
        return world.setBlockState( getBox().getGlobal( pos ), state );
    }

    public boolean setBlockState( BlockPos pos, BlockState state, int flags ) {
        return world.setBlockState( getBox().getGlobal( pos ), state, flags );
    }

    public TileEntity getTileEntity( BlockPos pos ) {
        return world.getTileEntity( pos );
    }

    public List<Entity> getEntitiesInside() {
        return getEntitiesInside( entity -> true );
    }

    public List<Entity> getEntitiesInside( Predicate<Entity> predicate ) {
        return world.getEntitiesInAABBexcluding( null, getBox().toAABB(), predicate );
    }

    @Override
    public boolean equals( Object obj ) {
        return obj instanceof Area && ( (Area) obj ).referenceID == referenceID;
    }

    @Override
    public int hashCode() {
        return Long.hashCode( referenceID );
    }

    public static Area deserialize( CompoundNBT nbt, long refID, World world, SerializeType serializeType ) {
        ResourceLocation id = new ResourceLocation( nbt.getString( "id" ) );
        AreaType type = MDRegistries.AREA_TYPES.getValue( id );
        if( type == null ) return null;
        AreaBox box = AreaBox.deserialize( nbt, "box" );
        Area area = type.create( world, box );
        area.setReferenceID( refID );
        area.read( nbt, serializeType );
        return area;
    }

    public static CompoundNBT serialize( Area area, SerializeType serializeType ) {
        CompoundNBT nbt = new CompoundNBT();
        area.write( nbt, serializeType );
        ResourceLocation id = MDRegistries.AREA_TYPES.getKey( area.getType() );
        if( id == null ) throw new RuntimeException( "Trying to save unregistered area type" );
        nbt.putString( "id", id.toString() );
        area.getBox().serialize( nbt, "box" );
        return nbt;
    }

    public enum SerializeType {
        FILESYSTEM,
        NETWORK
    }
}
