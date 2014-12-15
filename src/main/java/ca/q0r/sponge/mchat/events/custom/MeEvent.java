package ca.q0r.sponge.mchat.events.custom;

import ca.q0r.sponge.mchat.api.Parser;
import org.spongepowered.api.util.event.Cancellable;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.callback.CallbackList;

import java.util.UUID;

/**
 * Event that is fired when /mchatme is used.
 */
public class MeEvent implements Event, Cancellable {
    private UUID uuid;
    private String world, message;
    private boolean cancelled;

    /**
     * Instantiates Event
     *
     * @param uuid    UUID of player that is executing this event.
     * @param world   World the Player is in.
     * @param message Message being relayed.
     */
    public MeEvent(UUID uuid, String world, String message) {
        this.uuid = uuid;
        this.world = world;
        this.message = message;

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
     * Gets UUID of Player.
     *
     * @return UUID of Player.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets Message the player is sending.
     *
     * @return Message the player is sending.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets Message being sent.
     *
     * @param message Message being sent.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets format being used.
     *
     * @return Format being used.
     */
    public String getFormat() {
        return Parser.parseMe(uuid, world, message);
    }
}