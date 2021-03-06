package com.hooklite.endshop.config;

import com.hooklite.endshop.data.items.*;
import com.hooklite.endshop.data.models.Item;
import com.hooklite.endshop.data.models.Page;
import com.hooklite.endshop.data.models.Shop;
import com.hooklite.endshop.holders.ItemMenuHolder;
import com.hooklite.endshop.holders.ShopMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class InventoryFactory {

    /**
     * Creates a new inventory then loads in data from the shop.
     *
     * @param shops A list of shops that the data will be read from.
     * @return The inventory containing all the shop data.
     */
    public static Inventory getShopMenu(List<Shop> shops, Player player) {
        Inventory inventory = Bukkit.createInventory(new ShopMenuHolder(), getSize(shops.size()), ChatColor.DARK_GRAY + "Shops");

        // Sets the shop display items
        for(Shop shop : shops) {
            inventory.setItem(shop.slot, shop.displayItem);
        }

        inventory.setItem(inventory.getSize() - 5, BalanceItem.get(player));

        return inventory;
    }

    /**
     * Creates an inventory or page of items from the given page.
     *
     * @param page       The page that contains the items.
     * @param pageAmount The amount of pages.
     * @return An inventory containing the page items.
     */
    public static Inventory getPageInventory(Shop shop, Page page, int pageAmount) {
        int inventorySize = getSize(page.getItems().size());
        Inventory inventory = Bukkit.createInventory(new ItemMenuHolder(shop, page), inventorySize, shop.title);

        // Sets the inventory items
        for(int i = 0; i < page.getItems().size(); i++) {
            inventory.setItem(i, page.getItems().get(i).displayItem);
        }

        // Adds navigation elements to the inventory
        if(pageAmount > 1) {
            inventory.setItem(inventorySize - 6, PreviousPageItem.get());
            inventory.setItem(inventorySize - 4, NextPageItem.get());
        }
        inventory.setItem(inventorySize - 9, BackItem.get());
        inventory.setItem(inventorySize - 5, PageNumberItem.get(shop.pages.size(), pageAmount));

        return inventory;
    }

    /**
     * Calculates the size of the inventory from the amount of items.
     *
     * @return The size of the inventory.
     */
    private static int getSize(int itemAmount) {
        int size;

        if(itemAmount <= 9)
            size = 18;
        else if(itemAmount <= 18)
            size = 27;
        else if(itemAmount <= 27)
            size = 36;
        else if(itemAmount <= 36)
            size = 45;
        else if(itemAmount <= 45)
            size = 54;
        else
            throw new IllegalArgumentException("An inventory cannot contain more that 45 items!");

        return size;
    }
}
