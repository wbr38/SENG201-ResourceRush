package seng201.team53.unittests.services;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import seng201.team53.game.GameDifficulty;
import seng201.team53.game.GameEnvironment;
import seng201.team53.items.Purchasable;
import seng201.team53.items.Shop;
import seng201.team53.items.towers.Tower;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShopTest {
    private Shop shop;

    @BeforeEach
    void beforeAllTests() {
        shop = new Shop();
    }

    @Test
    void testAddMoney() {
        // Should start at balance of zero, and test adding $5
        assertEquals(shop.getMoney(), 0);
        shop.addMoney(5);
        assertEquals(shop.getMoney(), 5);
    }

    @Test
    void testSubtractMoney() {
        // Begin by adding $5, and subtract
        shop.addMoney(5);
        assertEquals(shop.getMoney(), 5);

        shop.subtractMoney(5);
        assertEquals(shop.getMoney(), 0);
    }

    @Test
    void testNegativeMoney() {
        // Money should not become negative. We should clamp at zero
        shop.subtractMoney(5);
        assertEquals(shop.getMoney(), 0);
    }

    @Test
    void testMoneyPropertyAdd() {
        final int add = 5;
        IntegerProperty intProp = shop.getMoneyProperty();
        ChangeListener<Number> addListener = new ChangeListener<>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                assertEquals(oldValue.intValue(), 0);
                assertEquals(newValue.intValue(), add);
                intProp.removeListener(this);
            }
        };

        intProp.addListener(addListener);
        shop.addMoney(add);
    }

    @Test
    void testMoneyPropertySubtract() {
        final int add = 5;
        IntegerProperty intProp = shop.getMoneyProperty();
        ChangeListener<Number> addListener = new ChangeListener<>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                assertEquals(oldValue.intValue(), add);
                assertEquals(newValue.intValue(), 0);
                intProp.removeListener(this);
            }
        };

        shop.addMoney(add);
        intProp.addListener(addListener);
        shop.subtractMoney(add);
    }

    @Test
    void testPurchaseItem() {
        // Needed to set GameDifficulty parameter
        GameEnvironment.init(null, null, 0, GameDifficulty.NORMAL);
        Purchasable item = Tower.Type.MINE;

        shop.addMoney(item.getCostPrice());
        shop.purchaseItem(item);
        assertEquals(shop.getMoney(), 0);

        shop.sellItem(item);
        assertEquals(shop.getMoney(), item.getSellPrice());
    }
}
