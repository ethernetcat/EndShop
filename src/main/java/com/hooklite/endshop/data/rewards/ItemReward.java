package com.hooklite.endshop.data.rewards;

import com.hooklite.endshop.data.models.Item;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemReward implements Reward {
    private Material rewardMaterial;
    private int configAmount;

    @Override
    public boolean execute(Item eItem, Player player, int amount) {
        ItemStack reward = new ItemStack(rewardMaterial);
        Inventory playerInventory = player.getInventory();

        if(reward.getMaxStackSize() > 1) {
            if(playerInventory.containsAtLeast(reward, 1)) {
                for(ItemStack item : playerInventory) {
                    if(item != null && item.getAmount() < item.getMaxStackSize() && reward.isSimilar(item)) {
                        if(item.getAmount() < reward.getMaxStackSize() - amount + 1) {
                            addItems(player, reward, amount * configAmount);
                            return true;
                        }
                    }
                }
            }
            if(getEmptySlots(player) >= Math.ceil((double) (amount * configAmount) / reward.getMaxStackSize())) {
                addItems(player, reward, amount * configAmount);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            if(getEmptySlots(player) >= amount * configAmount) {
                addItems(player, reward, amount * configAmount);
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public String getReward(int amount) {
        String materialName = rewardMaterial.name();
        materialName = materialName.replace("_", " ").toLowerCase();

        return String.format("%sx %s", amount * configAmount, materialName.substring(0, 1).toUpperCase() + materialName.substring(1));
    }

    @Override
    public String getFailedMessage() {
        return "You do not have enough inventory space!";
    }

    @Override
    public void setReward(String reward) throws InvalidConfigurationException {
        Material material = Material.matchMaterial(reward);

        if(material == null)
            throw new InvalidConfigurationException("Reward improperly configured!");

        this.rewardMaterial = Material.matchMaterial(reward);
    }

    @Override
    public int getConfigAmount() {
        return configAmount;
    }

    @Override
    public int getMaxAmount(Player player) {
        ItemStack reward = new ItemStack(rewardMaterial);
        Inventory inventory = player.getInventory();
        int amount = 0;

        if(reward.getMaxStackSize() > 1) {
            if(inventory.containsAtLeast(reward, 1)) {
                int stackAmount = 0;

                for(ItemStack item : inventory.getContents()) {
                    if(item != null && item.getType().equals(rewardMaterial)) {
                        stackAmount += item.getAmount();
                    }
                }

                amount += 64 - (stackAmount % reward.getMaxStackSize()) != 64 ? 64 - (stackAmount % reward.getMaxStackSize()) : 0;
                amount += getEmptySlots(player) * reward.getMaxStackSize();

                return amount / configAmount;
            }

            return (getEmptySlots(player) * reward.getMaxStackSize()) / configAmount;
        }

        return getEmptySlots(player);
    }

    @Override
    public void setAmount(int amount) {
        configAmount = amount;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public Reward getInstance() {
        return new ItemReward();
    }

    private void addItems(Player player, ItemStack reward, int amount) {
        for(int i = 0; i < amount; i++) {
            player.getInventory().addItem(reward);
        }
    }

    private int getEmptySlots(Player player) {
        int amount = 0;
        Inventory inventory = player.getInventory();

        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();

        for(int i = 0; i < inventory.getContents().length; i++) {
            ItemStack currentItem = inventory.getContents()[i];

            if(currentItem == null)
                amount++;
            else {
                if(currentItem == helmet || currentItem == chestplate || currentItem == leggings || currentItem == boots)
                    amount--;
            }
        }

        for(ItemStack ignored : player.getInventory().getExtraContents()) {
            amount--;
        }

        if(helmet == null)
            amount--;
        if(chestplate == null)
            amount--;
        if(leggings == null)
            amount--;
        if(boots == null)
            amount--;

        return amount;
    }
}
