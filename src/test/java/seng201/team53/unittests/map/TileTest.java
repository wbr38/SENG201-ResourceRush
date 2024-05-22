package seng201.team53.unittests.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.Tower;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TileTest {

    @Test
    void testNotBuildable() {
        // Not buildable
        Tile tile = new Tile(false, false, 0, 0);
        assertFalse(tile.isBuildable());
        assertFalse(tile.canPlaceTower());
        assertFalse(tile.canMoveTower());
    }

    @Test
    void testBuildable() {
        Tile tile = new Tile(true, false, 0, 0);
        assertTrue(tile.isBuildable());
        assertTrue(tile.canPlaceTower());
        assertFalse(tile.canMoveTower());
    }

    @Test
    void testIsPath() {
        Tile tile = new Tile(false, true, 0, 0);
        assertFalse(tile.isBuildable());
        assertFalse(tile.canPlaceTower());
        assertFalse(tile.canMoveTower());
    }

    @Test
    void testTower() {
        Tower tower = Tower.Type.LUMBER_MILL.create();
        Tile tile = new Tile(true, false, 0, 0);
        assertNull(tile.getTower());

        // Add tower
        tile.setTower(tower);
        assertEquals(tile.getTower(), tower);

        // Break tower
        assertTrue(tile.canMoveTower());
        tower.setBroken(true);
        assertFalse(tile.canMoveTower());

        // Remove tower
        tile.setTower(null);
        assertNull(tile.getTower());
    }

    @Test
    void testSetTowerNotBuildable() {
        Tower tower = Tower.Type.LUMBER_MILL.create();
        Tile tile = new Tile(false, true, 0, 0);
        assertThrows(IllegalStateException.class, () -> tile.setTower(tower));
    }

    @Test
    void testCoords() {
        final int x = 1;
        final int y = 2;
        Tile tile = new Tile(true, true, x, y);
        assertEquals(tile.getX(), x);
        assertEquals(tile.getY(), y);
    }

    @Test
    void testPathBuildable() {
        Tile tile = new Tile(true, true, 0, 0);
        assertTrue(tile.isPath());
        assertFalse(tile.isBuildable());
    }
}
