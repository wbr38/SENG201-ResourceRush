package seng201.team53.items.towers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.Tickable;
import seng201.team53.items.upgrade.Upgradeable;

public class Tower implements Tickable, Upgradeable {
    private final TowerType type;
    private final ImageView imageView;
    private final Timeline glowAnimation;
    private boolean broken = false;
    private double reloadSpeedModifier = 1;
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

    public void addReloadSpeedModifier() {
        reloadSpeedModifier += 0.2;
    }

    /**
     * @return True if this tower is ready to generate now, or false if it needs more time to reload. 
     */
    public boolean canGenerate() {
        if (broken)
            return false;

        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        long reloadSpeed = type.getReloadSpeed().toMillis();
        reloadSpeed /= (long) difficulty.getTowerReloadModifier();
        reloadSpeed /= (long) reloadSpeedModifier;

        long deltaTime = System.currentTimeMillis() - lastGenerateTime;
        if (deltaTime < reloadSpeed)
            return false;

        glowAnimation.play();
        lastGenerateTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public void tick() {
        lifetimeTicks++;
    }
}
