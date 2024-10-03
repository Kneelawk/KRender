package com.kneelawk.krender.reloadlistener.api;

import com.kneelawk.commonevents.api.Event;

/**
 * This holds the events for reload listeners, both data-packs and resource-packs.
 */
public final class ReloadListenerEvents {
    private ReloadListenerEvents() {}

    /**
     * Event fired before any reload listeners are run.
     */
    public static final Event<Pre> PRE_RELOAD = Event.createSimple(Pre.class);

    /**
     * Event fired after all reload listeners have completed.
     */
    public static final Event<Post> POST_RELOAD = Event.createSimple(Post.class);

    /**
     * Callback for before any reload listeners are run.
     */
    public interface Pre {
        /**
         * Called before any reload listeners are run.
         *
         * @param ctx the context information for the current reload.
         */
        void beforeReload(ReloadContext ctx);
    }

    /**
     * Callback for after all reload listeners have completed.
     */
    public interface Post {
        /**
         * Called after all reload listeners have completed.
         * <p>
         * This is called on the game thread, which will be the client thread for resource packs and the server thread
         * for data packs.
         *
         * @param ctx the context information for the just finished reload.
         */
        void afterReload(ReloadContext ctx);
    }
}
