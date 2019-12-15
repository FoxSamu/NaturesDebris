/*
 * Copyright (c) 2019 RedGalaxy & co.
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 6 - 15 - 2019
 */

package modernity.api.util;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.EnhancedRuntimeException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;


// Copied from forge 1.12.2
public class EnumHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Object reflectionFactory;
    private static Method newConstructorAccessor;
    private static Method newInstance;
    private static Method newFieldAccessor;
    private static Method fieldAccessorSet;
    private static boolean isSetup;

    private static void setup() {
        if( isSetup ) {
            return;
        }

        try {
            Method getReflectionFactory = Class.forName( "sun.reflect.ReflectionFactory" ).getDeclaredMethod( "getReflectionFactory" );
            reflectionFactory = getReflectionFactory.invoke( null );
            newConstructorAccessor = Class.forName( "sun.reflect.ReflectionFactory" ).getDeclaredMethod( "newConstructorAccessor", Constructor.class );
            newInstance = Class.forName( "sun.reflect.ConstructorAccessor" ).getDeclaredMethod( "newInstance", Object[].class );
            newFieldAccessor = Class.forName( "sun.reflect.ReflectionFactory" ).getDeclaredMethod( "newFieldAccessor", Field.class, boolean.class );
            fieldAccessorSet = Class.forName( "sun.reflect.FieldAccessor" ).getDeclaredMethod( "set", Object.class, Object.class );
        } catch( Exception e ) {
            LOGGER.error( "Error setting up EnumHelper", e );
        }

        isSetup = true;
    }

    private static Object getConstructorAccessor( Class<?> enumClass, Class<?>[] additionalParameterTypes ) throws Exception {
        Class<?>[] parameterTypes = new Class[ additionalParameterTypes.length + 2 ];
        parameterTypes[ 0 ] = String.class;
        parameterTypes[ 1 ] = int.class;
        System.arraycopy( additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length );
        return newConstructorAccessor.invoke( reflectionFactory, enumClass.getDeclaredConstructor( parameterTypes ) );
    }

    private static <T extends Enum<?>> T makeEnum( Class<T> enumClass, @Nullable String value, int ordinal, Class<?>[] additionalTypes, @Nullable Object[] additionalValues ) throws Exception {
        int additionalParamsCount = additionalValues == null ? 0 : additionalValues.length;
        Object[] params = new Object[ additionalParamsCount + 2 ];
        params[ 0 ] = value;
        params[ 1 ] = ordinal;
        if( additionalValues != null ) {
            System.arraycopy( additionalValues, 0, params, 2, additionalValues.length );
        }
        return enumClass.cast( newInstance.invoke( getConstructorAccessor( enumClass, additionalTypes ), new Object[] {
                params
        } ) );
    }

    public static void setFailsafeFieldValue( Field field, @Nullable Object target, @Nullable Object value ) throws Exception {
        field.setAccessible( true );
        Field modifiersField = Field.class.getDeclaredField( "modifiers" );
        modifiersField.setAccessible( true );
        modifiersField.setInt( field, field.getModifiers() & ~ Modifier.FINAL );
        Object fieldAccessor = newFieldAccessor.invoke( reflectionFactory, field, false );
        fieldAccessorSet.invoke( fieldAccessor, target, value );
    }

    private static void blankField( Class<?> enumClass, String fieldName ) throws Exception {
        for( Field field : Class.class.getDeclaredFields() ) {
            if( field.getName().contains( fieldName ) ) {
                field.setAccessible( true );
                setFailsafeFieldValue( field, enumClass, null );
                break;
            }
        }
    }

    private static void cleanEnumCache( Class<?> enumClass ) throws Exception {
        blankField( enumClass, "enumConstantDirectory" );
        blankField( enumClass, "enumConstants" );
    }

    // Tests an enum is compatible with these args, throws an error if not.
    public static void testEnum( Class<? extends Enum<?>> enumType, Class<?>[] paramTypes ) {
        addEnum( true, enumType, null, paramTypes, null );
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum( Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues ) {
        return addEnum( false, enumType, enumName, paramTypes, paramValues );
    }

    @SuppressWarnings( { "unchecked", "serial" } )
    @Nullable
    private static <T extends Enum<?>> T addEnum( boolean test, Class<T> enumType, @Nullable String enumName, Class<?>[] paramTypes, @Nullable Object[] paramValues ) {
        if( ! isSetup ) {
            setup();
        }

        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();

        for( Field field : fields ) {
            String name = field.getName();
            if( name.equals( "$VALUES" ) || name.equals( "ENUM$VALUES" ) ) // Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
            {
                valuesField = field;
                break;
            }
        }

        int flags = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL | 0x1000 /* SYNTHETIC */;
        if( valuesField == null ) {
            String valueType = String.format( "[L%s;", enumType.getName().replace( '.', '/' ) );

            for( Field field : fields ) {
                if( ( field.getModifiers() & flags ) == flags &&
                        field.getType().getName().replace( '.', '/' ).equals( valueType ) ) // Apparently some JVMs return .'s and some don't..
                {
                    valuesField = field;
                    break;
                }
            }
        }

        if( valuesField == null ) {
            List<String> lines = Lists.newArrayList();
            lines.add( String.format( "Could not find $VALUES field for enum: %s", enumType.getName() ) );
            lines.add( String.format( "Flags: %s", String.format( "%16s", Integer.toBinaryString( flags ) ).replace( ' ', '0' ) ) );
            lines.add( "Fields:" );
            for( Field field : fields ) {
                String mods = String.format( "%16s", Integer.toBinaryString( field.getModifiers() ) ).replace( ' ', '0' );
                lines.add( String.format( "       %s %s: %s", mods, field.getName(), field.getType().getName() ) );
            }

            for( String line : lines )
                LOGGER.fatal( line );

            if( test ) {
                throw new EnhancedRuntimeException( "Could not find $VALUES field for enum: " + enumType.getName() ) {
                    @Override
                    protected void printStackTrace( WrappedPrintStream stream ) {
                        for( String line : lines )
                            stream.println( line );
                    }
                };
            }
            return null;
        }

        if( test ) {
            Object ctr = null;
            Exception ex = null;
            try {
                ctr = getConstructorAccessor( enumType, paramTypes );
            } catch( Exception e ) {
                ex = e;
            }
            if( ctr == null ) {
                throw new EnhancedRuntimeException( String.format( "Could not find constructor for Enum %s", enumType.getName() ), ex ) {
                    private String toString( Class<?>[] cls ) {
                        StringBuilder b = new StringBuilder();
                        for( int x = 0; x < cls.length; x++ ) {
                            b.append( cls[ x ].getName() );
                            if( x != cls.length - 1 )
                                b.append( ", " );
                        }
                        return b.toString();
                    }

                    @Override
                    protected void printStackTrace( WrappedPrintStream stream ) {
                        stream.println( "Target Arguments:" );
                        stream.println( "    java.lang.String, int, " + toString( paramTypes ) );
                        stream.println( "Found Constructors:" );
                        for( Constructor<?> ctr : enumType.getDeclaredConstructors() ) {
                            stream.println( "    " + toString( ctr.getParameterTypes() ) );
                        }
                    }
                };
            }
            return null;
        }

        valuesField.setAccessible( true );

        try {
            T[] previousValues = (T[]) valuesField.get( enumType );
            T newValue = makeEnum( enumType, enumName, previousValues.length, paramTypes, paramValues );
            setFailsafeFieldValue( valuesField, null, ArrayUtils.add( previousValues, newValue ) );
            cleanEnumCache( enumType );

            return newValue;
        } catch( Exception e ) {
            LOGGER.error( "Error adding enum with EnumHelper", e );
            throw new RuntimeException( e );
        }
    }

    static {
        if( ! isSetup ) {
            setup();
        }
    }
}