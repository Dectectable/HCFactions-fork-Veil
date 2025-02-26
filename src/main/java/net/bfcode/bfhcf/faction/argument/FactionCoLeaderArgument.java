package net.bfcode.bfhcf.faction.argument;

import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.bfcode.bfbase.util.command.CommandArgument;
import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.faction.FactionMember;
import net.bfcode.bfhcf.faction.struct.Role;
import net.bfcode.bfhcf.faction.type.PlayerFaction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FactionCoLeaderArgument extends CommandArgument
{
    private HCFaction plugin;
    
    public FactionCoLeaderArgument(HCFaction plugin) {
        super("coleader", "Sets an member as an coleader.");
        this.plugin = plugin;
        this.aliases = new String[] { "coleader", "colead", "coleaderr" };
    }
    
    public String getUsage(String label) {
        return '/' + label + ' ' + this.getName() + " <playerName>";
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can set faction leaders.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        UUID uuid = player.getUniqueId();
        FactionMember selfMember = playerFaction.getMember(uuid);
        Role selfRole = selfMember.getRole();
        if (selfRole != Role.LEADER) {
            sender.sendMessage(ChatColor.RED + "You must be an leader to assign the coleader role to an member.");
            return true;
        }
        @SuppressWarnings("deprecation")
		FactionMember targetMember = playerFaction.getMember(args[1]);
        if (targetMember == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' is not in your faction.");
            return true;
        }
        if (targetMember.getRole().equals(Role.COLEADER)) {
            sender.sendMessage(ChatColor.RED + "This member is already a co-leader!");
            return true;
        }
        if (targetMember.getUniqueId().equals(uuid)) {
            sender.sendMessage(ChatColor.RED + "You are the leader, which means you cannot co-leader yourself.");
            return true;
        }
        targetMember.setRole(Role.COLEADER);
        playerFaction.broadcast(ChatColor.GREEN + targetMember.getName() + ChatColor.YELLOW + " has been promoted to a co leader.");
        return true;
    }
    
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player)sender;
        PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
        if (playerFaction == null || playerFaction.getMember(player.getUniqueId()).getRole() != Role.COLEADER) {
            return Collections.emptyList();
        }
        List<String> results = new ArrayList<String>();
        Map<UUID, FactionMember> members = playerFaction.getMembers();
        for (Map.Entry<UUID, FactionMember> entry : members.entrySet()) {
            if (entry.getValue().getRole() != Role.LEADER) {
                OfflinePlayer target = Bukkit.getOfflinePlayer((UUID)entry.getKey());
                String targetName = target.getName();
                if (targetName == null || results.contains(targetName)) {
                    continue;
                }
                results.add(targetName);
            }
        }
        return results;
    }
}
