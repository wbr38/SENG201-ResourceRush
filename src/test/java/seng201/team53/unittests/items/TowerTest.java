package seng201.team53.unittests.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seng201.team53.game.GameEnvironment.getGameEnvironment;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.items.Cart;
import seng201.team53.game.items.towers.Tower;
import seng201.team53.game.items.towers.TowerType;
import seng201.team53.game.state.CartState;
import seng201.team53.game.state.GameState;

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
    void testGenerate() {
        getGameEnvironment().setupNextRound();
        List<Cart> carts = getGameEnvironment().getRound().getCarts();
        Tower tower = Tower.Type.LUMBER_MILL.create();

        // Fail: GameState is not ROUND_ACTVIE
        assertFalse(tower.canGenerate());
        getGameEnvironment().getStateHandler().setState(GameState.ROUND_ACTIVE);

        // Fail: No carts traversing path
        carts.get(0).setCartState(CartState.WAITING);
        assertFalse(tower.canGenerate());
        carts.get(0).setCartState(CartState.TRAVERSING_PATH);

        // Can now generate
        assertTrue(tower.canGenerate());

        // Test tower broken
        tower.setBroken(true);
        assertFalse(tower.canGenerate());
        tower.setBroken(false);
        assertTrue(tower.canGenerate());

        // Test Inventory
        tower.setInInventory(true);
        assertFalse(tower.canGenerate());
        tower.setInInventory(false);
        assertTrue(tower.canGenerate());

        // Make the available cart full, should now fail
        carts.get(0).fill();
        assertFalse(tower.canGenerate());
    }

}