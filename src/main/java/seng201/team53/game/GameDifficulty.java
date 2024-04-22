package seng201.team53.game;

public enum GameDifficulty {
    EASY(
         "Easy",
         100,
         2.0,
         1.5,
         0.7f),

    NORMAL(
           "Normal",
           100,
           2.0,
           1.5,
           0.7f),

    HARD(
         "Hard",
         100,
         2.0,
         1.5,
         0.7f);

    private final String name;
    private final double startingMoney;
    private final double moneyEarnMultiplier;
    private final double towerReloadMultiplier;
    private final float cartVelocityMultiplier;

    GameDifficulty(
                   String name,
                   double startingMoney,
                   double moneyEarnMultiplier,
                   double towerReloadMultiplier,
                   float cartVelocityMultiplier) {

        this.name = name;
        this.startingMoney = startingMoney;
        this.moneyEarnMultiplier = moneyEarnMultiplier;
        this.towerReloadMultiplier = towerReloadMultiplier;
        this.cartVelocityMultiplier = cartVelocityMultiplier;
    }

    public String getName() {
        return name;
    }

    public double getStartingMoney() {
        return startingMoney;
    }

    public double getMoneyEarnMultiplier() {
        return moneyEarnMultiplier;
    }

    public double getTowerReloadMultiplier() {
        return towerReloadMultiplier;
    }

    public float getCartVelocityMultiplier() {
        return cartVelocityMultiplier;
    }
}
