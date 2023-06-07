import java.util.List;

public class Team {
    //Team id number.
    private final int id;
    //Name of the team.
    private final String name;
    //Players of the team.
    private List<Player> players;
    //Field that holds the goal difference for each team.
    private int goalDifference;
    //Field that holds the points for each team.
    private int points;

    //Team constructor.
    public Team(int id, String name, List<Player> players) {
        this.id = id;
        this.name = name;
        this.players = players;
    }

    //Getter for team id.
    public int getId() {
        return this.id;
    }

    //Getter for team name.
    public String getName() {
        return this.name;
    }

    //getter for the players of this team.
    public List<Player> getPlayers() {
        return this.players;
    }

    //This method is used to add a player list to some team after the players are randomly generated in the main class.
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    //Setting the new goal difference after each match
    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }
    //Getter for goal difference.
    public int getGoalDifference() {
        return this.goalDifference;
    }
    //Setter for points of some team.
    public void setPoints(int points) {
        this.points = points;
    }
    //Getter for points of some team.
    public int getPoints() {
        return this.points;
    }
}
