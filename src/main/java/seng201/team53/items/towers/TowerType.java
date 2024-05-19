package seng201.team53.items.towers;

import java.time.Duration;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

public class TowerType implements Purchasable<Tower> {
    private final String name;
    private final String description;
    private final ResourceType resourceType;
    private final int costPrice;
    private final int resourceAmount;
    private final Duration reloadSpeed;

    TowerType(String name, String description, ResourceType resourceType, int costPrice,
              int resourceAmount, Duration reloadSpeed) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.costPrice = costPrice;
        this.resourceAmount = resourceAmount;
        this.reloadSpeed = reloadSpeed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Duration getReloadSpeed() {
        return reloadSpeed;
    }

    @Override
    public int getCostPrice() {
        return costPrice;
    }

    @Override
    public int getSellPrice() {
        GameDifficulty difficulty = GameEnvironment.getGameEnvironment().getDifficulty();
        int sellPrice = (int)Math.round(costPrice * difficulty.getSellPriceModifier());
        return sellPrice;
    }

    @Override
    public boolean isSellable() {
        return true;
    }

    public Tower create() {
        return new Tower(this);
    }
}
