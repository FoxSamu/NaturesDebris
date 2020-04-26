package modernity.client.model.bush;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class BushModelGeometry implements IModelGeometry<BushModelGeometry> {
    private final int tint;
    private final float round;
    private final float uvRatio;
    private Material texture;
    private Material particles;

    public BushModelGeometry( int tint, float round, float uvRatio ) {
        this.tint = tint;
        this.round = round;
        this.uvRatio = uvRatio;
    }

    @Override
    public IBakedModel bake( IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation ) {
        return new BushBakedModel( spriteGetter.apply( texture ), spriteGetter.apply( particles ), owner, tint, round, uvRatio, overrides );
    }

    @Override
    public Collection<Material> getTextures( IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors ) {
        texture = owner.resolveTexture( "bush" );
        particles = owner.resolveTexture( "particle" );
        if( particles.getAtlasLocation().equals( MissingTextureSprite.getLocation() ) ) {
            particles = texture;
            return ImmutableSet.of( texture );
        }
        return ImmutableSet.of( texture, particles );
    }
}
