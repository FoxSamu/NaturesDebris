package modernity.common.area.core;

import modernity.common.registry.MDRegistries;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
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

    public void write( CompoundNBT nbt ) {
    }

    public void read( CompoundNBT nbt ) {
    }

    public static Area deserialize( CompoundNBT nbt, long refID, World world ) {
        ResourceLocation id = new ResourceLocation( nbt.getString( "id" ) );
        AreaType type = MDRegistries.AREA_TYPES.getValue( id );
        if( type == null ) return null;
        AreaBox box = new AreaBox( nbt, "box" );
        Area area = type.create( world, box );
        area.setReferenceID( refID );
        area.read( nbt );
        return area;
    }

    public static CompoundNBT serialize( Area area ) {
        CompoundNBT nbt = new CompoundNBT();
        area.write( nbt );
        ResourceLocation id = MDRegistries.AREA_TYPES.getKey( area.getType() );
        if( id == null ) throw new RuntimeException( "Trying to save unregistered area type" );
        nbt.putString( "id", id.toString() );
        area.getBox().serialize( nbt, "box" );
        return nbt;
    }
}
