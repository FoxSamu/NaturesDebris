//package modernity.api;
//
//import net.minecraftforge.eventbus.api.BusBuilder;
//import net.minecraftforge.eventbus.api.IEventBus;
//
//import java.lang.reflect.Field;
//
//final class ModernityHolder {
//    static final IEventBus EVENT_BUS = BusBuilder.builder().startShutdown().build();
//    static IModernity modernity;
//    private static boolean found;
//
//    private ModernityHolder() {
//    }
//
//    static IModernity get() {
//        if( ! found ) {
//            try {
//                Class<?> cls = Class.forName( "modernity.ModernityBootstrap" );
//                Field field = cls.getField( "MODERNITY" );
//                modernity = (IModernity) field.get( null );
//            } catch( Exception exc ) {
//            }
//            found = true;
//        }
//        return modernity;
//    }
//}
