package seng201.team53.unittests.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.state.CartState;
import seng201.team53.items.Cart;
import seng201.team53.items.ResourceType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartTest {

    Cart cart;

    final int maxCapacity = 5;
    final float velocity = 1.0f;
    final ResourceType resourceType = ResourceType.ENERGY;
    final int spawnAfterTicks = 10;
    
    @BeforeAll
    void beforeAllTests() {
        GameEnvironment.init(null, null, 0, GameDifficulty.NORMAL);
    }

    @BeforeEach
    void beforeEachTest() {
        cart = new Cart(maxCapacity, velocity, resourceType, spawnAfterTicks);
    }

    @Test
    void testDefaults() {
        assertEquals(cart.getMaxCapacity(), maxCapacity);
        assertEquals(cart.getVelocity(), velocity, 0.0);
        assertEquals(cart.getResourceType(), resourceType);
        assertEquals(cart.getSpawnAfterTicks(), spawnAfterTicks);
        
        assertEquals(cart.getCartState(), CartState.WAITING);
    }

    @Test
    void testVelocityModifier() {
        float oldVelocity = cart.getVelocity();
        assertEquals(cart.getVelocityModifier(), 1.0f, 0.0);

        // Velocity should now be slower
        cart.decreaseVelocityModifier();
        assertTrue(oldVelocity > cart.getVelocity());
    }

    @Test
    void testAddResource() {
        assertEquals(cart.getCapacity(), 0);
        assertFalse(cart.isFull());

        // Try to fill cart with wrong resource
        final ResourceType wrongResource = ResourceType.WOOD;
        assertNotEquals(wrongResource, cart.getResourceType()); // sanity check
        for (int i = 0; i < maxCapacity; i++)
            cart.addResource(wrongResource);

        assertFalse(cart.isFull());

        // Fill cart with correct resource
        for (int i = 0; i < maxCapacity; i++)
            cart.addResource(resourceType);

        assertTrue(cart.isFull());

    }

    @Test
    void testFill() {
        int oldPoints = GameEnvironment.getGameEnvironment().getPoints();

        assertFalse(cart.isFull());
        cart.fill();
        assertTrue(cart.isFull());

        // Verify that points were given for filling the cart
        int newPoints = GameEnvironment.getGameEnvironment().getPoints();
        assertTrue(newPoints > oldPoints);
    }

    @Test
    void testTick() {
        // Default
        assertEquals(cart.getCartState(), CartState.WAITING);

        // Update cart state to traversing path once spawnAfterTicks is reached
        cart.tick(spawnAfterTicks - 1);
        assertEquals(cart.getCartState(), CartState.WAITING);

        cart.tick(spawnAfterTicks);
        assertEquals(cart.getCartState(), CartState.TRAVERSING_PATH);
    }
}
