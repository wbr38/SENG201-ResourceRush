package seng201.team53.game.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class represents the shop. It stores the players money, determines if an item can be bought and handles
 * selling of items
 */
public class Shop {
    private final IntegerProperty moneyProperty = new SimpleIntegerProperty(0);

    /**
     * Retrieves the money property.
     * This property is observable, meaning it can be watched for changes
     * @return The money property
     */
    public IntegerProperty getMoneyProperty() {
        return moneyProperty;
    }

    /**
     * Retrieves the players money
     * @return The amount of money the player has
     */
    public int getMoney() {
        return moneyProperty.get();
    }

    /**
     * Attempt to purchase an item from the shop
     * @param item The item to be purchased
     * @return True if the item was successfuly purchased from the shop, and the
     *         player's money was adjusted. False if the player did not have enough
     *         balance to purchase the item.
     */
    public boolean purchaseItem(Purchasable item) {
        int cost = item.getCostPrice();

        if (cost > this.getMoney())
            return false;

        this.subtractMoney(cost);
        return true;
    }

    /**
     * Sells a purchasable item and adds the sell price to the players balance
     * @param item The item to sell
     */
    public void sellItem(Purchasable item) {
        int sellPrice = item.getSellPrice();
        this.addMoney(sellPrice);
    }

    /**
     * Subtract an amount of money from the player's balance. The balance will not
     * go below zero.
     * @param amount The amount of money to subtract
     */
    public void subtractMoney(int amount) {
        moneyProperty.set(Math.max(0, getMoney() - amount));
    }

    /**
     * Increases the player's balance by a given amount
     * @param amount The amount to increase the players balance
     */
    public void addMoney(int amount) {
        moneyProperty.set(moneyProperty.get() + amount);
    }
}
