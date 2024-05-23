package seng201.team53.gui.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.game.state.GameState;
import seng201.team53.game.items.Cart;
import seng201.team53.game.items.Item;
import seng201.team53.game.items.Purchasable;
import seng201.team53.game.items.Shop;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.towers.TowerType;
import seng201.team53.game.items.upgrade.UpgradeItem;
import seng201.team53.game.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * This class is a sub-controller of the GameController and is responsible for handling map interactions
 */
public class MapInteractionController {
    private final GameController gameController;
    private Item selectedItem;
    private ImageView selectedImageView;

    /**
     * Constructs a new map interaction controller with a reference to the main game controller
     * @param gameController The game controller
     */
    public MapInteractionController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Initialises the map interaction controller by setting a mouse click event on the overlay
     */
    public void init() {
        gameController.getOverlay().setOnMouseClicked(this::onMouseClickOverlay);
    }

    /**
     * @return The currently selected item that the user is moving/placing.
     */
    public Item getSelectedItem() {
        return selectedItem;
    }

    /**
     * Attempt to purchase an item, and begin the process of placing or using the item.
     * @param purchasable The item to purchase.
     */
    public void tryPurchaseItem(Purchasable purchasable) {
        GameEnvironment gameEnvironment = getGameEnvironment();
        GameMap map = getGameEnvironment().getMap();
        if (map.getInteraction() != MapInteraction.NONE)
            return;
        if (!gameEnvironment.getShop().purchaseItem(purchasable))
            return;
        if (purchasable instanceof UpgradeItem upgradeItem) {
            List<Upgradeable> applicableItems = upgradeItem.getApplicableItems();
            if (applicableItems.isEmpty()) {
                gameController.showNotification("You have no items to apply this upgrade to", 2);
                return;
            }
        }

        Item item = purchasable.create();
        startPlacingItem(item);
        if (gameEnvironment.getStateHandler().getState() == GameState.ROUND_ACTIVE) {
            gameEnvironment.getStateHandler().setState(GameState.ROUND_PAUSE);
        }
    }

    /**
     * Sell the item the player has currently selected / is moving
     */
    public void sellSelectedItem() {
        Item item = this.getSelectedItem();
        if (item == null)
            return;

        Shop shop = GameEnvironment.getGameEnvironment().getShop();
        shop.sellItem(item.getPurchasableType());
        stopPlacingItem();
    }

    /**
     * Start moving/selecting/placing an item.
     * @param item The item to start placing
     */
    public void startPlacingItem(Item item) {
        this.selectedItem = item;
        GridPane gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(true);

        Image image = getGameEnvironment().getAssetLoader().getItemImage(item.getPurchasableType());
        Pane overlay = gameController.getOverlay();
        selectedImageView = new ImageView(image);
        selectedImageView.setFitHeight(GameMap.TILE_HEIGHT);
        selectedImageView.setFitWidth(GameMap.TILE_WIDTH);
        overlay.getChildren().add(selectedImageView);
        overlay.setOnMouseMoved(this::onMouseMove);
        gameController.showSellItemPopup(item);

        GameMap map = getGameEnvironment().getMap();
        if (item.getPurchasableType() instanceof TowerType) {
            map.setInteraction(MapInteraction.PLACE_TOWER);
        } else {
            map.setInteraction(MapInteraction.PLACE_UPGRADE);
        }
    }

    /**
     * Stop moving/selecting/placing the currently selected item.
     */
    public void stopPlacingItem() {
        GridPane gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(false);

        // Stop following mouse
        Pane overlay = gameController.getOverlay();
        overlay.setOnMouseMoved(null);
        overlay.getChildren().remove(selectedImageView);

        GameMap map = getGameEnvironment().getMap();
        map.setInteraction(MapInteraction.NONE);
        selectedItem = null;
        selectedImageView = null;
        gameController.showSellItemPopup(null);
    }

    /**
     * Try placing the currently selected tower onto a tile.
     * @param tile The tile to place the tower on.
     */
    public void tryPlaceSelectedTower(Tile tile) {
        if (!tile.canPlaceTower())
            return;
        if (!(selectedItem instanceof Tower selectedTower))
            return;

        GameMap map = getGameEnvironment().getMap();
        map.addTower(selectedTower, tile);
        stopPlacingItem();
    }

