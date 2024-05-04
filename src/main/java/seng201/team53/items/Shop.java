package seng201.team53.items;

import java.util.ArrayList;

public class Shop {
    public ArrayList<Purchasable> items;

    private int money = 100;

    /**
     * Attempt to purchase an item from the shop
     * 
     * @return True if the item was successfuly purchased from the shop, and the
     *         player's money was adjusted. False if the player did not have enough
     *         balance to purchase the item.
     */
    public boolean purchaseItem(Purchasable item) {
        var cost = item.getCostPrice();

        if (cost > this.money)
            return false;

        this.subtractMoney(cost);
        return true;
    }


    private void updateLabels() {
        GameController gameController = GameEnvironment.getGameEnvironment().getController();
        gameController.updateMoneyLabel(this.getMoney());
        gameController.updateShopButtons(this.getMoney());
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
