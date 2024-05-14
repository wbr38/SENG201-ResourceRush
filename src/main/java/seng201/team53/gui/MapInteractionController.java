package seng201.team53.gui;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.Shop;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

public class MapInteractionController {
    private final GameController gameController;

    private Tower selectedTower;

    public MapInteractionController(GameController gameController) {
        this.gameController = gameController;
    }

    public void init() {
        Pane overlay = gameController.getOverlay();

        // NOTE: using setOnMouseClicked will overwrite previous event listeners
        // Maybe there's something like addEventHandler we could use, if other classes end up needing to use overlay.setOnMouseClicked
        overlay.setOnMouseClicked(this::onOverlayClicked);
    }

    public void placeSelectedTower(Tile tile) {
        if (!tile.canPlaceTower())
            return;

        Tower tower = this.getSelectedTower();

        // Add tower to map
        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        map.addTower(tower, tile);

        // Add the tower image to the map's tile
        ImageView towerImage = tower.getImageView();
        gameController.getOverlay().getChildren().remove(towerImage);
        gameController.getGridPane().add(towerImage, tile.getX(), tile.getY());
        stopMovingTower();
    }

    /**
     * Attempt to purchase the given TowerType from the shop. If successful, the user will begin placing the newly purchased tower.
     * @param towerType The TowerType to purchase and start placing.
     */
    public void tryPurchaseTower(TowerType towerType) {
        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        if (map.getInteraction() != MapInteraction.NONE)
            return;

        Boolean purchased = GameEnvironment.getGameEnvironment().getShop().purchaseItem(towerType);
        if (!purchased)
            return;

        Tower tower = towerType.create();
        startMovingTower(tower);
    }

    /**
     * Sell the tower the player has currently selected / is moving
     */
    public void sellSelectedTower() {
        Tower tower = this.getSelectedTower();
        // if (tower == null)
        // return;

        Shop shop = GameEnvironment.getGameEnvironment().getShop();
        shop.sellItem(tower.getType());

        stopMovingTower();
    }

    public void moveTower(Tile tile) {
        if (!tile.canMoveTower())
            return;

        // Remove the tower from the map and start placing it again
        Tower tower = tile.getTower();
        removeTower(tile);
        startMovingTower(tower);
    }

    public void startMovingTower(Tower tower) {
        this.selectedTower = tower;

        var gridPane = gameController.getGridPane();
        var overlay = gameController.getOverlay();
        gridPane.setGridLinesVisible(true);
        gameController.showSellTowerPopup(tower.getType());
        gameController.setInventoryVisible(true);

        // Add tower image to overlay, to appear as if the user is dragging the tower
        ImageView towerImage = tower.getImageView();
        towerImage.toBack();
        towerImage.setFitHeight(GameMap.TILE_HEIGHT);
        towerImage.setFitWidth(GameMap.TILE_WIDTH);
        overlay.getChildren().add(towerImage);
        overlay.setOnMouseMoved(this::onSelectedTowerMouseMove);

        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        map.setInteraction(MapInteraction.PLACE_TOWER);
    }

    /**
     * Stop moving the selected tower
     */
    public void stopMovingTower() {
        Tower tower = this.selectedTower;
        this.selectedTower = null;
        var overlay = gameController.getOverlay();
        var gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(false);
        overlay.setOnMouseMoved(null);
        overlay.getChildren().remove(tower.getImageView());

        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        map.setInteraction(MapInteraction.NONE);
    }

    private void removeTower(Tile tile) {
        Tower tower = tile.getTower();

        // Remove the tower's image from the map
        var gridPane = gameController.getGridPane();
        gridPane.getChildren().remove(tower.getImageView());

        // Remove the tower from the map & tile
        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        map.removeTower(tile);
    }

    private void onSelectedTowerMouseMove(MouseEvent event) {
        Tower tower = this.getSelectedTower();
        if (tower == null)
            return;

        ImageView towerImage = tower.getImageView();
        towerImage.setX(event.getX() - ((double)GameMap.TILE_HEIGHT / 2));
        towerImage.setY(event.getY() - ((double)GameMap.TILE_WIDTH / 2));
    }

    private void onOverlayClicked(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        GameMap map = GameEnvironment.getGameEnvironment().getMap();
        MapInteraction interaction = map.getInteraction();
        Tile tile;
        try {
            tile = map.getTileFromScreenPosition((int)event.getSceneX(), (int)event.getSceneY());
        } catch (TileNotFoundException e) {
            return;
        }

        switch (interaction) {
            // User is not selected a tower, and clicked a tile.
            // If there is a tower on this tile, start moving it.
            case NONE:
                moveTower(tile);
                return;

            // User has a tower selected and clicked a tower to place it onto
            case PLACE_TOWER:
                placeSelectedTower(tile);
                return;

            default:
                break;
        }
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }
}
