package seng201.team53.gui;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.game.state.GameState;
import seng201.team53.items.Cart;
import seng201.team53.items.Purchasable;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;
import seng201.team53.items.upgrade.UpgradeItem;
import seng201.team53.items.upgrade.Upgradeable;

import java.util.List;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class MapInteractionController {
    private final GameController gameController;
    private Tower selectedTower;
    private UpgradeItem selectedUpgradeItem;
    private ImageView selectedImageView;


    public MapInteractionController(GameController gameController) {
        this.gameController = gameController;
    }

    public void init() {
        gameController.getOverlay().setOnMouseClicked(this::onMouseClickOverlay);
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }

    public void tryPurchase(Purchasable purchasable) {
        GameEnvironment gameEnvironment = getGameEnvironment();
        GameMap map = getGameEnvironment().getMap();
        if (map.getInteraction() != MapInteraction.NONE)
            return;
        if (!gameEnvironment.getShop().canPurchaseItem(purchasable))
            return;

        var round = getGameEnvironment().getRound();
        if (purchasable instanceof TowerType towerType) {
            startMovingTower(towerType.create());
            startFollowingMouse(selectedTower.getImageView());
            map.setInteraction(MapInteraction.PLACE_TOWER);
        } else if (purchasable instanceof UpgradeItem upgradeItem) {
            List<Upgradeable> applicableItems = upgradeItem.getApplicableItems();
            if (applicableItems.isEmpty()) {
                gameController.showNotification("You have no items to apply this upgrade to", 2);
                return;
            }
            selectedUpgradeItem = upgradeItem;
            map.setInteraction(MapInteraction.PLACE_UPGRADE);
            startFollowingMouse(new ImageView(upgradeItem.getImage()));
        }
        if (getGameEnvironment().getStateHandler().getState() == GameState.ROUND_ACTIVE) {
            gameEnvironment.pauseRound(); // make it easier to click a cart or tower
            gameController.showResumeButton();
            getGameEnvironment().getStateHandler().setState(GameState.ROUND_PAUSE);
        }
    }

    public void startMovingTower(Tower tower) {
        GameEnvironment gameEnvironment = getGameEnvironment();
        var gridPane = gameController.getGridPane();
        selectedTower = tower;
        gridPane.setGridLinesVisible(true);
        gameController.showSellTowerPopup(tower.getType());
        gameEnvironment.getShop().purchaseItem(tower.getType());
        gameEnvironment.getMap().setInteraction(MapInteraction.PLACE_TOWER);
    }

    public void startFollowingMouse(ImageView imageView) { // idk what to call method
        var overlay = gameController.getOverlay();
        selectedImageView = imageView;
        selectedImageView.toBack();
        selectedImageView.setFitHeight(GameMap.TILE_HEIGHT);
        selectedImageView.setFitWidth(GameMap.TILE_WIDTH);
        overlay.getChildren().add(selectedImageView);
        overlay.setOnMouseMoved(this::onMouseMove);
    }

    public void stopFollowingMouse() {
        GameMap map = getGameEnvironment().getMap();
        var overlay = gameController.getOverlay();
        var gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(false);
        overlay.setOnMouseMoved(null);
        overlay.getChildren().remove(selectedImageView);
        selectedTower = null;
        selectedUpgradeItem = null;
        selectedImageView = null;
    }

    public void tryPlaceSelectedTower(Tile tile) {
        if (!tile.canPlaceTower())
            return;
        if (selectedTower == null)
            return;

        // Add tower to map
        GameMap map = getGameEnvironment().getMap();
        map.addTower(selectedTower, tile);

        // Add the tower image to the map's tile
        ImageView towerImage = selectedTower.getImageView();
        gameController.getOverlay().getChildren().remove(towerImage);
        gameController.getGridPane().add(towerImage, tile.getX(), tile.getY());
        gameController.showSellTowerPopup(null);
        stopFollowingMouse();
        map.setInteraction(MapInteraction.NONE);
    }

    public void tryMoveTower(Tile tile) {
        if (!tile.canMoveTower())
            return;

        Tower tower = tile.getTower();
        GameMap map = getGameEnvironment().getMap();
        removeTower(tower);
        startMovingTower(tower);
        startFollowingMouse(tower.getImageView());
        map.setInteraction(MapInteraction.PLACE_TOWER);
    }

    public void removeTower(Tower tower) {
        var gridPane = gameController.getGridPane();
        gridPane.getChildren().remove(tower.getImageView());

        GameMap map = getGameEnvironment().getMap();
        map.removeTower(tower);
    }

    private void onMouseMove(MouseEvent event) {
        System.out.println(System.currentTimeMillis() + " called");
        selectedImageView.setX(event.getSceneX() - ((double)GameMap.TILE_HEIGHT / 2));
        selectedImageView.setY(event.getSceneY() - ((double)GameMap.TILE_WIDTH / 2));
    }

    private void tryUpgradeCart(int screenX, int screenY) {
        // try find cart
        Cart cart = getGameEnvironment().getRound().findCartAtScreenPosition(screenX, screenY);
        if (cart == null)
            return;

        if (!selectedUpgradeItem.canApply(cart)) {
            gameController.showNotification("You cannot apply " + selectedUpgradeItem.getName() + " to this cart", 3);
            return;
        }
        selectedUpgradeItem.apply(cart);
        stopFollowingMouse();
        getGameEnvironment().getMap().setInteraction(MapInteraction.NONE);
    }

    private void tryUpgradeTower(Tile tile) {
        var tower = tile.getTower();
        if (tower == null)
            return;

        if (!selectedUpgradeItem.canApply(tower)) {
            gameController.showNotification("You cannot apply " + selectedUpgradeItem.getName() + " to this tower", 3);
            return;
        }
        selectedUpgradeItem.apply(tower);
        stopFollowingMouse();
        getGameEnvironment().getMap().setInteraction(MapInteraction.NONE);
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
            // User is not selected a tower, and clicked a tile.
            // If there is a tower on this tile, start moving it.
            case NONE -> tryMoveTower(tile);

            // User has a tower selected and clicked a tower to place it onto
            case PLACE_TOWER -> tryPlaceSelectedTower(tile);

            case PLACE_UPGRADE -> {
                if (selectedUpgradeItem.isCartUpgrade())
                    tryUpgradeCart((int) event.getSceneX(), (int) event.getSceneY());
                else if (selectedUpgradeItem.isTowerUpgrade())
                    tryUpgradeTower(tile);
            }
        }
    }
}
