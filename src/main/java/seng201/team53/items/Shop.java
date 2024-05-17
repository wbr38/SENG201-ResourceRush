package seng201.team53.items;

import seng201.team53.exceptions.ItemNotFoundException;
import seng201.team53.game.GameEnvironment;
import seng201.team53.gui.GameController;

public class Shop {

    private int money = 0;

    /**
     * Attempt to purchase an item from the shop
     * 
     * @return True if the item was successfuly purchased from the shop, and the
     *         player's money was adjusted. False if the player did not have enough
     *         balance to purchase the item.
     */
    public boolean purchaseItem(Purchasable<?> item) {
        int cost = item.getCostPrice();

        if (cost > this.money)
            return false;

        this.subtractMoney(cost);
        return true;
    }


    public void sellItem(Purchasable<?> item) throws ItemNotFoundException {
        int sellPrice = item.getSellPrice();
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
