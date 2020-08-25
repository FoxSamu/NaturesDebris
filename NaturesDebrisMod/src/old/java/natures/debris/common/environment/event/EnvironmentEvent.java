/*
 * Copyright (c) 2020 RedGalaxy & contributors
 * All rights reserved. Do not distribute.
 *
 * For a full license, see LICENSE.txt
 */

package natures.debris.common.environment.event;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.CompoundNBT;

/**
 * Represents a single environment event in a world with an {@link EnvironmentEventManager}.
 */
public abstract class EnvironmentEvent {
    private final EnvironmentEventType type;
    private final EnvironmentEventManager manager;
    private boolean active;

    private boolean enabled = true;

    public EnvironmentEvent(EnvironmentEventType type, EnvironmentEventManager manager) {
        this.type = type;
        this.manager = manager;
    }

    /**
     * Returns this event in the specified command context. Use this to get the actual event to apply command
     * invocations on, instead of using {@code this}. Because a dummy event is used to build the commands, {@code this}
     * will reference the dummy event and not the actual event.
     */
    public static <T extends EnvironmentEvent> T getFromCommand(CommandContext<CommandSource> ctx, EnvironmentEventType type) {
        return getFromCommand(ctx.getSource(), type);
    }

    /**
     * Returns this event in the specified command context. Use this to get the actual event to apply command
     * invocations on, instead of using {@code this}. Because a dummy event is used to build the commands, {@code this}
     * will reference the dummy event and not the actual event.
     */
    public static <T extends EnvironmentEvent> T getFromCommand(CommandSource src, EnvironmentEventType type) {
        // TODO Re-evaluate
//        World world = src.getWorld();
//        if( world.dimension instanceof IEnvEventsDimension ) {
//            return ( (IEnvEventsDimension) world.dimension ).getEnvEventManager().getByType( type );
//        }
        return null;
    }

    /**
     * Returns the type of this environment event.
     */
    public EnvironmentEventType getType() {
        return type;
    }

    /**
     * Returns the manager that has this environment event.
     */
    public EnvironmentEventManager getManager() {
        return manager;
    }

    /**
     * Returns true when this event is active.
     *
     * @see #isInactive()
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the activeness of this environment event.
     *
     * @param active True if this event must be active, false otherwise.
     * @see #stop()
     * @see #start()
     */
    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
            if (active) {
                onStart();
            } else {
                onStop();
            }
        }
    }

    /**
     * Returns true when this event is inactive
     *
     * @see #isActive()
     */
    public boolean isInactive() {
        return !active;
    }

    /**
     * Sets this event to inactive, calling {@linkplain #setActive <code>setActive</code>}{@code (false)}.
     *
     * @see #start()
     */
    public void stop() {
        setActive(false);
    }

    /**
     * Sets this event to active, calling {@linkplain #setActive <code>setActive</code>}{@code (true)}.
     *
     * @see #stop()
     */
    public void start() {
        setActive(true);
    }

    /**
     * Called when the status of this event changes from <i>inactive</i> to <i>active</i>.
     */
    protected void onStart() {

    }

    /**
     * Called when the status of this event changes from <i>active</i> to <i>inactive</i>.
     */
    protected void onStop() {

    }

    /**
     * Called to update this event.
     */
    public void tick() {

    }

    protected void onEnable() {

    }

    protected void onDisable() {
        stop();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    /**
     * Writes this event to the specified NBT tag.
     */
    public abstract void write(CompoundNBT nbt);

    /**
     * Reads this event from the specified NBT tag.
     */
    public abstract void read(CompoundNBT nbt);

    /**
     * Creates a string for this event to display in the event debug monitor.
     */
    public abstract String debugInfo();
}
