/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

import modernity.common.biome.MDBiomes;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.Random;
import java.util.function.Predicate;

// TODO: This is done now: start entity exporter
public class GuiWikiBlockEdit extends GuiWikiEdit {
    private static final long SEED = 75179919239919239L;

    private final GuiWikiTextField<IBlockState> blockInput; // IBlockState


    private IBlockState state;
    private ExportableState exportable;
    private RotateMode rotateMode = RotateMode.TOP_3D;
    private Random rand = new Random();
    private boolean enableFluids = true;

    private final World worldInstance = new World();

    public GuiWikiBlockEdit( GuiScreen lastScreen ) {
        super( lastScreen );

        WikiBlockInputParser highlighter = new WikiBlockInputParser();
        textFields.add( blockInput = new GuiWikiTextField<>( 0, mc.fontRenderer, width / 2 - 155, height - 64, 310, 20, highlighter ) );

        blockInput.setTextFormatter( highlighter );
        blockInput.setMaxStringLength( Integer.MAX_VALUE );
    }

    @Override
    protected void initialize() {
        labels.add( new GuiWikiLabel( WikiTranslations.blockState, 0xffffffff, width / 2 - 155, height - 74, true ) );
        addButton( new GuiWikiButton( 0, 20, 70, 100, 20, rotateMode.name ).setEventHandler(
                b -> b.setText( ( rotateMode = rotateMode.next() ).name )
        ) );
        addButton( new GuiWikiButton( 0, 20, 94, 100, 20, WikiTranslations.fluidEnabled + "." + enableFluids ).setEventHandler(
                b -> b.setText( WikiTranslations.fluidEnabled + "." + ( enableFluids = ! enableFluids ) )
        ) );

        blockInput.x = width / 2 - 155;
        blockInput.y = height - 64;
    }

    @Override
    public void tick() {
        for( GuiTextField tf : textFields ) {
            tf.tick();
        }

        updateBlockstate( blockInput.getValue() );
    }

    private void updateBlockstate( IBlockState state ) {
        this.state = state == null ? Blocks.AIR.getDefaultState() : state;
        exportable = new ExportableState( this.state );
        worldInstance.state = this.state;
    }

    @Override
    protected int getContainerWidth() {
        return 16;
    }

    @Override
    protected int getContainerHeight() {
        return 16;
    }

    @Override
    public void renderContainer( int mx, int my, boolean export ) {
        if( ! export ) {
            drawRect( rootX - 8, rootY - 8, rootX + 24, rootY + 24, 0xffffffff );
        }
        if( state != null ) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef( this.rootX + 8, this.rootY + 8, this.zLevel );
            float s = this.rotateMode.scale * ( export ? 16 : 32 );
            GlStateManager.scalef( s, - s, s );
            GlStateManager.pushMatrix();
            this.rotateMode.rotate( 0 );
            this.drawState( 0, 0 );
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }

    private void drawBlock( BufferBuilder buff ) {
        IBakedModel model = mc.getBlockRendererDispatcher().getModelForState( state );
        mc.getBlockRendererDispatcher()
          .getBlockModelRenderer()
          .renderModel( worldInstance, model, exportable, BlockPos.ORIGIN, buff, false, rand, SEED, EmptyModelData.INSTANCE );
    }

    private void drawFluid( BufferBuilder buff ) {
        mc.getBlockRendererDispatcher().renderFluid( BlockPos.ORIGIN, worldInstance, buff, state.getFluidState() );
    }

    private void drawTranslucent() {
        rand.setSeed( SEED );
        IFluidState fstate = state.getFluidState();
        BlockRenderLayer blockLayer = state.getBlock().getRenderLayer();
        BlockRenderLayer fluidLayer = fstate.getFluid().getRenderLayer();
        if( blockLayer != BlockRenderLayer.TRANSLUCENT && ( fluidLayer != BlockRenderLayer.TRANSLUCENT || ! enableFluids ) ) {
            return;
        }
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.alphaFunc( 0x207, 0.5F );
        GlStateManager.depthMask( false );

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        buff.begin( 7, DefaultVertexFormats.BLOCK );

        if( blockLayer == BlockRenderLayer.TRANSLUCENT ) {
            drawBlock( buff );
        }

        if( fluidLayer == BlockRenderLayer.TRANSLUCENT && enableFluids ) {
            drawFluid( buff );
        }

        buff.sortVertexData( rotateMode.cameraX, rotateMode.cameraY, rotateMode.cameraZ );
        tess.draw();

        GlStateManager.depthMask( true );
    }

