package modernity.api.reflect;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings( "unchecked" )
public class MethodAccessor<T, R> {
    private final Method method;

    public MethodAccessor( Method method ) {
        this.method = method;
        method.setAccessible( true );
    }

    public MethodAccessor( Class<? super T> cls, String name, Class<?>... params ) {
        this.method = ObfuscationReflectionHelper.findMethod( cls, name, params );
        method.setAccessible( true );
    }

    public R call( T t, Object... values ) {
        try {
            return (R) method.invoke( t, values );
        } catch( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException( e );
        }
    }

    public Method getMethod() {
        return method;
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

        public R call( Object... values ) {
            return MethodAccessor.this.call( instance, values );
        }
    }
}
