package seng201.team53.gui;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.towers.Tower;

public class InventoryController {

    // A mapping of inventory tower buttons -> tower (or null)
    private Map<Button, Tower> reserveTowers = new HashMap<>();

    public void handleInventoryTowerClick(Button towerButton) {
        MapInteractionController interactionController = GameEnvironment.getGameEnvironment().getController().getMapInteractionController();
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
        MapInteractionController interactionController = GameEnvironment.getGameEnvironment().getController().getMapInteractionController();
        interactionController.stopMovingTower();
        return;
    }

    private void retrieveTower(Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower == null)
            return;

        reserveTowers.put(towerButton, null);
        TowerButton.changeTower(towerButton, null);
        MapInteractionController interactionController = GameEnvironment.getGameEnvironment().getController().getMapInteractionController();
        interactionController.startMovingTower(occupiedTower);
    }
}
