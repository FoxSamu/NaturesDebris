package modernity.common.environment.event;

import net.minecraft.nbt.CompoundNBT;

/**
 * Represents a single environment event in a world with an {@link EnvironmentEventManager}.
 */
public abstract class EnvironmentEvent {
    private final EnvironmentEventType type;
    private final EnvironmentEventManager manager;
    private boolean active;

    public EnvironmentEvent( EnvironmentEventType type, EnvironmentEventManager manager ) {
        this.type = type;
        this.manager = manager;
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
     * Returns true when this event is inactive
     *
     * @see #isActive()
     */
    public boolean isInactive() {
        return ! active;
    }

    /**
     * Sets the activeness of this environment event.
     *
     * @param active True if this event must be active, false otherwise.
     * @see #stop()
     * @see #start()
     */
    public void setActive( boolean active ) {
        if( this.active != active ) {
            this.active = active;
            if( active ) {
                onStart();
            } else {
                onStop();
            }
        }
    }

    /**
     * Sets this event to inactive, calling {@linkplain #setActive <code>setActive</code>}{@code (false)}.
     *
     * @see #start()
     */
    public void stop() {
        setActive( false );
    }

    /**
     * Sets this event to active, calling {@linkplain #setActive <code>setActive</code>}{@code (true)}.
     *
     * @see #stop()
     */
    public void start() {
        setActive( true );
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

    /**
     * Writes this event to the specified NBT tag.
     */
    public abstract void write( CompoundNBT nbt );

    /**
     * Reads this event from the specified NBT tag.
     */
    public abstract void read( CompoundNBT nbt );
}
