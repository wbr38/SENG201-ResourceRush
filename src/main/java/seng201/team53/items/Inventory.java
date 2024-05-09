package seng201.team53.items;

import java.util.HashMap;
import java.util.Collection;
import java.util.Objects;

import javafx.scene.control.Button;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.Map;
import seng201.team53.gui.TowerButton;
import seng201.team53.items.towers.Tower;

public class Inventory {

    // A mapping of inventory tower buttons -> tower (or null)
    private java.util.Map<Button, Tower> reserveTowers = new HashMap<>();

    public void handleInventoryTowerClick(Button towerButton, double mouseX, double mouseY) {
        Map map = GameEnvironment.getGameEnvironment().getMap();
        Tower selectedTower = map.getSelectedTower();
        Tower occupiedTower = reserveTowers.get(towerButton);

        // Place tower - user has a tower selected
        if (selectedTower != null) {
            // Button is already occupied
            if (occupiedTower != null) {
                GameEnvironment.getGameEnvironment().getController().showNotification("Inventory space already in use", 1);
                return;
            }

            // Place tower into inventory
            TowerButton.changeTower(towerButton, selectedTower.getType());
            map.stopPlacingTower();
            reserveTowers.put(towerButton, selectedTower);
            return;
        }

        // Retreive tower - user does not have a tower currently selected

        // Empty inventory button clicked (no action)
        if (occupiedTower == null) {
            return;
        }

        // Retrieve tower from inventory
        reserveTowers.remove(towerButton);
        TowerButton.changeTower(towerButton, null);
        map.startPlacingTower(occupiedTower, mouseX, mouseY);
    }

    /**
     * @return A collection of towers that are in the player's inventory. 
     */
    public Collection<Tower> getTowers() {
        Collection<Tower> towers = this.reserveTowers.values();
        towers.removeIf(Objects::isNull);
        return towers;
    }
}
