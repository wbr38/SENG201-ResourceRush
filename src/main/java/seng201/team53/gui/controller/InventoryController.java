package seng201.team53.gui.controller;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.items.Item;
import seng201.team53.items.towers.Tower;

public class InventoryController {
    private final GameController gameController;
    private final MapInteractionController interactionController;

    // A mapping of inventory tower buttons -> tower (or null)
    private final Map<Button, Tower> reserveTowers = new HashMap<>();

    public InventoryController(GameController gameController, MapInteractionController interactionController) {
        this.gameController = gameController;
        this.interactionController = interactionController;
    }

    public void init() {
        var inventoryButtons = Arrays.asList(gameController.inventoryButton1,
                                             gameController.inventoryButton2,
                                             gameController.inventoryButton3,
                                             gameController.inventoryButton4);
        inventoryButtons.forEach(button -> {
            button.setOnMouseClicked(event -> onInventoryButtonClick(event, button));
            gameController.updateButton(button, null);
        });
    }

    private void placeTowerIntoInventory(Tower tower, Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);

        // Button is already occupied
        if (occupiedTower != null) {
            GameEnvironment.getGameEnvironment().getController().showNotification("Inventory space already in use", 1);
            return;
        }

        // Place tower into inventory
        gameController.updateButton(towerButton, tower.getPurchasableType());
        reserveTowers.put(towerButton, tower);

        // Tower was previously being selected/moved, and is now placed into inventory
        interactionController.stopPlacingItem();
    }

    private void retrieveTower(Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower == null)
            return;

        reserveTowers.put(towerButton, null);
        gameController.updateButton(towerButton, null);
        interactionController.startPlacingItem(occupiedTower);

        GameMap map = getGameEnvironment().getMap();
        map.setInteraction(MapInteraction.PLACE_TOWER);
    }

    private void onInventoryButtonClick(MouseEvent event, Button towerButton) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Item selectedItem = interactionController.getSelectedItem();
        if (selectedItem == null) {
            // User clicked inventory button with no tower selected
            // They are trying to retrieve a tower from inventory
            this.retrieveTower(towerButton);
            return;
        }

        if (!(selectedItem instanceof Tower)) {
            GameEnvironment.getGameEnvironment().getController().showNotification("Only towers may be placed into inventory", 1.0f);
            return;
        }

        Tower selectedTower = (Tower)selectedItem;

        // User has a tower selected, they are trying to place a tower into inventory
        this.placeTowerIntoInventory(selectedTower, towerButton);
        return;
    }
}
