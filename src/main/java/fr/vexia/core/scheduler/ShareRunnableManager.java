package fr.vexia.core.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

/**
 * Manager to control access with the share runnables and determined in which
 * handler should be is the share runnable
 *
 */
public final class ShareRunnableManager {

    /**
     * List of share runnable handler works in synchronized mode
     */
    private static final List<ShareRunnableHandler> SYNC_HANDLERS = new ArrayList<>(3);
    /**
     * List of share runnable handler works in async mode
     */
    private static final List<ShareRunnableHandler> ASYNC_HANDLERS = new ArrayList<>(3);

    /**
     * Add a new share runnable
     *
     * @param plugin   Plugin the reference to the plugin scheduling task
     * @param async    Work the runnable in async mode
     * @param ticks    Ticks on which the share runnable works
     * @param runnable Runnable that will be added
     */
    public static void addShareRunnable(final Plugin plugin, final boolean async, final long ticks, final ShareRunnable runnable) {
        if(ticks < 1) {
            throw new IllegalArgumentException("The number of ticks can not be under 1");
        }
        List<ShareRunnableHandler> handlerList = async ? ASYNC_HANDLERS : SYNC_HANDLERS;
        final ShareRunnableHandler handler = getOrCreateHandler(plugin, handlerList, ticks);
        handler.addRunnable(runnable);
    }

    /**
     * Get or create a {@link ShareRunnableHandler}
     *
     * @param plugin      Plugin to launch the handler if he doesn't exist
     * @param handlerList List that can have the handler
     * @param ticks       Ticks on which the handler works
     * @return An instance of the handler
     */
    private static ShareRunnableHandler getOrCreateHandler(final Plugin plugin, final List<ShareRunnableHandler> handlerList, final long ticks) {
        ShareRunnableHandler handler = find(handlerList, ticks);
        if(handler == null) {
            handler = new ShareRunnableHandler(ticks);
            String sentence = null;

            if(handlerList == SYNC_HANDLERS) {
                handler.runTaskTimer(plugin);
                sentence = "Synchrone";
            } else {
                handler.runTaskTimerAsynchronously(plugin);
                sentence = "Asynchrone";
            }

            plugin.getLogger().log(Level.INFO, ShareRunnableHandler.class.getSimpleName() + "[" + ticks + "ticks | " + sentence + "]: Démarrage");
            handlerList.add(handler);
        }
        return handler;
    }

    /**
     * Find a share runnable handler corresponding to a number of ticks
     *
     * @param handlerList List of handler
     * @param ticks       number of ticks
     * @return An instance of Share runnable handler if a handler is working on the
     * number of ticks, {@code null} otherwise
     */
    private static ShareRunnableHandler find(final List<ShareRunnableHandler> handlerList, final long ticks) {
        for(final ShareRunnableHandler runnable : handlerList) {
            if(runnable.getTicks() == ticks) {
                return runnable;
            }
        }
        return null;
    }

    /**
     * Cancel all share runnables working on a number of ticks
     *
     * @param ticks Number of ticks to find and cancel the runnables
     * @param sync  {@code true} if the cancel concerned the sync list,
     *              {@code false} otherwise
     * @param async {@code true} if the cancel concerned the async list,
     *              {@code false} otherwise
     */
    public static void cancelAllShareRunnable(final long ticks, final boolean sync, final boolean async) {
        if(sync) {
            cancelHandler(SYNC_HANDLERS, ticks);
        }
        if(async) {
            cancelHandler(ASYNC_HANDLERS, ticks);
        }
    }

    /**
     * Cancel all runnables contained in handler correspond to a number of ticks
     *
     * @param handlerList List of Handler
     * @param ticks       Number of ticks to find and cancel the runnables
     */
    private static void cancelHandler(final List<ShareRunnableHandler> handlerList, final long ticks) {
        final ShareRunnableHandler handler = find(handlerList, ticks);
        if(handler != null) {
            handler.cancel();
            handlerList.remove(handler);
        }
    }

    private ShareRunnableManager() {
    }

}
