import java.util.*;
import java.util.stream.*;

public class LeagueManager {
    //Field of the matches list in the league.
    private final List<Match> matches;
    //Field of the team list in the league.
    private final List<Team> teams;
    //Field of the players that participate in the league.
    private final List<Player> players;

    //League manager constructor.
    public LeagueManager(List<Match> matches, List<Team> teams, List<Player> players) {
        this.matches = matches;
        this.teams = teams;
        this.players = players;
    }

    //This method returns all the matches of a specific team by its id from the csv file.
    public List<Match> findMatchesByTeam(int teamId) {
        return this.matches.stream()
                .filter(match -> match.getHomeTeam().getId() == teamId || match.getAwayTeam().getId() == teamId)
                .collect(Collectors.toList());
    }

    //This method returns the n teams with the most goals.
    public List<Team> findTopScoringTeams(int n) {
        return this.teams.stream()
                .sorted(Comparator.comparingInt(this::calculateTeamGoals).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }


    //A helper method of findTopScoringTeams method. It calculates the goal amount for some team.
    private int calculateTeamGoals(Team team) {
        int homeGoals = this.matches.stream()
                .filter(match -> match.getHomeTeam().equals(team))
                .mapToInt(match -> Math.toIntExact(match.getHomeGoals()))
                .sum();

        int awayGoals = this.matches.stream()
                .filter(match -> match.getAwayTeam().equals(team))
                .mapToInt(match -> Math.toIntExact(match.getAwayGoals()))
                .sum();

        return homeGoals + awayGoals;
    }

    //This method returns a list for all the players with minimum n goals.
    public List<Player> findPlayersWithAtLeastNGoals(int n) {
        return this.players.stream()
                .filter(player -> this.matches.stream()
                        .flatMap(match -> match.getGoals().stream())
                        .filter(goal -> goal.getScorer().getId() == player.getId())
                        .count() >= n)
                .collect(Collectors.toList());
    }

    //This method returns a team by its position in the league table.
    public Team getTeamByPosition(int position) {
        return teams.stream()
                .sorted((team1, team2) -> {
                    int points1 = calculateTeamPoints(team1);
                    int points2 = calculateTeamPoints(team2);
                    if (points1 != points2) {
                        return points2 - points1;
                    }
                    return calculateGoalDifference(team2) - calculateGoalDifference(team1);
                })
                .skip(position - 1)
                .findFirst()
                .orElse(null);
    }

    //This helper method calculates and returns the number of points for some team.
    private int calculateTeamPoints(Team team) {
        return (int) this.matches.stream()
                .filter(match -> (match.getHomeTeam().equals(team) && match.getHomeGoals() > match.getAwayGoals())
                        || (match.getAwayTeam().equals(team) && match.getAwayGoals() > match.getHomeGoals()))
                .count() * 3
                + (int) this.matches.stream()
                .filter(match -> match.getHomeGoals() == match.getAwayGoals()
                        && (match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team)))
                .count();
    }

    //This helper method calculates the difference between scored goals and received goals for some team
    private int calculateGoalDifference(Team team) {
        int homeGoalDifference = this.matches.stream()
                .filter(match -> match.getHomeTeam().equals(team))
                .mapToInt(match -> (int) (match.getHomeGoals() - match.getAwayGoals()))
                .sum();

        int awayGoalDifference = this.matches.stream()
                .filter(match -> match.getAwayTeam().equals(team))
                .mapToInt(match -> (int) (match.getAwayGoals() - match.getHomeGoals()))
                .sum();

        return homeGoalDifference + awayGoalDifference;
    }

    //This method returns the top n players with the most goals.
    public Map<Integer, Integer> getTopScorers(int n) {
        return this.players.stream()
                .collect(Collectors.toMap(
                        Player::getId,
                        player -> (int) this.matches.stream()
                                .flatMap(m -> m.getGoals().stream())
                                .filter(g -> g.getScorer().equals(player))
                                .count(),
                        Integer::sum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (id, goals) -> id, LinkedHashMap::new));
    }

    //Getter for a player object by his id field.
    public Player getPlayerById(int id) {
        return this.players.stream()
                .filter(player -> player.getId() == id)
                .findFirst()
                .orElse(null);
    }
}





