package net.bfcode.bfhcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.bfcode.bfhcf.utils.ConfigurationService;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;

public class SkullListener implements Listener
{
    private static String KILL_BEHEAD_PERMISSION = "hcf.kill.behead";
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (ConfigurationService.KIT_MAP) {
            return;
        }
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer != null && killer.hasPermission("hcf.kill.behead")) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
            SkullMeta meta = (SkullMeta)skull.getItemMeta();
            meta.setOwner(player.getName());
            skull.setItemMeta((ItemMeta)meta);
            event.getDrops().add(skull);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            BlockState state = event.getClickedBlock().getState();
            if (state instanceof Skull) {
                Skull skull;
                player.sendMessage(ChatColor.YELLOW + "This head belongs to " + ChatColor.WHITE + (((skull = (Skull)state).getSkullType() == SkullType.PLAYER && skull.hasOwner()) ? skull.getOwner() : ("a " + WordUtils.capitalizeFully(skull.getSkullType().name()) + " skull")) + ChatColor.YELLOW + '.');
            }
        }
    }
}
