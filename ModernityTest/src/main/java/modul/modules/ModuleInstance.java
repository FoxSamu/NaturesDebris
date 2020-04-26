/*
 * Copyright (c) 2020 RedGalaxy
 * All rights reserved. Do not distribute.
 *
 * Date:   03 - 23 - 2020
 * Author: rgsw
 */

package modul.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ModuleInstance {
    private final Module module;
    private final Class<?> moduleClass;
    private final Object instance;
    private final HashMap<String, List<Consumer<ModuleEvent>>> listeners = Maps.newHashMap();

    ModuleInstance( Module module, Object instance ) {
        this.module = module;
        this.instance = instance;

        Class<?> cls = instance.getClass();
        addListeners( cls );

        moduleClass = cls;
    }

    public Module getModule() {
        return module;
    }

    public Object getInstance() {
        return instance;
    }

    public void invokeEvent( String type, ModuleEvent event ) {
        List<Consumer<ModuleEvent>> list = listeners.get( type );
        if( list != null ) {
            list.forEach( consumer -> consumer.accept( event ) );
        }
    }

    private void addListeners( Class<?> cls ) {
        Method[] methods = cls.getMethods();
        for( Method method : methods ) {

            // Method must be public and not-static
            int mods = method.getModifiers();
            if( Modifier.isPublic( mods ) && ! Modifier.isStatic( mods ) ) {

                // Method must have @ModuleListener
                if( method.isAnnotationPresent( ModuleListener.class ) ) {

                    Parameter[] params = method.getParameters();
                    if( params.length == 1 && ModuleEvent.class.isAssignableFrom( params[ 0 ].getType() ) ) {
                        // Method wants event instance
                        Class<?> type = params[ 0 ].getType();
                        ModuleListener annotation = method.getAnnotation( ModuleListener.class );
                        String[] events = annotation.value();
                        Consumer<ModuleEvent> handler = event -> {
                            if( event.getClass().isAssignableFrom( type ) ) {
                                try {
                                    method.invoke( instance, event );
                                } catch( IllegalAccessException | InvocationTargetException ignored ) {
                                }
                            }
                        };
                        for( String ev : events ) {
                            listeners.computeIfAbsent( ev, key -> Lists.newArrayList() )
                                     .add( handler );
                        }
                    } else if( params.length == 0 ) {
                        // Method does not want event instance
                        ModuleListener annotation = method.getAnnotation( ModuleListener.class );
                        String[] events = annotation.value();
                        Consumer<ModuleEvent> handler = event -> {
                            try {
                                method.invoke( instance );
                            } catch( IllegalAccessException | InvocationTargetException ignored ) {
                            }
                        };
                        for( String ev : events ) {
                            listeners.computeIfAbsent( ev, key -> new ArrayList<>() )
                                     .add( handler );
                        }
                    }
                }
            }
        }

        // Add listeners from superclass methods
        Class<?> sup = cls.getSuperclass();
        if( sup != null ) addListeners( sup );

        // Add listeners from interface methods
        for( Class<?> ifc : cls.getInterfaces() ) {
            addListeners( ifc );
        }
    }

    public boolean hasAnnotation( Class<? extends Annotation> annotation ) {
        return moduleClass.isAnnotationPresent( annotation );
    }

    public <A extends Annotation> A getAnnotation( Class<? extends A> annotation ) {
        return moduleClass.getAnnotation( annotation );
    }
}
