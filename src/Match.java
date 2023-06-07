import java.util.List;

public class Match {
    //Match id number.
    private final int id;
    //First team of the match.
    private final Team homeTeam;
    //Second team of the match
    private final Team awayTeam;
    //Field for the goals during the match.
    private final List<Goal> goals;

    //Match constructor.
    public Match(int id, Team homeTeam, Team awayTeam, List<Goal> goals) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.goals = goals;
    }

    //Getter for the first team of the match.
    public Team getHomeTeam() {
        return this.homeTeam;
    }

    //Getter for the second team of the match.
    public Team getAwayTeam() {
        return this.awayTeam;
    }

    //Getter for the goal list of the match.
    public List<Goal> getGoals() {
        return this.goals;
    }

    //Getter for the goals of the first team.
    public long getHomeGoals() {
        return this.goals.stream()
                .filter(goal -> goal.getScorer().getTeam().equals(this.homeTeam))
                .count();
    }

    //Getter for the goals of the second team.
    public long getAwayGoals() {
        return this.goals.stream()
                .filter(goal -> goal.getScorer().getTeam().equals(this.awayTeam))
                .count();
    }

    //Getter for match id.
    public int getId() {
        return this.id;
    }


}

