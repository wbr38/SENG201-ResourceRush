package seng201.team53.gui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

/**
 * Contains functions for modifying a Tower GUI Button (in inventory or shop)
 */
public class TowerButton {

    /**
     * Change the Button to match the info of the TowerType
     * @param button The button to change the image, text, tooltip of.
     * @param towerType Set to `null` to make into a blank button (text "Add Tower")
     */
    public static void changeTower(Button button, TowerType towerType) {

        if (towerType == null) {
            button.setGraphic(null);
            button.getTooltip().setText("");
            button.setText("Add Tower");
            return;
        }

        Tower dummyTower = towerType.create();

        // Set new image
        ImageView imageView = dummyTower.getImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        button.setGraphic(imageView);

        // Set text
        button.getTooltip().setText("Cost $" + towerType.getCostPrice());
        button.setText(towerType.getName() + " Tower");
    }
}
