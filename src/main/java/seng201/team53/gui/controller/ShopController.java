package seng201.team53.gui.controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.Shop;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.upgrade.UpgradeItem;

import java.util.HashMap;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * This class is a sub-controller of the GameController and is responsible for handing events in the shop
 */
public class ShopController {
    private final GameController gameController;
    private final Map<Button, Purchasable> shopButtons = new HashMap<>();

    /**
     * Constructs a new shop controller with a reference to the main game controller
     * @param gameController The game controller
     */
    public ShopController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Initialises the shop controller.
     * This method adds all the shop buttons in the graphical user to a map which maps each button to a specific
     * purchasable item. Each button is updated and a click event is added.
     * A change listener is added to the money property which will update the money label.
     */
    public void init() {
        shopButtons.put(gameController.shopTowerButton1, Tower.Type.LUMBER_MILL);
        shopButtons.put(gameController.shopTowerButton2, Tower.Type.MINE);
        shopButtons.put(gameController.shopTowerButton3, Tower.Type.QUARRY);
        shopButtons.put(gameController.shopTowerButton4, Tower.Type.WINDMILL);
        shopButtons.put(gameController.shopItemButton1, UpgradeItem.Type.REPAIR_TOWER);
        shopButtons.put(gameController.shopItemButton2, UpgradeItem.Type.TEMP_FASTER_TOWER_RELOAD);
        shopButtons.put(gameController.shopItemButton3, UpgradeItem.Type.TEMP_SLOWER_CART);
        shopButtons.put(gameController.shopItemButton4, UpgradeItem.Type.FILL_CART);
        shopButtons.forEach((button, purchasable) -> {
            gameController.updateButton(button, purchasable);
            button.setOnMouseClicked(event -> onShopButtonClick(event, purchasable));
        });

        gameController.sellItemPane.setOnMousePressed(this::onSellTowerButtonClick);

        Shop shop = getGameEnvironment().getShop();
        this.updateGUI(shop.getMoney());
        shop.getMoneyProperty().addListener(($, oldValue, newValue) -> this.updateGUI(newValue.intValue()));
    }

    /**
     * Update the GUI labels and shop items for the current value of `money`.
     */
    private void updateGUI(int money) {
        gameController.moneyLabel.setText("$" + money);
        shopButtons.forEach((button, towerType) -> {
            int cost = towerType.getCostPrice();
            button.setOpacity(cost > money ? 0.5 : 1.0);
        });
    }

    /**
     * Handles when a shop button is clicked
     * @param event The mouse event
     * @param purchasable The purchasable item attached to the clicked button
     */
    private void onShopButtonClick(MouseEvent event, Purchasable purchasable) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        gameController.getMapInteractionController().tryPurchaseItem(purchasable);
    }

    /**
     * Handles when the sell item button is clicked.
     * This calls the map interaction controller to handle selling a selected item and hides the sell item popup
     * @param event The mouse event
     */
    private void onSellTowerButtonClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        gameController.showSellItemPopup(null);
        gameController.getMapInteractionController().sellSelectedItem();
    }
}
