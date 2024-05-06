package seng201.team53.gui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

/**
 * Contains functions for modifying a Tower GUI Button (in inventory or shop)
 */
public class TowerButton {
    
    public static void changeTower(Button button, TowerType towerType) {

        Tower dummyTower = towerType.create();
        
        // Set new image
        ImageView imageView = dummyTower.getImageView();
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        button.setGraphic(imageView);

        // Set text 
        button.getTooltip().setText("Cost $" + dummyTower.getCostPrice());
        button.setText(dummyTower.getName() + " Tower");
    }
}
