package com.hooklite.endshop.events;

import com.hooklite.endshop.data.rewards.Action;
import com.hooklite.endshop.holders.PluginHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class TransactionEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ItemStack ITEM;
    private final ItemStack CLICKED_ITEM;
    private final Player PLAYER;
    private final Action ACTION;
    private final PluginHolder HOLDER;

    public TransactionEvent(ItemStack rewardItem, ItemStack clickedItem, Player player, PluginHolder holder, Action action) {
        ITEM = rewardItem;
        CLICKED_ITEM = clickedItem;
        PLAYER = player;
        ACTION = action;
        HOLDER = holder;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Action getAction() {
        return ACTION;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ItemStack getItem() {
        return ITEM;
    }

    public Player getWhoClicked() {
        return PLAYER;
    }

    public ItemStack getClickedItem() {
        return CLICKED_ITEM;
    }

    public PluginHolder getHolder() {
        return HOLDER;
    }
}
