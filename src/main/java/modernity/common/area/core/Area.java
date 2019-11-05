package modernity.common.area.core;

import modernity.common.registry.MDRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

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
        AreaBox box = new AreaBox( nbt, "box" );
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
