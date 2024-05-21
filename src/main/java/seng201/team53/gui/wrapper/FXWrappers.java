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
import seng201.team53.items.Cart;
import seng201.team53.items.towers.Tower;

import java.util.HashMap;
import java.util.Map;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

// responsible for handling creation and deletion of fx wrappers for towers and carts
public class FXWrappers {
    private final Map<Tower, FXTower> fxTowers = new HashMap<>();
    private final Map<Cart, FXCart> fxCarts = new HashMap<>();

    public void init() {
        getGameEnvironment().getRoundProperty().addListener(($, oldRound, newRound) -> onGameRoundChange(newRound));
        getGameEnvironment().getMap().getTowersProperty().addListener(this::onTowersChange);

        // set it up for current round
        var round = getGameEnvironment().getRound();
        onGameRoundChange(round);
    }

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

    // handles when the game round is changed (comes from GameRound)
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

            // Capacity label (0/10)
            Label capacityLabel = new Label("0/" + cart.getMaxCapacity());
            capacityLabel.setTranslateY(10); // 10 units
            capacityLabel.setFont(Font.font("System Regular", FontWeight.BOLD, 16));
            capacityLabel.setTextFill(Color.WHITE);

            // Add a shadow to the capacity label
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

    // handles when towers are added or removed from the main map (comes from GameMap)
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
