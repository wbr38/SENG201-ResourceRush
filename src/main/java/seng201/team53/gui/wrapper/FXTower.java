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
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class FXTower {
    private final Tower tower;
    private final Timeline glowAnimation;
    private final Media soundEffectMedia;
    private final ImageView imageView;
    private final ChangeListener<Boolean> towerBrokenListener;
    private final ChangeListener<Number> lastGenerateTimeListener;

    public FXTower(Tower tower, Tile tile, ImageView imageView) {
        this.tower = tower;
        this.imageView = imageView;

        var glow = new Glow(0);
        glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0)),
                new KeyFrame(Duration.millis(150), new KeyValue(glow.levelProperty(), 0.8)),
                new KeyFrame(Duration.millis(300), new KeyValue(glow.levelProperty(), 0)));
        glowAnimation.setCycleCount(1);
        imageView.setEffect(glow);
        getGameEnvironment().getController().getGridPane().add(imageView, tile.getX(), tile.getY());

        String resource = getClass().getResource("/assets/sound/projectile.wav").toString();
        soundEffectMedia = new Media(resource);

        towerBrokenListener = ($, oldValue, newValue) ->
                onBrokenUpdate(newValue);
        lastGenerateTimeListener = ($, newTime, oldTime) ->
                onTowerGenerate();

        tower.getBrokenProperty().addListener(towerBrokenListener);
        tower.getLastGenerateTimeProperty().addListener(lastGenerateTimeListener);
    }

    private void onBrokenUpdate(boolean broken) {
        TowerType towerType = tower.getPurchasableType();
        Image towerImage = getGameEnvironment().getAssetLoader().getTowerTypeImage(towerType, broken);
        imageView.setImage(towerImage);
    }

    private void onTowerGenerate() {
        glowAnimation.play();

        MediaPlayer soundEffect = new MediaPlayer(soundEffectMedia);
        soundEffect.setCycleCount(1);
        soundEffect.play();
    }

    public void onTowerRemoved() {
        tower.getBrokenProperty().removeListener(towerBrokenListener);
        tower.getLastGenerateTimeProperty().removeListener(lastGenerateTimeListener);
        getGameEnvironment().getController().getGridPane().getChildren().remove(imageView);
    }
}
