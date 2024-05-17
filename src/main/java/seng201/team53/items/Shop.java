package seng201.team53.items;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import seng201.team53.exceptions.ItemNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final IntegerProperty moneyProperty = new SimpleIntegerProperty(100);

    public List<Purchasable> inventory = new ArrayList<>();

    public IntegerProperty getMoneyProperty() {
        return moneyProperty;
    }

    public int getMoney() {
        return moneyProperty.get();
    }

    /**
     * Subtract an amount of money from the player's balance. The balance will not
     * go below zero.
     */
    public void subtractMoney(int amount) {
        moneyProperty.set(Math.max(0, getMoney() - amount));
    }

    public void addMoney(int amount) {
        moneyProperty.add(amount);
    }

    public boolean canPurchaseItem(Purchasable item){
        return item.getCostPrice() <= getMoney();
    }

    /**
     * Attempt to purchase an item from the shop
     * 
     * @return True if the item was successfuly purchased from the shop, and the
     *         player's money was adjusted. False if the player did not have enough
     *         balance to purchase the item.
     */
    public void purchaseItem(Purchasable item) {
        subtractMoney(item.getCostPrice());
        inventory.add(item);
    }

    public void sellItem(Purchasable item) throws ItemNotFoundException {
        if (!inventory.contains(item)) {
            throw new ItemNotFoundException("Tried to sell a " + item.getName() + " item that we do not own!");
        }
        addMoney(item.getSellPrice());
        inventory.remove(item);
    }
}
