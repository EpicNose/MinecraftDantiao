package com.valorin.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ArenaEventAbs extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
