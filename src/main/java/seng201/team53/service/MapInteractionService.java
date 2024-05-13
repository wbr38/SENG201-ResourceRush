package seng201.team53.service;

import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.Tower;

import static seng201.team53.game.GameEnvironment.getGameEnvironment;

public class MapInteractionService {
    private MapInteraction interaction = MapInteraction.NONE;

    public MapInteraction getInteraction() {
        return interaction;
    }

    public void setInteraction(MapInteraction interaction) {
        this.interaction = interaction;
    }

    public boolean canMoveTowerAt(Tile tile) { // new
        var tower = tile.getTower();
        return tower != null && !tower.isBroken();
    }
    public boolean canPlaceTowerAt(Tile tile) {
        var tower = tile.getTower();
        return tower == null && tile.isBuildable() && !tile.isPath();
    }

    public void placeTower(Tower tower, Tile tile) {
        var map = getGameEnvironment().getMap();
        map.addTower(tower, tile);
        tile.setTower(tower);
    }

    public void removeTower(Tower tower, Tile tile) { // new
        var map = getGameEnvironment().getMap();
        map.removeTower(tower);
        tile.setTower(null);
    }
}
