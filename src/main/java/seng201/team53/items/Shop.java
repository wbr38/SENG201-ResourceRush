package seng201.team53.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Shop {

    private final IntegerProperty moneyProperty = new SimpleIntegerProperty(0);

    public IntegerProperty getMoneyProperty() {
        return moneyProperty;
    }

    public int getMoney() {
        return moneyProperty.get();
    }

    /**
     * Attempt to purchase an item from the shop
     * 
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


    public void sellItem(Purchasable item) {
        int sellPrice = item.getSellPrice();
        this.addMoney(sellPrice);
    }

    /**
     * Subtract an amount of money from the player's balance. The balance will not
     * go below zero.
     */
    public void subtractMoney(int amount) {
        moneyProperty.set(Math.max(0, getMoney() - amount));
    }

    public void addMoney(int amount) {
        moneyProperty.set(moneyProperty.get() + amount);
    }
}
