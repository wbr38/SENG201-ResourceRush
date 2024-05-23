package seng201.team53.gui.wrapper;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import seng201.team53.game.map.Tile;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.towers.TowerType;

import java.util.Objects;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

/**
 * Represents a graphical wrapper for a tower.
 * This class contains an instance of a tower and all the javafx elements which allows for the tower's graphics.
 * Change listeners are stored so when the tower is removed, the listeners can also be removed
 */
public class FXTower {
    private final Tower tower;
    private final Timeline glowAnimation;
    private final Media soundEffectMedia;
    private final ImageView imageView;
    private final ChangeListener<Boolean> towerBrokenListener;
    private final ChangeListener<Number> lastGenerateTimeListener;

    /**
     * Constructs a new FX tower instance.
     * This constructor sets up the glow animation which is used when the tower generates.
     * Change listeners are created and added for the towers broken and last generate property.
     * @param tower The tower
     * @param tile The tile where the tower is placed
     * @param imageView The towers image view
     */
    public FXTower(Tower tower, Tile tile, ImageView imageView) {
        this.tower = tower;
        this.imageView = imageView;

        Glow glow = new Glow(0);
        glowAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0)),
            new KeyFrame(Duration.millis(150), new KeyValue(glow.levelProperty(), 0.8)),
            new KeyFrame(Duration.millis(300), new KeyValue(glow.levelProperty(), 0)));
        glowAnimation.setCycleCount(1);
        imageView.setEffect(glow);
        getGameEnvironment().getController().getGridPane().add(imageView, tile.getX(), tile.getY());

        String resource = Objects.requireNonNull(getClass().getResource("/assets/sound/projectile.wav")).toString();
        soundEffectMedia = new Media(resource);

        towerBrokenListener = ($, oldValue, newValue) -> onBrokenUpdate(newValue);
        lastGenerateTimeListener = ($, newTime, oldTime) -> onTowerGenerate();

        tower.getBrokenProperty().addListener(towerBrokenListener);
        tower.getLastGenerateTimeProperty().addListener(lastGenerateTimeListener);
    }

    /**
     * Handles when the towers broken property is updated.
     * This method will change the towers image to the towers type image.
     * If broken is true, it will fetch the broken image, otherwise the normal image
     * @param broken The new broken value
     */
    private void onBrokenUpdate(boolean broken) {
        TowerType towerType = tower.getPurchasableType();
        Image towerImage = getGameEnvironment().getAssetLoader().getTowerTypeImage(towerType, broken);
        imageView.setImage(towerImage);
    }

    /**
     * Handles when the tower generates
     * This method will play the glow animation as well as a sound effect each time the towers last generate
     * time is changed, meaning the tower has generated
     */
    private void onTowerGenerate() {
        glowAnimation.play();

        MediaPlayer soundEffect = new MediaPlayer(soundEffectMedia);
        soundEffect.setCycleCount(1);
        soundEffect.play();
        soundEffect.setOnEndOfMedia(() -> soundEffect.dispose());
    }

    /**
     * Handles when the tower is removed from the map
     * This method will remove the listeners as well as the graphical image view placed on the map
     */
    public void onTowerRemoved() {
        tower.getBrokenProperty().removeListener(towerBrokenListener);
        tower.getLastGenerateTimeProperty().removeListener(lastGenerateTimeListener);
        getGameEnvironment().getController().getGridPane().getChildren().remove(imageView);
    }
}
