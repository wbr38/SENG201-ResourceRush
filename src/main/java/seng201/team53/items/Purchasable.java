package seng201.team53.items;

public interface Purchasable {
    String getName();

    String getDescription();

    int getCostPrice();

    int getSellPrice();

    Boolean isSellable();
}
