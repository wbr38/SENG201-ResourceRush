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
import seng201.team53.items.Item;
import seng201.team53.items.Purchasable;
import seng201.team53.items.Shop;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class MapInteractionController {
    private final GameController gameController;
    private Item selectedItem;
    private ImageView selectedImageView;


    public MapInteractionController(GameController gameController) {
        this.gameController = gameController;
    }

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

        var map = getGameEnvironment().getMap();
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
        var map = getGameEnvironment().getMap();
        map.removeTower(tower);
    }

    private void tryUpgradeCart(int screenX, int screenY) {
        var cart = getGameEnvironment().getController().getFXWrappers().findCartAtScreen(screenX, screenY);
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

    private void tryUpgradeTower(Tile tile) {
        var tower = tile.getTower();
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

    private void onMouseMove(MouseEvent event) {
        if (selectedImageView == null)
            return;

        selectedImageView.setX(event.getSceneX() - ((double)GameMap.TILE_HEIGHT / 2));
        selectedImageView.setY(event.getSceneY() - ((double)GameMap.TILE_WIDTH / 2));
    }

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
                        tryUpgradeCart((int) event.getSceneX(), (int) event.getSceneY());
                    else if (upgradeItem.isTowerUpgrade())
                        tryUpgradeTower(tile);
                }
            }
        }
    }
}
