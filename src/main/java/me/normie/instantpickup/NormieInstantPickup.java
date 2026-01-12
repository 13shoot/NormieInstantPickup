package me.normie.instantpickup;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class NormieInstantPickup extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("NormieInstantPickup enabled");
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {
        Block block = event.getBlock();
        BlockState state = block.getState();
        boolean isContainer = state instanceof InventoryHolder;

        Inventory inv = event.getPlayer().getInventory();
        Location loc = event.getPlayer().getLocation();

        event.getItems().forEach(item -> {
            ItemStack stack = item.getItemStack();

            // Non-container blocks → instant pickup
            if (!isContainer) {
                if (inv.firstEmpty() != -1) {
                    inv.addItem(stack);
                } else {
                    loc.getWorld().dropItemNaturally(loc, stack);
                }
                item.remove();
                return;
            }

            // Container blocks:
            // - block item → instant pickup
            // - contents → vanilla drop
            if (stack.getType() == block.getType()) {
                if (inv.firstEmpty() != -1) {
                    inv.addItem(stack);
                    item.remove();
                }
            }
        });
    }
}
