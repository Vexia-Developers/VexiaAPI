package fr.vexia.core.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Share executable with a counter
 *
 */
public abstract class ShareRunnableActionCounter implements ShareRunnable {

    /**
     * Value initial of counter put in parameter of constructor
     */
    private final int initCounter;
    /**
     * Actual value of counter that will be decreased
     */
    private int counter;

    /**
     * Constructor
     *
     * @param counter Initial counter that will be decreased to perform the actions
     *                before the end
     */
    public ShareRunnableActionCounter(final int counter) {
        this.counter = counter + 1;
        this.initCounter = this.counter;
    }

    @Override
    public void perform() {
        this.counter--;
        if(this.ended()) {
            this.performEnded(this.counter);
        } else {
            this.performContinue(this.counter);
        }
    }

    /**
     * Action when the counter is not finished
     *
     * @param counter Value of counter
     */
    protected abstract void performContinue(int counter);

    /**
     * Action when the counter is finished
     *
     * @param counter Value of counter
     */
    protected abstract void performEnded(int counter);

    @Override
    public boolean ended() {
        return this.counter <= 0;
    }

    @Override
    public void cancel() {
        this.counter = -1;
    }

    @Override
    public void reset() {
        this.counter = this.initCounter;
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
        ShareRunnableManager.addShareRunnable(plugin, async, ticks, this);
    }
}
