package seng201.team53.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.items.Purchasable;
import seng201.team53.items.towers.TowerType;
import seng201.team53.items.upgrade.UpgradeItem;

import java.util.HashMap;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class ShopController {
    private final GameController gameController;
    private final Map<Button, Purchasable> shopButtons = new HashMap<>();

    public ShopController(GameController gameController) {
        this.gameController = gameController;
    }

    public void init() {
        shopButtons.put(gameController.shopTowerButton1, TowerType.LUMBER_MILL);
        shopButtons.put(gameController.shopTowerButton2, TowerType.MINE);
        shopButtons.put(gameController.shopTowerButton3, TowerType.QUARRY);
        shopButtons.put(gameController.shopTowerButton4, TowerType.WIND_MILL);
        shopButtons.put(gameController.shopItemButton1, UpgradeItem.Type.REPAIR_TOWER);
        shopButtons.put(gameController.shopItemButton2, UpgradeItem.Type.TEMP_FASTER_TOWER_RELOAD);
        shopButtons.put(gameController.shopItemButton3, UpgradeItem.Type.TEMP_SLOWER_CART);
        shopButtons.put(gameController.shopItemButton4, UpgradeItem.Type.FILL_CART);
        shopButtons.forEach((button, purchasable) -> {
            gameController.updateButton(button, purchasable);
            button.setOnMouseClicked(event -> onShopButtonClick(event, purchasable));
        });

        gameController.sellTowerPane.setOnMousePressed(this::onSellTowerButtonClick);

        var shop = getGameEnvironment().getShop();
        shop.getMoneyProperty().addListener(($, oldValue, newValue) -> {
            int money = newValue.intValue();
            gameController.moneyLabel.setText("$" + money);
            shopButtons.forEach((button, towerType) -> {
                int cost = towerType.getCostPrice();
                button.setOpacity(cost > money ? 0.5 : 1.0);
            });
        });
    }

    private void onShopButtonClick(MouseEvent event, Purchasable purchasable) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        gameController.getMapInteractionController().tryPurchase(purchasable);
    }

    private void onSellTowerButtonClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        gameController.showSellTowerPopup(null);
    }
}
