package seng201.team53;

public enum GameDifficulty {

    EASY(
            "Easy",
            100,
            2.0,
            1.5,
            0.7),

    NORMAL(
            "Normal",
            100,
            2.0,
            1.5,
            0.7),

    HARD(
            "Hard",
            100,
            2.0,
            1.5,
            0.7);

    public final String name;
    public final double startingMoney;
    public final double moneyEarnMultiplier;
    public final double towerReloadMultiplier;
    public final double cartVelocityMultiplier;

    GameDifficulty(
            String name,
            double startingMoney,
            double moneyEarnMultiplier,
            double towerReloadMultiplier,
            double cartVelocityMultiplier) {

        this.name = name;
        this.startingMoney = startingMoney;
        this.moneyEarnMultiplier = moneyEarnMultiplier;
        this.towerReloadMultiplier = towerReloadMultiplier;
        this.cartVelocityMultiplier = cartVelocityMultiplier;
    }
}
