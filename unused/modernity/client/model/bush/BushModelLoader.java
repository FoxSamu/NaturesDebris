package modernity.client.model.bush;

import com.google.gson.*;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.client.model.IModelLoader;

public class BushModelLoader implements IModelLoader<BushModelGeometry> {
    private static final float ACCURATE_UV_RATIO = 0.2928932188F;    // 1-sqrt(2)/2
    private static final float APPROXIMATE_UV_RATIO = 0.3333333333F; // 1/3

    @Override
    public void onResourceManagerReload( IResourceManager resourceManager ) {
    }

    @Override
    public BushModelGeometry read( JsonDeserializationContext deserializationContext, JsonObject obj ) {
        float round = JSONUtils.getFloat( obj, "rounding", 3 );
        int tint = JSONUtils.getInt( obj, "tintindex", - 1 );
        float ratio = APPROXIMATE_UV_RATIO;
        if( obj.has( "bevelUVRatio" ) ) ratio = ratio( obj.get( "bevelUVRatio" ) );

        // TODO Ambient Occlusion Parameter
        return new BushModelGeometry( tint, round, ratio );
    }

    private float ratio( JsonElement element ) {
        if( element.isJsonPrimitive() ) {
            if( element.getAsJsonPrimitive().isString() ) {
                String ratio = element.getAsString();
                if( ! ratio.equalsIgnoreCase( "approximate" ) && ! ratio.equalsIgnoreCase( "accurate" ) ) {
                    throw new JsonSyntaxException( "String 'uvRatio' must be 'approximate' or 'accurate': it can't be '" + ratio + "'" );
                }
                return ratio.equalsIgnoreCase( "accurate" ) ? ACCURATE_UV_RATIO : APPROXIMATE_UV_RATIO;
            } else if( element.getAsJsonPrimitive().isNumber() ) {
                return element.getAsFloat();
            } else {
                throw new JsonSyntaxException( "Field 'uvRatio' must be a number, a string or an array" );
            }
        } else if( element.isJsonArray() ) {
            JsonArray array = element.getAsJsonArray();
            if( array.size() != 2 ) {
                throw new JsonSyntaxException( "Array 'uvRatio' must contain 2 numbers" );
            }

            JsonElement a = array.get( 0 );
            JsonElement b = array.get( 1 );

            if( ! a.isJsonPrimitive() || ! b.isJsonPrimitive() || ! a.getAsJsonPrimitive().isNumber() || ! b.getAsJsonPrimitive().isNumber() )
                throw new JsonSyntaxException( "Array 'uvRatio' must contain 2 numbers" );

            return a.getAsFloat() / b.getAsFloat();
        } else {
            throw new JsonSyntaxException( "Field 'uvRatio' must be a number, a string or an array" );
        }
    }
}
