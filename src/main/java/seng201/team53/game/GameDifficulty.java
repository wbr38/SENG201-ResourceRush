package seng201.team53.game;

public enum GameDifficulty {
    EASY(
            "Easy",
            100,
            2.0,
            1.5,
            0.7f,
            0.25),

    NORMAL(
            "Normal",
            100,
            2.0,
            1.5,
            0.7f,
            0.35),

    HARD(
            "Hard",
            100,
            2.0,
            1.5,
            0.7f,
            0.45);

    private final String name;
    private final double startingMoney;
    private final double moneyEarnMultiplier;
    private final double towerReloadMultiplier;
    private final float cartVelocityMultiplier;
    private final double randomEventOdds;

    GameDifficulty(
            String name,
            double startingMoney,
            double moneyEarnMultiplier,
            double towerReloadMultiplier,
            float cartVelocityMultiplier, double randomEventOdds) {

        this.name = name;
        this.startingMoney = startingMoney;
        this.moneyEarnMultiplier = moneyEarnMultiplier;
        this.towerReloadMultiplier = towerReloadMultiplier;
        this.cartVelocityMultiplier = cartVelocityMultiplier;
        this.randomEventOdds = randomEventOdds;
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
    public double getRandomEventOdds() {
        return randomEventOdds;
    }
}
