package natures.debris.common.block.plant.fluid;

/**
 * The reaction of a plant when meeting a certain fluid. This is the value returned by {@link IFluidLogic#fluidReplace},
 * {@link IFluidLogic#growInFluid}, {@link IFluidLogic#generateInFluid}, {@link IFluidLogic#placeInFluid} and {@link
 * IFluidLogic#bucketRemoveFluid}. See the description of those methods for an exact description of what each different
 * reaction type does when returned.
 */
public enum FluidReaction {
    /** Keep the plant and block the fluid change. */
    KEEP,

    /** Destroy the plant with effects. */
    DESTROY,

    /** Silently remove the plant. */
    REMOVE,

    /** Keep the plant and change fluid. */
    FLOOD
}
