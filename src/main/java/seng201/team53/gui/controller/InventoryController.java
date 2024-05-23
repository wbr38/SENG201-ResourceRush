package seng201.team53.gui.controller;

import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.items.Item;
import seng201.team53.game.items.towers.Tower;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * This class is a sub-controller of the GameController and is responsible for handling events related the inventory
 */
public class InventoryController {
    private final GameController gameController;
    private final MapInteractionController interactionController;
    private final Map<Button, Tower> reserveTowers = new HashMap<>();

    /**
     * Construct a new inventory controller instance with a reference to the main game controller
     * and the map interaction controller
     * @param gameController The game controller
     * @param interactionController The map interaction controller
     */
    public InventoryController(GameController gameController, MapInteractionController interactionController) {
        this.gameController = gameController;
        this.interactionController = interactionController;
    }

    /**
     * Initialises the inventory controller.
     * This method updates each button to remove the default graphic and tool tip
     */
    public void init() {
        List<Button> inventoryButtons = Arrays.asList(gameController.inventoryButton1,
                                                      gameController.inventoryButton2,
                                                      gameController.inventoryButton3,
                                                      gameController.inventoryButton4);
        inventoryButtons.forEach(button -> {
            button.setOnMouseClicked(event -> onInventoryButtonClick(event, button));
            gameController.updateButton(button, null);
        });
    }

    /**
     * Handles when a tower is placed into the inventory.
     * If the inventory space is occupied, it will display a notification saying so.
     * Otherwise, the button will be removed and the tower will be placed into a reserved tower collection
     * @param tower The tower to be placed into the inventory
     * @param towerButton The button representing the inventory space
     */
    private void placeTowerIntoInventory(Tower tower, Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower != null) {
            GameEnvironment.getGameEnvironment().getController().showNotification("Inventory space already in use", 1);
            return;
        }

        gameController.updateButton(towerButton, tower.getPurchasableType());
        reserveTowers.put(towerButton, tower);
        tower.setInInventory(true);
        interactionController.stopPlacingItem();
    }

    /**
     * This method tries to retrieve the tower stored at a given button which represents an inventory space.
     * If a tower was found in this inventory space, it will start the place tower interaction
     * @param towerButton The button if a tower was stored at this inventory space, null otherwise
     */
    private void retrieveTower(Button towerButton) {
        Tower occupiedTower = reserveTowers.get(towerButton);
        if (occupiedTower == null)
            return;

        occupiedTower.setInInventory(false);
        reserveTowers.put(towerButton, null);
        gameController.updateButton(towerButton, null);
        interactionController.startPlacingItem(occupiedTower);

        GameMap map = getGameEnvironment().getMap();
        map.setInteraction(MapInteraction.PLACE_TOWER);
    }

    /**
     * Handles when an inventory button is clicked.
     * If the user is currently moving a tower, the retrieve method will be called.
     * Otherwise, a notification will be shown
     * @param event The mouse event
     * @param towerButton The button representing the inventory space
     */
    private void onInventoryButtonClick(MouseEvent event, Button towerButton) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        Item selectedItem = interactionController.getSelectedItem();
        if (selectedItem == null) {
            this.retrieveTower(towerButton);
            return;
        }
        if (!(selectedItem instanceof Tower selectedTower)) {
            GameEnvironment.getGameEnvironment().getController().showNotification("Only towers may be placed into inventory", 1.0f);
            return;
        }

        this.placeTowerIntoInventory(selectedTower, towerButton);
    }
}
