/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package modernity.common.blockold.plant;

import modernity.common.blockold.MDBlockStateProperties;
import modernity.common.blockold.MDBlockTags;
import modernity.common.blockold.MDNatureBlocks;
import modernity.common.blockold.fluid.IMurkyWaterloggedBlock;
import modernity.common.fluidold.MDFluids;
import modernity.common.particle.MDParticleTypes;
import modernity.generic.util.MovingBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SaltCrystalBlock extends SingleDirectionalPlantBlock implements IMurkyWaterloggedBlock {
    public static final IntegerProperty AGE = MDBlockStateProperties.AGE_0_11;
    public static final BooleanProperty NATURAL = MDBlockStateProperties.NATURAL;
    public static final BooleanProperty CAN_SPREAD = MDBlockStateProperties.CAN_SPREAD;

    private static final int MAX_GROW_CHANCE = 6 * 10;
    private static final int MAX_SPREAD_CHANCE = MAX_GROW_CHANCE * 5;

    private static final int[] STAGE_HEIGHTS = {2, 2, 4, 4, 5, 6, 7, 7, 8, 9, 11, 12};

    private static final int[] SPREAD_CHANCE_MULT = {0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 4, 5};

    private static final AxisAlignedBB[] STATE_BOXES = new AxisAlignedBB[12];
    private static final VoxelShape[] STATE_SHAPES = new VoxelShape[12];

    static {
        for(int i = 0; i <= 11; i++) {
            STATE_BOXES[i] = new AxisAlignedBB(0 / 16D, 0, 0 / 16D, 16 / 16D, STAGE_HEIGHTS[i] / 16D, 16 / 16D);
            STATE_SHAPES[i] = VoxelShapes.create(STATE_BOXES[i]);
        }
    }

    /**
     * All relative positions a salt crystal can grow to.
     * <p>
     * Salt crystal grow area (s = source):
     * <pre>
     *   o o o
     * o o o o o
     * o o s o o
     * o o o o o
     *   o o o
     * </pre>
     */
    private static final BlockPos[] GROW_AREA = {
        new BlockPos(-1, 0, 0),
        new BlockPos(-1, 0, 1),
        new BlockPos(0, 0, 1),
        new BlockPos(1, 0, 1),
        new BlockPos(1, 0, 0),
        new BlockPos(1, 0, -1),
        new BlockPos(0, 0, -1),
        new BlockPos(-1, 0, -1),

        new BlockPos(-2, 0, -1),
        new BlockPos(-2, 0, 0),
        new BlockPos(-2, 0, 1),

        new BlockPos(-1, 0, 2),
        new BlockPos(-0, 0, 2),
        new BlockPos(1, 0, 2),

        new BlockPos(2, 0, 1),
        new BlockPos(2, 0, 0),
        new BlockPos(2, 0, -1),

        new BlockPos(1, 0, -2),
        new BlockPos(0, 0, -2),
        new BlockPos(-1, 0, -2)
    };

    private static final int[] SPREAD_DIR_CHANCES = {2, 3, 2, 3, 2, 3, 2, 3, 5, 4, 5, 5, 4, 5, 5, 4, 5, 5, 4, 5};

    public SaltCrystalBlock(Properties properties) {
        super(properties, Direction.UP);

        setDefaultState(getDefaultState().with(AGE, 0)
                                         .with(NATURAL, true)
                                         .with(CAN_SPREAD, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(AGE, NATURAL, CAN_SPREAD);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        MovingBlockPos mpos = new MovingBlockPos();


        int age = state.get(AGE);
        boolean natural = state.get(NATURAL);
        boolean canSpread = state.get(CAN_SPREAD);


        int growChance = computeGrowChance(world, pos, natural);
        if(growChance > 0 && rand.nextInt(MAX_GROW_CHANCE) < growChance) {
            // Grow if not at full age
            if(age < 11) {
                age++; // Replace old values with new so that we can continue our processing with new values
                state = state.with(AGE, age);
                world.setBlockState(pos, state, 3);
            }

            // Spreading is only done when growing is possible
            if(canSpread) {

                int spreadChance = growChance * SPREAD_CHANCE_MULT[age];
                if(spreadChance > 0) {

                    // Compute the chance that a certain crystal is natural or can't spread
                    int noSpreadChance = 8;
                    int naturalChance = 8;

                    if(world.getFluidState(pos).getFluid() == MDFluids.MURKY_WATER) {
                        noSpreadChance = 5; // Lower values: higher chances
                        naturalChance = 6;
                    }


                    int amount = 2 + rand.nextInt(7);
                    for(int i = 0; i < amount; i++) {

                        if(rand.nextInt(MAX_SPREAD_CHANCE) < spreadChance) {

                            int offIndex = rand.nextInt(20); // Index in offset lookup table

                            if(rand.nextInt(SPREAD_DIR_CHANCES[offIndex]) == 0) {
                                BlockPos offset = GROW_AREA[offIndex];

                                // Check vertical offset
                                for(int y = 1; y >= -1; y--) {
                                    mpos.setPos(pos).addPos(offset).moveUp(y);

                                    // Check sustaining block
                                    mpos.moveDown();
                                    if(!canBlockSustain(world, mpos, world.getBlockState(mpos))) {
                                        continue;
                                    }
                                    mpos.moveUp();

                                    // Check if block at location is empty
                                    BlockState bstate = world.getBlockState(mpos);
                                    IFluidState fstate = bstate.getFluidState();
                                    if(!bstate.isAir(world, mpos) && !(fstate.getFluid() == MDFluids.MURKY_WATER && bstate.getBlock() == MDNatureBlocks.MURKY_WATER)) {
                                        continue;
                                    }

                                    // Recursively check if block is reachable from root crystal
                                    // We can move at y=0 and then go down so we check y=-1 as if y=0
                                    int offY = y == -1 ? 0 : y;
                                    if(!canReachLocation(world, pos, offset.getX(), offY, offset.getZ(), mpos)) {
                                        // Also try y=-1
                                        if(y != -1 || !canReachLocation(world, pos, offset.getX(), -1, offset.getZ(), mpos)) {
                                            continue;
                                        }
                                    }

                                    // Naturality and spreadability
                                    boolean isNatural = rand.nextInt(naturalChance) == 0;
                                    boolean noSpread = rand.nextInt(noSpreadChance) == 0;

                                    // Apply growing chance
                                    int localChance = computeGrowChance(world, mpos, isNatural);
                                    if(localChance > 0 && rand.nextInt(MAX_GROW_CHANCE) < localChance) {
                                        mpos.setPos(pos).addPos(offset).moveUp(y);

                                        // Place block
                                        BlockState placeState = getDefaultState().with(CAN_SPREAD, !noSpread)
                                                                                 .with(NATURAL, isNatural);

                                        world.setBlockState(mpos, computeStateForPos(world, mpos, placeState), 3);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @Override
    public BlockState computeStateForPos(IWorldReader world, BlockPos pos, BlockState state) {
        return state.with(WATERLOGGED, world.getFluidState(pos).getFluid() == MDFluids.MURKY_WATER);
    }

    @Override
    public BlockState computeStateForGeneration(IWorldReader world, BlockPos pos, Random rand) {
        return computeStateForPos(world, pos, getGenerationStage(rand));
    }

    private boolean canReachLocation(World world, BlockPos pos, int x, int y, int z, MovingBlockPos mpos) {
        if(x == 0 && z == 0) return true;

        mpos.setPos(pos).addPos(x, y, z);
        if(world.getBlockState(mpos).getMaterial().blocksMovement()) return false;

        boolean canReach = false;
        if(x > 0) canReach = canReachLocation(world, pos, x - 1, y, z, mpos);
        if(x < 0) canReach = canReachLocation(world, pos, x + 1, y, z, mpos);
        if(z > 0 && !canReach) canReach = canReachLocation(world, pos, x, y, z - 1, mpos);
        if(z < 0 && !canReach) canReach = canReachLocation(world, pos, x, y, z + 1, mpos);

        return canReach;
    }

    private int computeGrowChance(World world, BlockPos pos, boolean natural) {
        int sources = 0;

        MovingBlockPos mpos = new MovingBlockPos();

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                mpos.setPos(pos).addPos(x, -1, z);

                if(isSaltSource(world.getBlockState(mpos))) {
                    sources++;
                    if(x == 0 && z == 0) {
                        sources++;
                    }
                }
            }
        }

        int fluid = 0;

        if(world.getFluidState(pos).getFluid() == MDFluids.MURKY_WATER) {
            fluid = 3;
        }

        if(natural) fluid++;

        return sources * Math.min(6, fluid);
    }

    private boolean isSaltSource(BlockState state) {
        return state.isIn(MDBlockTags.SALT_SOURCE);
    }

    public BlockState getGenerationStage(Random rand) {
        int randAge = rand.nextInt(6);
        if(rand.nextBoolean()) {
            randAge += rand.nextInt(7);
        }
        return getDefaultState().with(AGE, randAge);
    }

    @Override
    public VoxelShape getShape(BlockState state) {
        return STATE_SHAPES[state.get(AGE)];
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if(rand.nextInt(10) == 0) {
            AxisAlignedBB aabb = STATE_BOXES[state.get(AGE)];

            double x = rand.nextDouble() * (aabb.maxX - aabb.minX) + aabb.minX + pos.getX();
            double y = rand.nextDouble() * (aabb.maxY - aabb.minY) + aabb.minY + pos.getY();
            double z = rand.nextDouble() * (aabb.maxZ - aabb.minZ) + aabb.minZ + pos.getZ();

            world.addParticle(MDParticleTypes.SALT, x, y, z, rand.nextDouble() * 0.04 - 0.02, rand.nextDouble() * 0.04 - 0.02, rand.nextDouble() * 0.04 - 0.02);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockPos pos = ctx.getPos();
        World world = ctx.getWorld();
        return getDefaultState().with(AGE, 0)
                                .with(NATURAL, false)
                                .with(CAN_SPREAD, true)
                                .with(WATERLOGGED, world.getFluidState(pos).getFluid() == MDFluids.MURKY_WATER);
    }

    @Override
    public boolean canBlockSustain(IWorldReader world, BlockPos pos, BlockState state) {
        return isBlockSideSustainable(state, world, pos, Direction.UP);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
}
