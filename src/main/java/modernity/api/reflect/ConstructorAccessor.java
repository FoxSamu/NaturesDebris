package modernity.api.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings( "unchecked" )
public class ConstructorAccessor<T, R> {
    private final Constructor constructor;

    public ConstructorAccessor( Constructor constructor ) {
        this.constructor = constructor;
        constructor.setAccessible( true );
    }

    public ConstructorAccessor( Class<? super T> cls, String name, Class<?>... params ) {
        this.constructor = ObfuscationReflectionHelper.findConstructor( cls, params );
        constructor.setAccessible( true );
    }

    public R create( T t, Object... values ) {
        try {
            return (R) constructor.newInstance( t, values );
        } catch( IllegalAccessException | InvocationTargetException | InstantiationException e ) {
            throw new RuntimeException( e );
        }
    }

    public Constructor getConstructor() {
        return constructor;
    }

    public Manager forInstance( T inst ) {
        return new Manager( inst );
    }

    public Manager forStatic() {
        return new Manager( null );
    }

    public class Manager {
        private final T instance;

        private Manager( T instance ) {
            this.instance = instance;
        }

        public R create( Object... values ) {
            return ConstructorAccessor.this.create( instance, values );
        }
    }
}
