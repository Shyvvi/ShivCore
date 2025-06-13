package net.shyvv.shivCore.entities.behavior;

import net.shyvv.shivCore.ShivCoreMain;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Behavior {
    private final JavaPlugin plugin = ShivCoreMain.getInstance();

    public abstract void tick(Entity e);
    public abstract String behaviorID();
    /// ignoresBreaks controls whether this behavior shouldn't break the for loop when iterating through behaviors
    public abstract boolean ignoresBreaks();

    public boolean isActive(Entity e) {
        return e.hasMetadata(behaviorID());
    }

    public void setActive(Entity e, boolean active) {
        if(active) {
            e.setMetadata(behaviorID(), new FixedMetadataValue(plugin, true));
        } else if(e.hasMetadata(behaviorID())) {
            e.removeMetadata(behaviorID(), plugin);
        }
    }
}
