package seng201.team53.game;

public enum GameDifficulty {
    EASY(
         "Easy",
         600,
         0.5,
         200.0,
         1.0,
         1.5,
         0.7f,
         0.25),

    NORMAL(
           "Normal",
           500,
           1.0,
           100.0,
           0.75,
           1.0,
           1.0f,
           0.35),

    // TESTING(
    //        "TESTING",
    //        10000, 
    //        1.0,
    //        100.0,
    //        0.75,
    //        1.0,
    //        3.0f,
    //        0.35),

    HARD(
         "Hard",
         400,
         1.5,
         200.0,
         0.5,
         0.75,
         1.25f,
         0.45);

    private final String name;
    private final int startingMoney;
    private final double numberOfCartsMultiplier;
    private final double moneyEarnMultiplier;
    private final double sellPriceModifier;
    private final double towerReloadModifier;
    private final float cartVelocityMultiplier;
    private final double randomEventOdds;

    GameDifficulty(
                   String name,
                   int startingMoney,
                   double numberOfCartsMultiplier,
                   double moneyEarnMultiplier,
                   double sellPriceModifier,
                   double towerReloadMultiplier,
                   float cartVelocityMultiplier,
                   double randomEventOdds
    ) {
        this.name = name;
        this.startingMoney = startingMoney;
        this.numberOfCartsMultiplier = numberOfCartsMultiplier;
        this.moneyEarnMultiplier = moneyEarnMultiplier;
        this.sellPriceModifier = sellPriceModifier;
        this.towerReloadModifier = towerReloadMultiplier;
        this.cartVelocityMultiplier = cartVelocityMultiplier;
        this.randomEventOdds = randomEventOdds;
    }

    public String getName() {
        return name;
    }

    /**
     * @return The amount of money the player starts the game with
     */
    public int getStartingMoney() {
        return startingMoney;
    }

    /**
     * @return The number of carts to spawn.
     */
    public int getNumberOfCarts(int roundNumber) {
        int baseCarts = 2;
        int cartsPerRound = baseCarts + (roundNumber - 1);
        return (int)Math.round(cartsPerRound * numberOfCartsMultiplier);
    }

    /**
     * @return The mulitplier used to calculate the amount of money earned per round. Formula: money += (roundNumber * moneyEarnMultiplier)
     */
    public double getMoneyEarnMultiplier() {
        return moneyEarnMultiplier;
    }

    /**
     * @return The sell back price of items in the shop is calculated by dividing the cost price by this number.
     */
    public double getSellPriceModifier() {
        return sellPriceModifier;
    }

    /**
     * @return The number that each tower's reload speed will be divided by.
     *         Increasing this value will result in towers reloading quicker.
     */
    public double getTowerReloadModifier() {
        return towerReloadModifier;
    }

    public float getCartVelocityMultiplier() {
        return cartVelocityMultiplier;
    }

    public double getRandomEventOdds() {
        return randomEventOdds;
    }
}
