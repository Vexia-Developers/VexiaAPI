package fr.vexia.core.scheduler;

/**
 * Interface to manage action during a cycle from a {@link ShareRunnableManager}
 *
 */
public interface ShareRunnable {
    /**
     * Action to do
     */
    void perform();

    /**
     * Know if the share runnable is ended
     *
     * @return {@code true} if all actions are performed, {@code false} otherwise
     */
    boolean ended();

    /**
     * Stop the share runnable
     */
    void cancel();

    /**
     * Reset the counter to can restart the share runnable
     */
	void reset();
}
