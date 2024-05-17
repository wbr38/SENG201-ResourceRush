package seng201.team53.gui.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

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
        gameController.updateButton(towerButton, tower.getType());
        reserveTowers.put(towerButton, tower);

        // Tower was previously being selected/moved, and is now placed into inventory

        interactionController.stopFollowingMouse();
    }

    private void retrieveTower(Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower == null)
            return;

        reserveTowers.put(towerButton, null);
        gameController.updateButton(towerButton, null);
        interactionController.startMovingTower(occupiedTower);
        interactionController.startFollowingMouse(occupiedTower.getImageView());
    }

    private void onInventoryButtonClick(MouseEvent event, Button towerButton) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Tower selectedTower = interactionController.getSelectedTower();

        // User has a tower selected, they are trying to place a tower into inventory
        if (selectedTower != null) {
            this.placeTowerIntoInventory(selectedTower, towerButton);
            return;
        }

        // User clicked inventory button with no tower selected
        // They are trying to retrieve a tower from inventory
        this.retrieveTower(towerButton);
    }
}