    /**
     * Start the process of moving the tower on this tile (if there is one).
     * Will remove the tower from the map, and the user wil begin placing it again.
     * @param tile The tile the user clicked, that may contain a tower.
     */
    public void tryMoveTower(Tile tile) {
        if (!tile.canMoveTower())
            return;

        Tower tower = tile.getTower();
        GameMap map = getGameEnvironment().getMap();
        removeTower(tower);
        startPlacingItem(tower);
        map.setInteraction(MapInteraction.PLACE_TOWER);
    }

    /**
     * Handles the GUI logic to remove a tower from the map.
     * This function will also call map.removeTower.
     * @param tower The tower to remove from the map.
     */
    private void removeTower(Tower tower) {
        GameMap map = getGameEnvironment().getMap();
        map.removeTower(tower);
    }

    /**
     * This method will attempt to upgrade a cart with a selected upgrade item at a given screen x and y coordinate.
     * If the selected upgrade item cannot be applied to a cart, a notification will be shown. Otherwise, the upgrade
     * will be applied and the user will stop placing the item.
     * @param screenX The screen x-coordinate
     * @param screenY The screen y-coordinate
     */
    private void tryUpgradeCart(int screenX, int screenY) {
        Cart cart = getGameEnvironment().getController().getFXWrappers().findCartAtScreen(screenX, screenY);
        if (cart == null)
            return;

        Item selectedItem = this.getSelectedItem();
        if (selectedItem instanceof UpgradeItem upgradeItem) {
            if (!upgradeItem.canApply(cart)) {
                gameController.showNotification("You cannot apply " + upgradeItem.getName() + " to this cart", 3);
                return;
            }
            upgradeItem.apply(cart);
            stopPlacingItem();
        }
    }

    /**
     * This method will attempt to upgrade a tower with a selected upgrade item at a tile.
     * If the selected tile does not have a tower on it, the method will stop.
     * If the selected upgrade item cannot be applied to a tower, a notification will be shown. Otherwise, the upgrade
     * will be applied and the user will stop placing the item.
     * @param tile The clicked tile
     */
    private void tryUpgradeTower(Tile tile) {
        Tower tower = tile.getTower();
        if (tower == null)
            return;

        Item selectedItem = this.getSelectedItem();
        if (selectedItem instanceof UpgradeItem upgradeItem) {
            if (!upgradeItem.canApply(tower)) {
                gameController.showNotification("You cannot apply " + upgradeItem.getName() + " to this tower", 3);
                return;
            }
            upgradeItem.apply(tower);
            stopPlacingItem();
        }
    }

    /**
     * Handles when the mouse moves during placing an item.
     * This method updates the image view on the overlay to show the image moving with the user's cursor.
     * @param event The mouse event
     */
    private void onMouseMove(MouseEvent event) {
        if (selectedImageView == null)
            return;

        selectedImageView.setX(event.getSceneX() - ((double)GameMap.TILE_HEIGHT / 2));
        selectedImageView.setY(event.getSceneY() - ((double)GameMap.TILE_WIDTH / 2));
    }

    /**
     * Handles when the overlay is clicked.
     * If the current map interaction is none, it will attempt to move the tower at that tile
     * If the current map interaction is place tower, it will attempt to place a tower at that tile
     * If the current map interaction is place upgrade, it will attempt to upgrade a cart or tower depending
     * on what type the upgrade is.
     * @param event
     */
    private void onMouseClickOverlay(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameMap map = getGameEnvironment().getMap();
        Tile tile;
        try {
            tile = map.getTileFromScreenPosition((int)event.getSceneX(), (int)event.getSceneY());
        } catch (TileNotFoundException e) {
            return;
        }

        switch (map.getInteraction()) {
            case NONE -> tryMoveTower(tile);
            case PLACE_TOWER -> tryPlaceSelectedTower(tile);
            case PLACE_UPGRADE -> {
                if (selectedItem instanceof UpgradeItem upgradeItem) {
                    if (upgradeItem.isCartUpgrade())
                        tryUpgradeCart((int)event.getSceneX(), (int)event.getSceneY());
                    else if (upgradeItem.isTowerUpgrade())
                        tryUpgradeTower(tile);
                }
            }
        }
    }
}
