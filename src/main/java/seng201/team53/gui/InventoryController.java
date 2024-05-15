package seng201.team53.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.towers.Tower;

public class InventoryController {
    private final GameController gameController;
    private final MapInteractionController interactionController;
    // A mapping of inventory tower buttons -> tower (or null)
    private Map<Button, Tower> reserveTowers = new HashMap<>();

    public InventoryController(GameController gameController, MapInteractionController interactionController) {
        this.gameController = gameController;
        this.interactionController = interactionController;
    }

    public void handleInventoryTowerClick(Button towerButton) {
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

    private void placeTowerIntoInventory(Tower tower, Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);

        // Button is already occupied
        if (occupiedTower != null) {
            GameEnvironment.getGameEnvironment().getController().showNotification("Inventory space already in use", 1);
            return;
        }

        // Place tower into inventory
        TowerButton.changeTower(towerButton, tower.getType());
        reserveTowers.put(towerButton, tower);

        // Tower was previously being selected/moved, and is now placed into inventory

        interactionController.stopFollowingMouse();
    }

    private void retrieveTower(Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower == null)
            return;

        reserveTowers.put(towerButton, null);
        TowerButton.changeTower(towerButton, null);
        interactionController.startMovingTower(occupiedTower);
        interactionController.startFollowingMouse(occupiedTower.getImageView());
    }
}
