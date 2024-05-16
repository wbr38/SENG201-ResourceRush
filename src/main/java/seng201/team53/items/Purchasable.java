package seng201.team53.items;

import javafx.scene.image.Image;

public interface Purchasable {
    String getName();

    String getDescription();

    int getCostPrice();

    int getSellPrice();

    Image getImage();

    boolean isSellable();
}
