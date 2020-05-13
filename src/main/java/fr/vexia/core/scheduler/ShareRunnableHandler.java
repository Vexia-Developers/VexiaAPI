package fr.vexia.core.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Handler to perform multiples share runnable
 *
 */
public class ShareRunnableHandler extends BukkitRunnable {

    /**
     * Size minimum to build a list to store share runnables
     */
    private static final byte SIZE_LIST_RUNNABLE = 5;

    /**
     * Map contained as key the browse counter, linked with a list of share
     * runnables
     */
    private final Map<Integer, List<ShareRunnable>> indexRunnable;
    /**
     * Browse counter max
     */
    private final double browseCounterMax;
    /**
     * Ticks on which the share runnables work
     */
    private final long ticks;
    /**
     * Time max before stop the runnable
     */
    private final int timeDeleteMax;
    /**
     * Browse counter to perform specific runnables
     */
    private int browseCounter;
    /**
     * Number of share runnable contained in handler
     */
    private int numberRunnable;
    /**
     * Remaining time before stop the runnable
     */
    private int timeDelete;
    /**
     * Know if the runnable is async {@code true} if the runnable is async,
     * {@code false} otherwise
     */
    private boolean async;

    /**
     * Constructor
     *
     * @param ticks Number of ticks when the share runnable will be called to
     *              perform action
     */
    public ShareRunnableHandler(final long ticks) {
        this.ticks = ticks;
        this.indexRunnable = new HashMap<>();
        this.browseCounter = 0;
        final double value = ticks >> 1;
        this.browseCounterMax = value <= 0 ? 0.5 : value;

        this.numberRunnable = 0;
        this.timeDelete = 0;
        this.timeDeleteMax = (int) (ticks / this.browseCounterMax / 20 * 5 * 6000);

        this.async = false;
    }

    /**
     * Get the future value of browse counter
     *
     * @return The future value of browse counter
     */
    private int incrementCounter() {
        final int counter = this.browseCounter + 1;
        return counter >= this.browseCounterMax ? 0 : counter;
    }

    @Override
    public void run() {
        if(this.numberRunnable == 0) {
            this.timeDelete++;
            if(this.timeDelete == this.timeDeleteMax) {
                ShareRunnableManager.cancelAllShareRunnable(this.ticks, !this.async, this.async);
            }
        } else {
            this.performRunnable();
            this.browseCounter = this.incrementCounter();
        }

    }

    /**
     * Perform all actions of runnable contained in list during the browse counter
     */
    private void performRunnable() {
        final List<ShareRunnable> list = this.indexRunnable.get(this.browseCounter);
        if(list == null || list.isEmpty()) {
            return;
        }

        final Iterator<ShareRunnable> it = list.iterator();
        while(it.hasNext()) {
            final ShareRunnable runnable = it.next();
            if(runnable.ended()) {
                it.remove();
                this.numberRunnable--;
            } else {
                runnable.perform();
            }
        }
    }

    /**
     * Add a new Share runnable
     *
     * @param runnable Runnable that will be added
     */
    public void addRunnable(final ShareRunnable runnable) {
        final int indexMap = this.incrementCounter();
        final List<ShareRunnable> list = this.indexRunnable.computeIfAbsent(indexMap, key -> new ArrayList<>(SIZE_LIST_RUNNABLE));
        this.numberRunnable++;
        this.timeDelete = 0;
        list.add(runnable);
    }

    /**
     * <b>Asynchronous tasks should never access any API in Bukkit. Great care
     * should be taken to assure the thread-safety of asynchronous tasks.</b>
     * <p>
     * Schedules this to repeatedly run asynchronously until cancelled, starting
     * after the specified number of server ticks.
     *
     * @param plugin Plugin the reference to the plugin scheduling task
     * @return a BukkitTask that contains the id number
     */
    public synchronized BukkitTask runTaskTimerAsynchronously(final Plugin plugin) {
        this.async = true;
        return super.runTaskTimerAsynchronously(plugin, 0, (long) (this.ticks / this.browseCounterMax));
    }

    /**
     * Schedules this to repeatedly run until cancelled, starting after thespecified
     * number of server ticks.
     *
     * @param plugin Plugin the reference to the plugin scheduling task
     * @return a BukkitTask that contains the id number
     */
    public synchronized BukkitTask runTaskTimer(final Plugin plugin) {
        this.async = false;
        return super.runTaskTimer(plugin, 0, (long) (this.ticks / this.browseCounterMax));
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTaskTimer(final Plugin plugin, final long delay, final long period) {
        return super.runTaskTimer(plugin, delay, period);
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTaskTimerAsynchronously(final Plugin plugin, final long delay, final long period) {
        return super.runTaskTimerAsynchronously(plugin, delay, period);
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTask(final Plugin plugin) {
        return super.runTask(plugin);
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTaskAsynchronously(final Plugin plugin) {
        return super.runTaskAsynchronously(plugin);
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTaskLater(final Plugin plugin, final long delay) {
        return super.runTaskLater(plugin, delay);
    }

    @Deprecated
    @Override
    public synchronized BukkitTask runTaskLaterAsynchronously(final Plugin plugin, final long delay) {
        return super.runTaskLaterAsynchronously(plugin, delay);
    }

    /**
     * Get the number of ticks on which the runnable works
     *
     * @return Get the ticks
     */
    public long getTicks() {
        return this.ticks;
    }
}
