package seng201.team53.game;

/**
 * An enum representing the difficulty of the game
 * Each difficulty has a set of parameters that affect the game play, such as the starting money, the number of carts,
 * and the money earn multiplier.
 */
public enum GameDifficulty {
    /**
     * Represents the easy difficulty level
     */
    EASY(
         "Easy",
         600,
         0.5,
         200.0,
         1.0,
         1.5,
         0.7f,
         0.25),

    /**
     * Represents the normal difficulty level
     */
    NORMAL(
           "Normal",
           500,
           1.0,
           100.0,
           0.75,
           1.0,
           1.0f,
           0.35),

    /**
     * Represents the hard difficulty level
     */
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

    /**
     * Constructs a new game difficulty level
     * @param name The name of the difficulty
     * @param startingMoney The amount of starting money
     * @param numberOfCartsMultiplier The number of carts multiplier
     * @param moneyEarnMultiplier The amount of money earn multiplier
     * @param sellPriceModifier The sell price modifier
     * @param towerReloadMultiplier The tower reload multiplier
     * @param cartVelocityMultiplier The cart velocity multiplier
     * @param randomEventOdds The odds in which a random event will occur
     */
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

    /**
     * Retrieves the name of difficulty level
     * @return The name of the difficulty
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the amount of money a player starts the game with
     * @return The amount of starting money
     */
    public int getStartingMoney() {
        return startingMoney;
    }

    /**
     * Calculates the number of carts to spawn given a round nunber
     * @param roundNumber The number of round
     * @return The number of carts to spawn.
     */
    public int getNumberOfCarts(int roundNumber) {
        int baseCarts = 2;
        int cartsPerRound = baseCarts + (roundNumber - 1);
        return (int)Math.round(cartsPerRound * numberOfCartsMultiplier);
    }

    /**
     * Retrieves the money earn multiplier which affects the amount of money earned per round
     * This value can be used like money += (roundNumber * moneyEarnMultiplier)
     * @return The money earn multiplier
     */
    public double getMoneyEarnMultiplier() {
        return moneyEarnMultiplier;
    }

    /**
     * Retrieves the sell price modifier which affects the price an item can be sold for
     * @return The sell price modifier
     */
    public double getSellPriceModifier() {
        return sellPriceModifier;
    }

    /**
     * Retrieves the tower reload modifier which affects how fast a tower reloads.
     * Increasing this value will result in towers reloading quicker.
     * @return The tower reload modifier
     */
    public double getTowerReloadModifier() {
        return towerReloadModifier;
    }

    /**
     * Retrieves the cart velocity modifier which affects the velocity of the carts
     * @return The cart velocity multiplier
     */
    public float getCartVelocityMultiplier() {
        return cartVelocityMultiplier;
    }

    /**
     * Retrieves the odds that a random event will occur.
     * @return The odds a random event will occur in the range [0, 1]
     */
    public double getRandomEventOdds() {
        return randomEventOdds;
    }
}
