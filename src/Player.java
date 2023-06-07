

public class Player {
    //Players id number.
    private final int id;
    //Player first name.
    private final String firstName;
    //Player last name.
    private final String lastName;
    //Team of the player.
    private final Team team;

    //Player constructor.
    public Player(int id, String firstName, String lastName, Team team) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
    }

    //Getter for players id.
    public int getId() {
        return this.id;
    }

    //Getter for first name.
    public String getFirstName() {
        return this.firstName;
    }

    //Getter for last name.
    public String getLastName() {
        return this.lastName;
    }

    //Getter for a players team.
    public Team getTeam() {
        return this.team;
    }

    //This method returns players full name.
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

}

