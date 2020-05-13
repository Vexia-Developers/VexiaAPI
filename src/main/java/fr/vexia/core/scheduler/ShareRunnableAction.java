package fr.vexia.core.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Share executable without counter and ends when it is canceled
 *
 */
public abstract class ShareRunnableAction implements ShareRunnable {

    private boolean ended;

    /**
     * Constructor
     */
    public ShareRunnableAction() {
        this.ended = false;
    }

    @Override
    public boolean ended() {
        return this.ended;
    }

    @Override
    public void cancel() {
        this.ended = true;
    }

    @Override
    public void reset() {
        this.ended = false;
    }

    /**
     * Start the runnable with his actual state
     *
     * @param plugin Plugin to start the runnable
     * @param async  Know if he must works in async runnable
     * @param ticks  Number of ticks when the share runnable will be called to
     *               perform action
     */
    public void runTask(final Plugin plugin, final boolean async, final long ticks) {
        this.ended = false;
        ShareRunnableManager.addShareRunnable(plugin, async, ticks, this);
    }
}
