package modernity.common.block.state;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;

import java.util.Collection;

public class ExtendedBlockState implements IBlockState {
    private final IBlockState underlying;

    public ExtendedBlockState( IBlockState underlying ) {
        this.underlying = underlying;
    }

    @Override
    public Block getBlock() {
        return underlying.getBlock();
    }

    @Override
    public Collection<IProperty<?>> getProperties() {
        return underlying.getProperties();
    }

    @Override
    public <T extends Comparable<T>> boolean has( IProperty<T> property ) {
        return underlying.has( property );
    }

    @Override
    public <T extends Comparable<T>> T get( IProperty<T> property ) {
        return underlying.get( property );
    }

    @Override
    public <T extends Comparable<T>, V extends T> IBlockState with( IProperty<T> property, V value ) {
        return new ExtendedBlockState( underlying.with( property, value ) );
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycle( IProperty<T> property ) {
        return new ExtendedBlockState( underlying.cycle( property ) );
    }

    @Override
    public ImmutableMap<IProperty<?>, Comparable<?>> getValues() {
        return underlying.getValues();
    }
}
