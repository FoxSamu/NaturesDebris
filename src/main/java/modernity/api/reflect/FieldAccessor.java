package modernity.api.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.function.Supplier;

@SuppressWarnings( "unchecked" )
public class FieldAccessor<T, R> {
    private final Field field;

    public FieldAccessor( Field field ) {
        this.field = field;
        field.setAccessible( true );
    }

    public FieldAccessor( Class<? super T> cls, String name ) {
        this.field = ObfuscationReflectionHelper.findField( cls, name );
        field.setAccessible( true );
    }

    public R get( T t ) {
        try {
            return (R) field.get( t );
        } catch( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    public void set( T t, R value ) {
        try {
            field.set( t, value );
        } catch( IllegalAccessException e ) {
            throw new RuntimeException( e );
        }
    }

    public Manager forInstance( T inst ) {
        return new Manager( inst );
    }

    public Manager forStatic() {
        return new Manager( null );
    }

    public Field getField() {
        return field;
    }

    public class Manager implements Supplier<R> {
        private final T instance;

        private Manager( T instance ) {
            this.instance = instance;
        }

        @Override
        public R get() {
            return FieldAccessor.this.get( instance );
        }

        public Manager set( R value ) {
            FieldAccessor.this.set( instance, value );
            return this;
        }
    }
}
