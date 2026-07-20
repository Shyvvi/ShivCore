package net.shyvv.shivCore.item;

import net.shyvv.shivCore.ShivCore;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent; // ADDED: Required import for custom crafting
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory; // ADDED: Required import for crafting inventory
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;

public abstract class SCItem implements Listener {
    ItemStack defaultItemStack;
    String itemID;
    String[][] recipeMatrix;

    public SCItem(ItemStack defaultItemStack, String itemID) {
        this.defaultItemStack = ItemStackBuilder.setItemStackData(defaultItemStack, "itemID", PersistentDataType.STRING, itemID);
        this.itemID = itemID;
        ItemManager.CUSTOM_ITEMS.add(this);
        System.out.println("Registering item "+itemID);
        ShivCore.getInstance().getServer().getPluginManager().registerEvents(this, ShivCore.getInstance());
    }

    public SCItem(ItemStack defaultItemStack, String itemID, String[][] recipeMatrix) {
        this(defaultItemStack, itemID);
        this.recipeMatrix = recipeMatrix;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        if (this.recipeMatrix != null) {
            CraftingInventory inv = e.getInventory();
            ItemStack[] matrix = inv.getMatrix();
            if (matrix.length == 9) {
                boolean matches = true;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int index = (i * 3) + j;
                        String required = "";
                        if (this.recipeMatrix.length > i) {
                            if (this.recipeMatrix[i] != null && this.recipeMatrix[i].length > j) {
                                if (this.recipeMatrix[i][j] != null) {
                                    required = this.recipeMatrix[i][j];
                                }
                            }
                        }
                        ItemStack currentItem = matrix[index];
                        String currentID = "";
                        if (currentItem != null) {
                            if (!currentItem.getType().isAir()) {
                                boolean foundCustom = false;
                                ItemMeta meta = currentItem.getItemMeta();
                                if (meta != null) {
                                    String customID = meta.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING);
                                    if (customID != null) {
                                        currentID = customID;
                                        foundCustom = true;
                                    }
                                }

                                if (!foundCustom) {
                                    if (required.contains(":")) {
                                        currentID = currentItem.getType().getKey().toString();
                                    } else {
                                        currentID = currentItem.getType().name();
                                    }
                                }
                            }
                        }

                        if (!required.equalsIgnoreCase(currentID)) {
                            matches = false;
                        }
                    }
                }

                if (matches) {
                    inv.setResult(this.defaultItemStack);
                }
            }
        }
    }

    public ItemStack getDefaultItemStack() {
        return defaultItemStack;
    }

    public String getItemID() {
        ItemMeta im = defaultItemStack.getItemMeta();
        return im != null ? im.getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING) : "";
    }

    public boolean matchesItemID(ItemStack is) {
        if(is != null) {
            String itemStackID = is.getItemMeta() != null ? is.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(ShivCore.getInstance(), "itemID"), PersistentDataType.STRING) : null;
            return itemStackID != null && itemStackID.equals(itemID);
        }
        return false;
    }
}