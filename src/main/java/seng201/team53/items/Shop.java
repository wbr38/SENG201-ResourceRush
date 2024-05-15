package seng201.team53.items;

import seng201.team53.exceptions.ItemNotFoundException;
import seng201.team53.game.GameEnvironment;
import seng201.team53.gui.GameController;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private int money = 10000; // todo - temp just setting this high for testing

    public List<Purchasable> inventory = new ArrayList<>();

    public boolean canPurchaseItem(Purchasable item){
        var cost = item.getCostPrice();
        return cost < this.money;
    }

    /**
     * Attempt to purchase an item from the shop
     * 
     * @return True if the item was successfuly purchased from the shop, and the
     *         player's money was adjusted. False if the player did not have enough
     *         balance to purchase the item.
     */
    public void purchaseItem(Purchasable item) {
        var cost = item.getCostPrice();
        this.subtractMoney(cost);
        this.inventory.add(item);
    }

    public void sellItem(Purchasable item) throws ItemNotFoundException {
        if (!this.inventory.contains(item)) {
            throw new ItemNotFoundException("Tried to sell a " + item.getName() + " item that we do not own!");
        }

        int sellPrice = item.getSellPrice();
        this.inventory.remove(item);
        this.addMoney(sellPrice);
    }

    /**
     * Update the GUI labels and shop items for the current value of `money`.
     */
    private void updateLabels() {
        GameController gameController = GameEnvironment.getGameEnvironment().getController();
        int money = this.getMoney();
        gameController.updateMoneyLabel(money);
        gameController.updateShopButtons(money);
    }

    public int getMoney() {
        return this.money;
    }

    /**
     * Subtract an amount of money from the player's balance. The balance will not
     * go below zero.
     */
    public void subtractMoney(int amount) {
        this.money -= amount;
        if (this.money < 0)
            this.money = 0;

        this.updateLabels();
    }

    public void addMoney(int amount) {
        this.money += amount;
        this.updateLabels();
    }
}
