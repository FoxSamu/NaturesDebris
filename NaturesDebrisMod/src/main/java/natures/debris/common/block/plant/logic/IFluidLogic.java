package natures.debris.common.block.plant.logic;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

import natures.debris.common.block.plant.IStateBuilder;
import natures.debris.common.block.plant.Plant;

/**
 * Generic fluid logic of a plant. A {@code IFluidLogic} implementation specifies how a plant should respond to fluids.
 * Generally four basic implementations can be imagined:
 * <ul>
 * <li><b>Air only</b>: This logic is the most common logic, plants with this logic only survive in air. Water and lava
 * destroy the plant upon taking its space.</li>
 * <li><b>Water only</b>: This logic is common for most waterlogged plants, plants with this logic only survive in
 * water. If water is removed, the plant destroys.</li>
 * <li><b>Water sources and air</b>: This logic is less common, plants with this logic survive in both air and water.
 * Lava and flowing water still destroy these fluids, but a source block of water can safely be removed or added to such
 * plants.</li>
 * <li><b>Any water and air</b>: This logic is uncommon and is not a natural logic in vanilla, plants with this logic
 * survive air and any kind of water. Both still and flowing water can be added to these plants, meaning that they won't
 * break when water flows in. Lava can still destroy these plants.
 * </li>
 * </ul>
 */
public interface IFluidLogic {
    /**
     * Add any properties to the block state, if necessary. When your plant supports water and air, a {@code
     * waterlogged} property might be added here.
     *
     * @param stateBuilder The state builder to feed state properties.
     */
    default void addProperties(IStateBuilder stateBuilder) {
    }

    /**
     * The reaction of the plant when the given fluid state is about to replace this plant while flowing.
     * <ul>
     * <li>{@link Reaction#KEEP KEEP}: Keeps the plant and prevents the fluid from flowing in. The fluid won't be able
     * to flow through this plant, the plant will not break nor accept the fluid.</li>
     * <li>{@link Reaction#DESTROY DESTROY}: Destroys the plant and lets the fluid flow in. The fluid is responsible for
     * determining how the plant is destroyed, water will in this case break the plant and let it drop an item while
     * lava just consumes the plant.</li>
     * <li>{@link Reaction#REMOVE REMOVE}: Removes the plant silently and lets the fluid flow in. Unlike {@code
     * DESTROY}, no destroying effects are played nor is an item dropped.</li>
     * <li>{@link Reaction#FLOOD FLOOD}: Keeps the plant, but lets the fluid flow in. Generally, when this is returned
     * {@link #withFluidState} is called in order to get the block state with the provided fluid.</li>
     * </ul>
     * Due to the nature of fluids in Minecraft the provided fluid may not always be the fluid that is going to be the
     * actual fluid that tries to replace the plant.
     *
     * @param plant The plant to be replaced
     * @param state The block state of the plant to be replaced
     * @param fluid The fluid state that wants to replace this plant
     * @return The reaction to the fluid, as specified above. When null is returned, it implies {@link Reaction#KEEP
     *     KEEP}.
     */
    Reaction fluidReplace(Plant plant, BlockState state, FluidState fluid);

    /**
     * The reaction of the plant when the plant is being placed into the given fluid, usually by a player.
     * <ul>
     * <li>{@link Reaction#KEEP KEEP}: Ignores and replaces the fluid with the plant. Surrounding fluid may still
     * consume this plant later when {@link #fluidReplace} determines so.</li>
     * <li>{@link Reaction#DESTROY DESTROY}: Prevents the plant from being placed into the fluid. If a player is placing
     * the plant, it behaves the same as {@code REMOVE} and the player will keep the item. However, some other placing
     * logic may choose to drop an item on {@code DESTROY} but not on {@code REMOVE}.</li>
     * <li>{@link Reaction#REMOVE REMOVE}: Prevents the plant from being placed into the fluid. Similar, if not equal,
     * to {@code DESTROY}. It will never cause an item to be dropped even when {@code DESTROY} may do so.</li>
     * <li>{@link Reaction#FLOOD FLOOD}: Places the plant in the fluid. Generally, when this is returned {@link
     * #withFluidState} is called in order to get the block state to be placed to keep the provided fluid.</li>
     * </ul>
     *
     * @param plant The plant to be placed
     * @param state The block state of the plant to be placed
     * @param fluid The fluid state that is currently at the place of the plant to be placed
     * @return The reaction to the fluid, as specified above. When null is returned, it implies {@link Reaction#KEEP
     *     KEEP}.
     */
    Reaction placeInFluid(Plant plant, BlockState state, FluidState fluid);

