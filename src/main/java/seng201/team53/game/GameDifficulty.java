package seng201.team53.game;

public enum GameDifficulty {
    EASY(
         "Easy",
         400,
         2.0,
         1.5,
         0.7f,
         0.25),

    NORMAL(
           "Normal",
           300,
           100.0,
           1.0,
           1.0f,
           0.35),

    HARD(
         "Hard",
         200,
         2.0,
         0.75,
         1.25f,
         0.45);

    private final String name;
    private final int startingMoney;
    private final double moneyEarnMultiplier;
    private final double towerReloadModifier;
    private final float cartVelocityMultiplier;
    private final double randomEventOdds;

    GameDifficulty(
                   String name,
                   int startingMoney,
                   double moneyEarnMultiplier,
                   double towerReloadMultiplier,
                   float cartVelocityMultiplier,
                   double randomEventOdds
    ) {
        this.name = name;
        this.startingMoney = startingMoney;
        this.moneyEarnMultiplier = moneyEarnMultiplier;
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
     * @return The mulitplier used to calculate the amount of money earned per round. Formula: money += (roundNumber * moneyEarnMultiplier)
     */
    public double getMoneyEarnMultiplier() {
        return moneyEarnMultiplier;
    }

    /**
     * @return The number that each tower's reload speed will be divided by.
     * Increasing this value will result in towers reloading quicker.
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
