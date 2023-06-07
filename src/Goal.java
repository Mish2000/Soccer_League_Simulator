public class Goal {
    //Goal id (not used) as we do not need it to make the necessary functionality to the program.
    private int id;
    //Goal minute (not used) as we do not need it to make the necessary functionality to the program.
    private int minute;
    //A player object of the goalscorer.
    private final Player scorer;

    //Goal constructor.
    public Goal(int id, int minute, Player scorer) {
        this.id = id;
        this.minute = minute;
        this.scorer = scorer;
    }

    //Getter of the goalscorer.
    public Player getScorer() {
        return this.scorer;
    }

}