    private void drawSolid() {
        rand.setSeed( SEED );
        IFluidState fstate = state.getFluidState();
        BlockRenderLayer blockLayer = state.getBlock().getRenderLayer();
        BlockRenderLayer fluidLayer = fstate.getFluid().getRenderLayer();
        if( blockLayer == BlockRenderLayer.TRANSLUCENT && ( fluidLayer == BlockRenderLayer.TRANSLUCENT || ! enableFluids ) ) {
            return;
        }
        GlStateManager.disableBlend();
        if( state.getBlock().getRenderLayer() == BlockRenderLayer.CUTOUT || state.getBlock().getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED ) {
            GlStateManager.enableAlphaTest();
            GlStateManager.alphaFunc( 516, 0.5F );
        } else {
            GlStateManager.disableAlphaTest();
            GlStateManager.alphaFunc( 0x207, 0.5F );
        }

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        buff.begin( 7, DefaultVertexFormats.BLOCK );

        if( blockLayer != BlockRenderLayer.TRANSLUCENT ) {
            drawBlock( buff );
        }

        if( fluidLayer != BlockRenderLayer.TRANSLUCENT && enableFluids ) {
            drawFluid( buff );
        }

        tess.draw();
    }

    public void drawState( int x, int y ) {
        GlStateManager.pushMatrix();
        GlStateManager.translated( - 0.5, - 0.5, - 0.5 );
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.enableDepthTest();
        GlStateManager.enableCull();
        GlStateManager.depthMask( true );
        GlStateManager.blendFuncSeparate( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA );

        this.mc.getTextureManager().bindTexture( TextureMap.LOCATION_BLOCKS_TEXTURE );
        this.mc.getTextureManager().getTexture( TextureMap.LOCATION_BLOCKS_TEXTURE ).setBlurMipmap( false, false );

        drawSolid();
        drawTranslucent();

        this.mc.getTextureManager().getTexture( TextureMap.LOCATION_BLOCKS_TEXTURE ).restoreLastBlurMipmap();

        GlStateManager.disableLighting();
        GlStateManager.disableColorMaterial();
        GlStateManager.disableDepthTest();
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.alphaFunc( 516, 0.01F );
        GlStateManager.popMatrix();
    }

    private static class World implements IWorldReader {

        private IBlockState state;

        @Override
        public int getCombinedLight( BlockPos pos, int lightValue ) {
            return 15 << 20 | 15 << 4;
        }

        @Override
        public boolean isAirBlock( BlockPos pos ) {
            return true;
        }

        @Override
        public Biome getBiome( BlockPos pos ) {
            return MDBiomes.MEADOW;
        }

        @Override
        public int getLightFor( EnumLightType type, BlockPos pos ) {
            return 15;
        }

        @Override
        public int getLightSubtracted( BlockPos pos, int amount ) {
            return 15;
        }

        @Override
        public boolean isChunkLoaded( int x, int z, boolean allowEmpty ) {
            return true;
        }

        @Override
        public boolean canSeeSky( BlockPos pos ) {
            return true;
        }

        @Override
        public int getHeight( Heightmap.Type heightmapType, int x, int z ) {
            return 0;
        }

        @Nullable
        @Override
        public EntityPlayer getClosestPlayer( double x, double y, double z, double distance, Predicate<Entity> predicate ) {
            return null;
        }

        @Override
        public int getSkylightSubtracted() {
            return 15;
        }

        @Override
        public WorldBorder getWorldBorder() {
            return new WorldBorder();
        }

