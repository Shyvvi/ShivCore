package net.shyvv.shivCore.entities;

import net.kyori.adventure.text.Component;
import net.shyvv.shivCore.util.EntityUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class RopeSegment extends AbstractTickingEntity {
    private final ItemStack display;
    public RopeSegment(String entityID, ItemStack display) {
        super(EntityType.ITEM_DISPLAY, entityID);
        this.display = display;
    }

    @Override
    public void tick(Entity entity) {
        Entity owner = entity.getWorld().getEntity(UUID.fromString(EntityUtil.getString(entity, "ropeAnchoringEntity")));
        if(owner == null || !owner.isValid() || owner.isDead()) entity.remove();
    }

    @Override
    protected void spawnArgs(Entity entity) {
        ItemDisplay id = (ItemDisplay) entity;
        id.setTeleportDuration(1);
        id.setItemStack(display);
    }
}
