package seng201.team53.gui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import seng201.team53.items.Purchasable;

/**
 * Contains functions for modifying a shop/inventory GUI Button. (for towers and items)
 */
public class ShopButton {

    /**
     * Change the Button to match the info of the purchaseable item.
     * @param button The button to change the image, text, tooltip of.
     * @param towerType Set to `null` to make into a blank button (makes the text "Add Item")
     */
    public static void changeItem(Button button, Purchasable<?> purchasable) {
        if (purchasable == null) {
            button.setGraphic(null);
            button.getTooltip().setText("");
            button.setText("Add Item");
            return;
        }

        // Set new image
        ImageView imageView = new ImageView(purchasable.getImage());
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        button.setGraphic(imageView);

        // Set text
        button.getTooltip().setText("Cost $" + purchasable.getCostPrice());
        button.setText(purchasable.getName());
    }
}
