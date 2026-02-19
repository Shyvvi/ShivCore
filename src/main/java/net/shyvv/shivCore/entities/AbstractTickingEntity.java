package net.shyvv.shivCore.entities;

import io.papermc.paper.datacomponent.item.CustomModelData;
import net.shyvv.shivCore.ShivCoreMain;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractTickingEntity {
    final EntityType baseEntity;
    final String entityID;
    BehaviorController behaviorController = null;

    private final NamespacedKey ownerKey = new NamespacedKey(ShivCoreMain.getInstance(), "OWNER");

    public AbstractTickingEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        this.entityID = entityID;
        this.baseEntity = baseEntity;
        this.behaviorController = behaviorController;
        EntityUtil.TICKING_ENTITIES.add(this);
    }

    public AbstractTickingEntity(EntityType baseEntity, String entityID) {
        this.entityID = entityID;
        this.baseEntity = baseEntity;
        EntityUtil.TICKING_ENTITIES.add(this);
    }

    public abstract void tick(Entity entity);
    protected abstract void spawnArgs(Entity entity);

    public EntityType getBaseEntity() {
        return this.baseEntity;
    }

    public NamespacedKey getEntityID() {
        return new NamespacedKey(ShivCoreMain.getInstance(), this.entityID);
    }

    public BehaviorController getBehaviorController() {
        return this.behaviorController;
    }

    /// Use tick and ignore this. This is just used for taking into account entity behaviors
    public void primaryTick(Entity e) {
        tick(e);
        if(behaviorController != null) {
            behaviorController.tickBehaviors(e);
        }
    }

    public int incrementInt(Entity entity, String id) {
        int i = getInt(entity, id);
        setInt(entity, i+1, id);
        return i;
    }

    public void setInt(Entity entity, int value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER, value);
    }

    @SuppressWarnings("DataFlowIssue")
    public int getInt(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.INTEGER);
        }
        return 0;
    }

    public void setFloat(Entity entity, float value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.FLOAT, value);
    }

    @SuppressWarnings("DataFlowIssue")
    public float getFloat(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.FLOAT);
        }
        return 0;
    }

    public static void setBoolean(Entity entity, boolean value, String id) {
        entity.getPersistentDataContainer().set(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.BOOLEAN, value);
    }

    @SuppressWarnings("DataFlowIssue")
    public static boolean getBoolean(Entity entity, String id) {
        if(entity.getPersistentDataContainer().has(new NamespacedKey(ShivCoreMain.getInstance(), id))) {
            return entity.getPersistentDataContainer().get(new NamespacedKey(ShivCoreMain.getInstance(), id), PersistentDataType.BOOLEAN);
        }
        return false;
    }


    public boolean isOwner(Entity entity, Entity owner) {
        if(entity.getPersistentDataContainer().has(ownerKey)) {
            return owner.getUniqueId().toString().equals(entity.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING));
        }
        return false;
    }

    public void setOwner(Entity entity, Entity owner) {
        entity.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, owner.getUniqueId().toString());
    }

    private <T extends Entity> Entity getOwner(Entity entity, Class<T> ownerEntityClass, boolean ownerIsPlayer) {
        if(entity.getPersistentDataContainer().has(ownerKey)) {
            if(ownerIsPlayer) {
                for(Player e : entity.getWorld().getPlayers()) {
                    if(isOwner(entity, e)) {
                        return e;
                    }
                }
            } else {
                for(Entity e : entity.getWorld().getEntitiesByClass(ownerEntityClass)) {
                    if(isOwner(entity, e)) {
                        return e;
                    }
                }
            }
        }
        return null;
    }
    public <T extends Entity> Entity getOwner(Entity entity, Class<T> ownerEntityClass) {
        return getOwner(entity, ownerEntityClass, false);
    }
    public Player getOwner(Entity entity) {
        return (Player) getOwner(entity, null, true);
    }

    public Collection<Entity> getOwnedEntities(Entity owner, Location loc, double searchDistance) {
        Collection<Entity> collection = new ArrayList<>();
        for(Entity e : owner.getWorld().getNearbyEntities(loc, searchDistance, searchDistance, searchDistance)) {
            if(isOwner(e, owner)) {
                collection.add(e);
            }
        }
        return collection;
    }

    /// Only if the base entity is an itemdisplay
    public void setModelData(Entity e, int cmd) {
        try {
            ItemDisplay i = (ItemDisplay) e;
            ItemStack is = i.getItemStack();
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(cmd);
            is.setItemMeta(im);
            i.setItemStack(is);
        } catch (Exception ignored) {}
    }

    public Entity spawn(Location loc) {
        World world = loc.getWorld();
        assert world != null;
        Entity entity = world.spawnEntity(loc, getBaseEntity());
        spawnArgs(entity);
        entity.getPersistentDataContainer().set(EntityUtil.TICKING_KEY, PersistentDataType.BOOLEAN, true);
        entity.getPersistentDataContainer().set(getEntityID(), PersistentDataType.BOOLEAN, true);
        EntityUtil.beginTickingEntity(entity);
        return entity;
    }

    public Entity spawn(Location loc, Entity owner) {
        Entity e = spawn(loc);
        setOwner(e, owner);
        return e;
    }


    public void move(Entity entity, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(entity.getLocation(), distance, 0, 0));
    }

    public void move(Entity entity, Location startPos, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(startPos, distance, 0, 0));
    }

    public void init(){}
}