        @Override
        public boolean checkNoEntityCollision( @Nullable Entity entityIn, VoxelShape shape ) {
            return true;
        }

        @Override
        public int getStrongPower( BlockPos pos, EnumFacing direction ) {
            return 0;
        }

        @Override
        public boolean isRemote() {
            return true;
        }

        @Override
        public int getSeaLevel() {
            return 0;
        }

        @Override
        public Dimension getDimension() {
            return null;
        }

        @Nullable
        @Override
        public TileEntity getTileEntity( BlockPos pos ) {
            return null;
        }

        @Override
        public IBlockState getBlockState( BlockPos pos ) {
            if( pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0 )
                return state;
            return Blocks.AIR.getDefaultState();
        }

        @Override
        public IFluidState getFluidState( BlockPos pos ) {
            return getBlockState( pos ).getFluidState();
        }

        @Override
        public int hashCode() {
            return state.hashCode();
        }

        @Override
        public boolean equals( Object obj ) {
            return state.equals( obj );
        }

        @Override
        public String toString() {
            return state.toString();
        }
    }

    private static class ExportableState implements IBlockState {

        private final IBlockState wrapped;

        private ExportableState( IBlockState wrapped ) {
            this.wrapped = wrapped;
        }

        @Override
        public Block getBlock() {
            return wrapped.getBlock();
        }

        @Override
        public Collection<IProperty<?>> getProperties() {
            return wrapped.getProperties();
        }

        @Override
        public <T extends Comparable<T>> boolean has( IProperty<T> property ) {
            return wrapped.has( property );
        }

        @Override
        public <T extends Comparable<T>> T get( IProperty<T> property ) {
            return wrapped.get( property );
        }

        @Override
        public <T extends Comparable<T>, V extends T> IBlockState with( IProperty<T> property, V value ) {
            return new ExportableState( wrapped.with( property, value ) );
        }

        @Override
        public <T extends Comparable<T>> IBlockState cycle( IProperty<T> property ) {
            return new ExportableState( wrapped.cycle( property ) );
        }

        @Override
        public ImmutableMap<IProperty<?>, Comparable<?>> getValues() {
            return wrapped.getValues();
        }

        @Override
        public Vec3d getOffset( IBlockReader access, BlockPos pos ) {
            return Vec3d.ZERO;
        }
    }

    protected enum RotateMode {
        TOP_3D( 0.625F, 30, 45, "top_3d" ),
        TOP_3D_SIDE( 0.625F, 30, 0, "top_3d_side" ),
        BOTTOM_3D( 0.625F, - 30, 45, "bottom_3d" ),
        BOTTOM_3D_SIDE( 0.625F, - 30, 0, "bottom_3d_side" ),
        FRONT( 1, 0, 0, "front" ),
        RIGHT( 1, 0, - 90, "right" ),
        BACK( 1, 0, 180, "back" ),
        LEFT( 1, 0, 90, "left" ),
        TOP( 1, 90, 0, "top" ),
        BOTTOM( 1, - 90, 180, "bottom" );

        private final float scale;
        private final float rotateX;
        private final float rotateY;
        public final float cameraX;
        public final float cameraY;
        public final float cameraZ;
        public final String name;

        RotateMode( float scale, float rotateX, float rotateY, String name ) {
            this.scale = scale;
            this.rotateX = rotateX;
            this.rotateY = rotateY;
            this.name = WikiTranslations.rotationMode + "." + name;

            Vector3f vec = new Vector3f( - 1, 0, 0 );
            Matrix4f mat = new Matrix4f();
            mat.rotX( - rotateX );
            mat.rotY( rotateY );
            mat.transform( vec );

            this.cameraX = vec.x;
            this.cameraY = vec.y;
            this.cameraZ = vec.z;
        }

        public RotateMode next() {
            return values()[ ordinal() + 1 & 7 ];
        }

        public void rotate( float lastValidRot ) {
            GlStateManager.rotatef( this.rotateX, 1, 0, 0 );
            GlStateManager.rotatef( this.rotateY + lastValidRot, 0, 1, 0 );
        }
    }
}
