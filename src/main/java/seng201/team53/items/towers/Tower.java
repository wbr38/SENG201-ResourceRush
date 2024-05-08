package seng201.team53.items.towers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team53.game.Tickable;

public class Tower implements Tickable {
    private final TowerType type;
    private final ImageView imageView;
    private final Timeline glowAnimation;
    private boolean broken = false;
    private int xpLevel = 0;
    private int lifetimeTicks = 0;
    private long lastGenerateTime = System.currentTimeMillis();

    protected Tower(TowerType type, ImageView imageView) {
        this.type = type;
        this.imageView = imageView;

        var glow = new Glow();
        glow.setLevel(0);
        glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0)),
                new KeyFrame(Duration.millis(250), new KeyValue(glow.levelProperty(), 0.8)),
                new KeyFrame(Duration.millis(500), new KeyValue(glow.levelProperty(), 0)));
        glowAnimation.setCycleCount(1);
        imageView.setEffect(glow);
        setBroken(false);
    }

    public TowerType getType() {
        return type;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
        imageView.setImage(broken ? type.getBrokenImage() : type.getImage());
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void incrementXpLevel(int amount) {
        this.xpLevel += amount;
    }

    @Override
    public void tick() {
        if (isBroken())
            return;

        lifetimeTicks++;
        if (System.currentTimeMillis() - lastGenerateTime >= type.getReloadSpeed().toMillis()) {
            lastGenerateTime  = System.currentTimeMillis();
            glowAnimation.play();
        }
    }
}
