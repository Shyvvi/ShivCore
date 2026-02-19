package net.shyvv.shivCore.entities;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractEntityless {
    /// Location of the entityless
    public Location location;
    public AbstractEntityless(Location loc) {
        this.location = loc;
    }

    /// How long the runnable lasts for
    public abstract int lifetime();
    /// How long the runnable is delayed and how frequently it is ticked
    public abstract int initDelay();
    public abstract int period();

    /// stopArgs is a boolean which determines whether the runnable is cancelled prematurely
    public abstract boolean stopArgs(Location loc);

    /// endArgs is called when the runnable is canceled
    public abstract void endArgs(Location loc);

    /// initArgs is called when the runnable is initiated
    public abstract void initArgs(Location loc);

    /// Main ticking function
    public abstract void tick(Location loc);

    /// Call this to create a new instance
    public void init() {
        initArgs(location);
        new BukkitRunnable() {
            int life = 0;
            @Override
            public void run() {
                tick(location);
                life++;
                if(life>lifetime() || stopArgs(location)) {
                    endArgs(location);
                    this.cancel();
                }
            }
        }.runTaskTimer(ShivCore.getInstance(), initDelay(), period());
    }
}
