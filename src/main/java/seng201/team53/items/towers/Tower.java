package seng201.team53.items.towers;

import seng201.team53.items.Purchasable;
import seng201.team53.items.ResourceType;

public abstract class Tower implements Purchasable {
    
    private String name;
    public ResourceType resourceType;
    public int resourceAmount;
    public float reloadSpeed;
    public int xpLevel;

    Tower(
        String name,
        ResourceType resourceType
    ) {
        this.name = name;
        this.resourceType = resourceType;
        this.resourceAmount = 0;
        this.xpLevel = 0;
    }

    public abstract void getUpgrades();
    public abstract String getSpriteFilePath();

    @Override
    public String getName() {
        return name;
    }
}
