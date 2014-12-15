package ca.q0r.sponge.mchat.events.custom;

import org.spongepowered.api.util.event.Cancellable;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.callback.CallbackList;

/**
 * Event that is fired when Variables are replaced.
 */
public class ReplaceEvent implements Event, Cancellable {
    private String var, value, format;
    private boolean cancelled;

    /**
     * Instantiates Event
     *
     * @param var    Variable being processed.
     * @param value  Value of Variable.
     * @param format Format being replaced.
     */
    public ReplaceEvent(String var, String value, String format) {
        this.var = var;
        this.value = value;
        this.format = format;

        this.cancelled = false;
    }

    //TODO Find out what these /should/ do!
    public CallbackList getCallbacks() {
        return new CallbackList();
    }

    /**
     * Checks whether the Event is cancelled.
     *
     * @return Event cancellation state.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets whether the Event is cancelled.
     *
     * @param cancel Event cancellation state.
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Get Variable being processed.
     *
     * @return Variable being processed.
     */
    public String getVariable() {
        return var;
    }

    /**
     * Get Value being processed.
     *
     * @return Value being processed.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets Value being processed.
     *
     * @param value Value being processed.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get Format being replaced.
     *
     * @return Format being replaced.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets Format being replaced.
     *
     * @param format Format being replaced.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Get Replaced Format after processing.
     *
     * @return Replaced Format after processing.
     */
    public String getReplacedFormat() {
        return format.replace(var, value);

    }
}