    /**
     * The reaction of the plant when the plant naturally tries to grow into the specified fluid, or when it is
     * generated within the fluid.
     * <ul>
     * <li>{@link Reaction#KEEP KEEP}: Ignores and replaces the fluid with the plant. Surrounding fluid may still
     * consume this plant later when {@link #fluidReplace} determines so.</li>
     * <li>{@link Reaction#DESTROY DESTROY}: Prevents the plant from being placed into the fluid.</li>
     * <li>{@link Reaction#REMOVE REMOVE}: Prevents the plant from being placed into the fluid. Same as {@code
     * DESTROY} in this case.</li>
     * <li>{@link Reaction#FLOOD FLOOD}: Places the plant in the fluid. Generally, when this is returned {@link
     * #withFluidState} is called in order to get the block state to be placed to keep the provided fluid.</li>
     * </ul>
     *
     * @param plant The plant to be placed
     * @param state The block state of the plant to be placed
     * @param fluid The fluid state that is currently at the place of the plant to be placed
     * @return The reaction to the fluid, as specified above. When null is returned, it implies {@link Reaction#KEEP
     *     KEEP}.
     */
    Reaction growInFluid(Plant plant, BlockState state, FluidState fluid);

    /**
     * The reaction of the plant when it is tried to be generated in the specified fluid.
     * <ul>
     * <li>{@link Reaction#KEEP KEEP}: Ignores and replaces the fluid with the plant. Surrounding fluid may still
     * consume this plant later when {@link #fluidReplace} determines so.</li>
     * <li>{@link Reaction#DESTROY DESTROY}: Prevents the plant from being placed into the fluid.</li>
     * <li>{@link Reaction#REMOVE REMOVE}: Prevents the plant from being placed into the fluid. Same as {@code
     * DESTROY} in this case.</li>
     * <li>{@link Reaction#FLOOD FLOOD}: Places the plant in the fluid. Generally, when this is returned {@link
     * #withFluidState} is called in order to get the block state to be placed to keep the provided fluid.</li>
     * </ul>
     *
     * @param plant The plant to be generated
     * @param state The block state of the plant to be generated
     * @param fluid The fluid state that is currently at the place of the plant to be generated
     * @return The reaction to the fluid, as specified above. When null is returned, it implies {@link Reaction#KEEP
     *     KEEP}.
     */
    Reaction generateInFluid(Plant plant, BlockState state, FluidState fluid);

    /**
     * The reaction of the plant when when a player tries to remove a fluid in this plant using a bucket. This means
     * that the fluid is being replaced by air.
     * <ul>
     * <li>{@link Reaction#KEEP KEEP}: Prevents the bucket from removing the fluid in this plant. Depending on the
     * context the bucket action might still try to remove the fluid on an adjacent block.</li>
     * <li>{@link Reaction#DESTROY DESTROY}: Destroys the plant and removes the fluid. This causes destroy effects to
     * be played and an item to be dropped.</li>
     * <li>{@link Reaction#REMOVE REMOVE}: Silently removes the plant and removes the fluid. Unlike {@code DESTROY}
     * this does not cause destroy effects, and no item will be dropped.</li>
     * <li>{@link Reaction#FLOOD FLOOD}: Removes the fluid and keeps the plant. Generally, when this is returned {@link
     * #withFluidState} is called in order to get the block state with the updated fluid.</li>
     * </ul>
     *
     * @param plant The plant being targeted
     * @param state The block state of the plant being targeted
     * @return The reaction to the fluid, as specified above. When null is returned, it implies {@link Reaction#KEEP
     *     KEEP}.
     */
    Reaction bucketRemoveFluid(Plant plant, BlockState state);

    /**
     * Returns the fluid state that the given block state is flooded in. This must be computed based on the properties
     * of the block state. This must not call {@link BlockState#getFluidState} as this method will be called from that
     * function.
     *
     * @param plant The plant of the block state
     * @param state The block state to get the fluid medium of
     * @return The fluid medium of the specified block state
     */
    FluidState getFluidState(Plant plant, BlockState state);

    /**
     * Returns the given block state, but with the fluid medium given. This is generally called when {@link
     * Reaction#FLOOD FLOOD} is returned by any of the reaction methods of this class (i.e. those that return a {@link
     * Reaction} object. When the state is not updated by this method, {@link Reaction#FLOOD FLOOD} will have
     * effectively the same effect as {@link Reaction#KEEP KEEP}.
     *
     * @param plant The plant of the block state
     * @param state The block state to change medium of
     * @param fluid The new medium for the fluid
     * @return The given block state in a new fluid medium
     */
    default BlockState withFluidState(Plant plant, BlockState state, FluidState fluid) {
        return state;
    }

    /**
     * The reaction of a plant when meeting a certain fluid. This is the value returned by {@link #fluidReplace}, {@link
     * #growInFluid}, {@link #generateInFluid}, {@link #placeInFluid} and {@link #bucketRemoveFluid}. See the
     * description of those methods for an exact description of what each different reaction type does when returned.
     */
    enum Reaction {
        /** Keep the plant and block the fluid change. */
        KEEP,

        /** Destroy the plant with effects. */
        DESTROY,

        /** Silently remove the plant. */
        REMOVE,

        /** Keep the plant and change fluid. */
        FLOOD
    }
}
