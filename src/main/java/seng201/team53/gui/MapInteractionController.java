package seng201.team53.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import seng201.team53.exceptions.TileNotFoundException;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;
import seng201.team53.service.MapInteractionService;

import java.util.function.Supplier;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class MapInteractionController {
    private final GameController gameController;
    private final MapInteractionService mapInteractionService = new MapInteractionService();
    private ImageView towerImageView;
    private Supplier<Tower> onPlaceSupplier;

    public MapInteractionController(GameController gameController) {
        this.gameController = gameController;
    }

    public void placeTowerAtTile(Tile tile) {
        if (!mapInteractionService.canPlaceTowerAt(tile))
            return;

        mapInteractionService.placeTower(onPlaceSupplier.get(), tile);
        gameController.getOverlay().getChildren().remove(towerImageView); // todo - find a work around, gets called at stopTowerMethod but throw an error without being removed from the overlay first
        gameController.getGridPane().add(towerImageView, tile.getX(), tile.getY());
        stopTowerMovement();
    }

    public void startPlacingTower(TowerType towerType) {
        if (mapInteractionService.getInteraction() != MapInteraction.NONE)
            return;

        beginTowerMovement(towerType, towerType.getImage(), towerType::create);
    }

    public void startMovingTowerAtTile(Tile tile) {
        if (!mapInteractionService.canMoveTowerAt(tile))
            return;

        var tower = tile.getTower();
        removeTower(tower, tile);
        beginTowerMovement(tower.getType(), tower.getType().getImage(), () -> tower);
    }

    private void beginTowerMovement(TowerType towerType, Image towerImage, Supplier<Tower> onPlaceSupplier) {
        this.towerImageView = new ImageView(towerImage);
        this.onPlaceSupplier = onPlaceSupplier;

        var overlay = gameController.getOverlay();
        var gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(true);
        towerImageView.toBack();
        towerImageView.setFitHeight(GameMap.TILE_HEIGHT);
        towerImageView.setFitWidth(GameMap.TILE_WIDTH);
        overlay.getChildren().add(towerImageView);
        overlay.setOnMouseMoved(this::onSelectedTowerMouseMove);
        overlay.setOnMouseClicked(this::onSelectedTowerMouseClick);
        gameController.showSellTowerPopup(towerType);
        gameController.setInventoryVisible(true);
        mapInteractionService.setInteraction(MapInteraction.PLACE_TOWER);
    }

    private void stopTowerMovement() {
        var overlay = gameController.getOverlay();
        var gridPane = gameController.getGridPane();
        gridPane.setGridLinesVisible(false);
        overlay.setOnMouseMoved(null);
        overlay.setOnMouseClicked(null);
        overlay.getChildren().remove(towerImageView);
        this.towerImageView = null;
        this.onPlaceSupplier = null;
        mapInteractionService.setInteraction(MapInteraction.NONE);
    }

    private void removeTower(Tower tower, Tile tile) {
        var gridPane = gameController.getGridPane();
        gridPane.getChildren().remove(tower.getImageView());
        mapInteractionService.removeTower(tower, tile);
    }

    private void onSelectedTowerMouseMove(MouseEvent event) {
        towerImageView.setX(event.getX() - ((double) GameMap.TILE_HEIGHT / 2));
        towerImageView.setY(event.getY() - ((double) GameMap.TILE_WIDTH / 2));
    }

    private void onSelectedTowerMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        var map = getGameEnvironment().getMap();
        var interaction = mapInteractionService.getInteraction();
        Tile tile;
        try {
            tile = map.getTileFromScreenPosition((int) event.getSceneX(),(int) event.getSceneY());
        } catch (TileNotFoundException e) {
            return;
        }

        if (interaction == MapInteraction.NONE) {
            startMovingTowerAtTile(tile);
            return;
        }
        if (interaction == MapInteraction.PLACE_TOWER) {
            placeTowerAtTile(tile);
        }
    }
}
