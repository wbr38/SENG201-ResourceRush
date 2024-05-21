package seng201.team53.unittests.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TowerTest {

    @BeforeAll
    void beforeAllTests() {
        GameEnvironment.init(null, null, 0, GameDifficulty.NORMAL);
    }

    @Test
    void testDefaults() {
        TowerType towerType = Tower.Type.LUMBER_MILL;
        Tower tower = towerType.create();

        assertEquals(tower.getPurchasableType(), towerType);
        assertFalse(tower.getBrokenProperty().get());
        assertEquals(tower.getXpLevel(), 0);
    }

    @Test
    void testBroken() {
        Tower tower = Tower.Type.LUMBER_MILL.create();
        assertFalse(tower.isBroken());
        tower.setBroken(true);
        assertTrue(tower.isBroken());
    }

    @Test
    void testGenerate() {
        Tower tower = Tower.Type.LUMBER_MILL.create();


        // Not enough time lapsed to generate
        assertFalse(tower.canGenerate());

        // Subtract reload speed from current time so tower should be able to generate again
        long reloadSpeed = tower.getPurchasableType().getReloadSpeed().toMillis();
        tower.setLastGenerateTime(System.currentTimeMillis() - reloadSpeed);
        assertTrue(tower.canGenerate());

        // Reload speed modifier
        reloadSpeed /= 1.2;
        tower.setLastGenerateTime(System.currentTimeMillis() - reloadSpeed);
        tower.resetReloadSpeedModifier();
        assertFalse(tower.canGenerate()); // have not modified, so should fail
        tower.addReloadSpeedModifier();
        assertTrue(tower.canGenerate()); // should pass because modifier was incresased

        // Broken towers can not generate
        tower.setBroken(true);
        assertFalse(tower.canGenerate());
    }
}