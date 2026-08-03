package net.shyvv.shivCore.entities;

import net.shyvv.shivCore.ShivCore;
import net.shyvv.shivCore.entities.behavior.BehaviorController;
import net.shyvv.shivCore.util.EntityManager;
import net.shyvv.shivCore.util.EntityUtil;
import net.shyvv.shivCore.util.MiscUtil;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class AbstractTickingEntity implements Listener {
    final EntityType baseEntity;
    final String entityID;
    BehaviorController behaviorController = null;

    private final NamespacedKey ownerKey = new NamespacedKey(ShivCore.getInstance(), "OWNER");

    public AbstractTickingEntity(EntityType baseEntity, String entityID, BehaviorController behaviorController) {
        this.entityID = entityID;
        this.baseEntity = baseEntity;
        this.behaviorController = behaviorController;
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(this, ShivCore.getInstance());
        EntityManager.TICKING_ENTITY_LOOKUP.put(entityID, this);
        EntityManager.TICKING_ENTITIES.add(this);
    }

    public AbstractTickingEntity(EntityType baseEntity, String entityID) {
        this(baseEntity, entityID, null);
    }

    public abstract void tick(Entity entity);
    protected abstract void spawnArgs(Entity entity);

    public EntityType getBaseEntity() {
        return this.baseEntity;
    }

    public String getEntityID() {
        return this.entityID;
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

    public boolean isOwner(Entity entity, Entity owner) {
        if(entity != null && owner != null && entity.getPersistentDataContainer().has(ownerKey)) {
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
        EntityUtil.setBoolean(entity, true, EntityManager.TICKING_KEY);
        EntityUtil.setString(entity, getEntityID(), "ENTITY_IDENTIFICATION");
        EntityManager.beginTickingEntity(entity, this);
        return entity;
    }

    public Entity spawn(Location loc, Entity owner) {
        Entity e = spawn(loc);
        setOwner(e, owner);
        return e;
    }

    /**
     * Spawns an entity with duplicate
     * @param loc spawn location
     * @param isDuplicate if the entity spawns with a duplicate or not
     * @return duplicate entity
     */
    public Entity spawn(Location loc, boolean isDuplicate) {
        String id = UUID.randomUUID().toString();
        EntityUtil.setString(spawn(loc), id, "dupeTarget");
        Entity e = spawn(loc);
        EntityUtil.setString(e, id, "dupeTarget");
        EntityUtil.setBoolean(e, isDuplicate, "isDuplicate");
        return e;
    }

    public boolean isDuplicate(Entity entity) {
        return EntityUtil.getBoolean(entity, "isDuplicate");
    }

    @ApiStatus.OverrideOnly
    public void moveArgs(Entity entity) {}

    public void move(Entity entity, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(entity.getLocation(), distance, 0, 0));
        moveArgs(entity);
    }

    public void move(Entity entity, Location startPos, double distance) {
        entity.teleport(MiscUtil.getRelativeLocation(startPos, distance, 0, 0));
        moveArgs(entity);
    }

    public void init(){}
}
