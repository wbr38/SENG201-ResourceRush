package seng201.team53.items;

import javafx.scene.image.Image;

public interface Purchasable {
    String getName();

    String getDescription();

    Image getImage();

    int getCostPrice();

    int getSellPrice();

    boolean isSellable();
}
