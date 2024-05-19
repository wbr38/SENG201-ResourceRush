package seng201.team53.items.towers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.Tickable;
import seng201.team53.game.map.GameMap;
import seng201.team53.items.Item;
import seng201.team53.items.ResourceType;
import seng201.team53.items.upgrade.Upgradeable;

import javafx.util.Duration;

public class Tower implements Item, Tickable, Upgradeable {
    private final Timeline glowAnimation;
    private TowerType type;
    private boolean broken = false;
    private double reloadSpeedModifier = 1;
    private int xpLevel = 0;
    private int lifetimeTicks = 0;
    private long lastGenerateTime = System.currentTimeMillis();
    private ImageView imageView;

    protected Tower(TowerType type) {
        this.type = type;

        this.imageView = new ImageView(this.type.getImage());
        imageView.setFitHeight(GameMap.TILE_HEIGHT);
        imageView.setFitWidth(GameMap.TILE_WIDTH);

        var glow = new Glow();
        glow.setLevel(0);
        glowAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0)),
            new KeyFrame(Duration.millis(250), new KeyValue(glow.levelProperty(), 0.8)),
            new KeyFrame(Duration.millis(500), new KeyValue(glow.levelProperty(), 0)));
        glowAnimation.setCycleCount(1);
        getImageView().setEffect(glow);
        setBroken(false);
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
        ImageView imageView = getImageView();
        Image image = broken ? type.getBrokenImage() : type.getImage();
        imageView.setImage(image);
    }

    public TowerType getPurchasableType() {
        return type;
    }

    public ImageView getImageView() {
        return imageView;
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
        reloadSpeed /= (long)difficulty.getTowerReloadModifier();
        reloadSpeed /= (long)reloadSpeedModifier;

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

    public interface Type {
        public static TowerType LUMBER_MILL = new TowerType("Lumber Mill Tower",
            "A Lumber Mill produces wood",
            ResourceType.WOOD,
            "/assets/items/wood_tower.png",
            "/assets/items/wood_tower_broken.png",
            100,
            1,
            java.time.Duration.ofSeconds(1));


        public static TowerType MINE = new TowerType("Mine Tower",
            "A Mine produces ores",
            ResourceType.STONE,
            "/assets/items/stone_tower.png",
            "/assets/items/stone_tower_broken.png",
            120,
            1,
            java.time.Duration.ofSeconds(1));

        public static TowerType QUARRY = new TowerType("Quarry Tower",
            "A Quarry produces stone",
            ResourceType.ORE,
            "/assets/items/quarry_tower.png",
            "/assets/items/quarry_tower_broken.png",
            150,
            1,
            java.time.Duration.ofSeconds(1));

        public static TowerType WIND_MILL = new TowerType("Windmill Tower",
            "A wind mill produces energy",
            ResourceType.ENERGY,
            "/assets/items/wind_turbine_tower.png",
            "/assets/items/wind_turbine_tower_broken.png",
            200,
            1,
            java.time.Duration.ofSeconds(1));
    }
}