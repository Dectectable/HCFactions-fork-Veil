package net.bfcode.bfhcf.faction.event;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import net.bfcode.bfhcf.faction.type.Faction;

import org.bukkit.event.Cancellable;

public class FactionCreateEvent extends FactionEvent implements Cancellable
{
    private static HandlerList handlers;
    private CommandSender sender;
    private boolean cancelled;
    
    public FactionCreateEvent(Faction faction, CommandSender sender) {
        super(faction);
        this.sender = sender;
    }
    
    public static HandlerList getHandlerList() {
        return FactionCreateEvent.handlers;
    }
    
    public CommandSender getSender() {
        return this.sender;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return FactionCreateEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
