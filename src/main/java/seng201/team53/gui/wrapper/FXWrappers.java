package seng201.team53.gui.wrapper;

import javafx.collections.MapChangeListener;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.assets.AssetLoader;
import seng201.team53.game.map.GameMap;
import seng201.team53.game.map.Tile;
import seng201.team53.game.round.GameRound;
import seng201.team53.game.items.Cart;
import seng201.team53.game.items.towers.Tower;

import java.util.HashMap;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * This class is responsible for handling creation, storing and deletion of fx wrappers for towers and carts
 */
public class FXWrappers {
    private final Map<Tower, FXTower> fxTowers = new HashMap<>();
    private final Map<Cart, FXCart> fxCarts = new HashMap<>();

    /**
     * Initialises the FXWrapper class by adding change listeners to the game round and maps towers properties
     */
    public void init() {
        getGameEnvironment().getRoundProperty().addListener(($, oldRound, newRound) -> onGameRoundChange(newRound));
        getGameEnvironment().getMap().getTowersProperty().addListener(this::onTowersChange);

        // set it up for current round
        var round = getGameEnvironment().getRound();
        onGameRoundChange(round);
    }

    /**
     * Searches for a cart's graphical image on the map given a screen x and y coordinate.
     * @param screenX The screen X coordinate
     * @param screenY The screen Y coordinate
     * @return The found cart if a cart was found at the given screen x and y coordinate, null otherwise
     */
    public Cart findCartAtScreen(int screenX, int screenY) {
        for (var entry : fxCarts.entrySet()) {
            var cart = entry.getKey();
            var fxCart = entry.getValue();
            var imageView = fxCart.getImageView();
            var pointInScene = imageView.localToScene(imageView.getX(), imageView.getY());
            double sceneX = pointInScene.getX();
            double sceneY = pointInScene.getY();
            if (sceneX <= screenX &&
                (sceneX + 40) > screenX &&
                sceneY <= screenY &&
                (sceneY + 40) > screenY)
                return cart;
        }
        return null;
    }

    /**
     * Handles when the game round changes
     * When the game round is changed, we need to clear the list of FX carts as they are no longer used then, initialise
     * each of the new carts for the new round with the required graphical properties
     * @param round The new game round
     */
    private void onGameRoundChange(GameRound round) {
        // we only ever add carts to the list of carts so prob only need this
        // when the cart finishes the path, it gets removed as per FXCart#onCartStateUpdate
        // when we change round, the GameRound and hence list of carts just gets garbage collected as there
        // is no longer a surviving reference to it
        fxCarts.clear();

        round.getCarts().forEach(cart -> {
            GameEnvironment gameEnvironment = getGameEnvironment();
            AssetLoader assetLoader = gameEnvironment.getAssetLoader();
            StackPane wrapper = new StackPane();
            ImageView imageView = new ImageView(assetLoader.getCartImage(cart.getResourceType(), false));

            Label capacityLabel = new Label("0/" + cart.getMaxCapacity());
            capacityLabel.setTranslateY(10); // 10 units
            capacityLabel.setFont(Font.font("System Regular", FontWeight.BOLD, 16));
            capacityLabel.setTextFill(Color.WHITE);

            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
            dropShadow.setColor(Color.BLACK);
            capacityLabel.setEffect(dropShadow);

            Polyline polylinePath = gameEnvironment.getMap().getPolylinePath();
            wrapper.setTranslateX(polylinePath.getPoints().get(1));
            wrapper.setTranslateY(polylinePath.getPoints().get(0));
            wrapper.getChildren().addAll(imageView, capacityLabel);
            gameEnvironment.getController().getOverlay().getChildren().add(wrapper);
            fxCarts.put(cart, new FXCart(cart, wrapper, imageView, capacityLabel));
        });
    }

    /**
     * Handles when the map of towers is changed. This method is called when a tower is added or removed/
     * If the tower was removed, the graphical representation of the tower should be removed and no longer stored.
     * If the tower was added, the graphical representation of the tower needs to be created and stored.
     * @param change
     */
    private void onTowersChange(MapChangeListener.Change<? extends Tower, ? extends Tile> change) {
        var tower = change.getKey();
        if (change.wasRemoved()) {
            var fxTower = fxTowers.get(tower);
            fxTower.onTowerRemoved();
            fxTowers.remove(tower);
            return;
        }

        var towerType = tower.getPurchasableType();
        var tile = change.getValueAdded();
        var image = getGameEnvironment().getAssetLoader().getTowerTypeImage(towerType, tower.isBroken());
        var imageView = new ImageView(image);
        imageView.setFitHeight(GameMap.TILE_HEIGHT);
        imageView.setFitWidth(GameMap.TILE_WIDTH);
        fxTowers.put(tower, new FXTower(tower, tile, imageView));
    }
}
