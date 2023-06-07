import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Main {

    public static void main(String[] args) {
        soccerSimulator();
    }

    //This method responsible for the overall simulation of the soccer league, by reading the data from the csv file
    //and manipulating it to the simulator needs.
    public static void soccerSimulator() {
        List<Team> teams = new ArrayList<>(); //List of the 10 soccer teams.
        Path path = Paths.get(Constants.PATH_TO_TEAMS_CSV);
        try (Stream<String> lines = Files.lines(path)) {
            //Iteration over every line.
            lines.forEach(line -> {
                String[] details = line.split(",");
                //Team id in the csv file.
                int teamId = Integer.parseInt(details[0]);
                //Team name in the csv file.
                String teamName = details[1];
                Team team = new Team(teamId, teamName, new ArrayList<>());
                //Each team will have 15 players.
                List<Player> players = IntStream.range(0, 15)
                        .mapToObj(id -> new Player(id + 1,
                                Constants.FIRST_NAMES.get(Constants.RANDOM.nextInt(Constants.FIRST_NAMES.size())),
                                Constants.LAST_NAMES.get(Constants.RANDOM.nextInt(Constants.LAST_NAMES.size())),
                                team))
                        .collect(Collectors.toList());
                // adding players to the team after they are created
                team.setPlayers(players);
                teams.add(team);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calling the helper method to generate all the matches of the league.
        List<List<Match>> leagueMatches = generateLeagueMatches(teams);

        // Making a list of all matches from all rounds.
        List<Match> allMatches = leagueMatches.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Making a list of players from all the teams.
        List<Player> players = teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .collect(Collectors.toList());

        // Creating a league manager object after we have the lists of all matches teams and players.
        LeagueManager leagueManager = new LeagueManager(allMatches, teams, players);

        // This code is responsible for simulating a round, and inside each there will be simulated five matches.
        leagueMatches.forEach(round -> {
            System.out.println("Starting a new round...");
            round.forEach(Main::simulateMatch);
            // After the loop of the round, there is a call for the league table printer method.
            printLeagueTable(teams);
            // Then it will make an interaction with user to offer him to perform some operations.
            showOptionsForUser(leagueManager);
        });
    }


    //This method is responsible for simulating a single match in some round.
    private static void simulateMatch(Match match) {
        System.out.println("Match between " + match.getHomeTeam().getName() + " VS " + match.getAwayTeam().getName());
        System.out.println("Match in progress...");
        IntStream.rangeClosed(1, 10)
                .map(i -> 10 - i)
                .forEach(i -> {
                    System.out.println(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                });
        // Randomly generate goals and record them in the match
        // Each match can have a maximum of 10 goals to avoid larger numbers.
        int numberOfGoals = Constants.RANDOM.nextInt(10);
        IntStream.range(0, numberOfGoals)
                .mapToObj(number -> {
                    int minute = Constants.RANDOM.nextInt(90);
                    Player scorer = getRandomPlayer(match);
                    return new Goal(number + 1, minute, scorer);
                })
                .forEach(match.getGoals()::add);

        // Calculate the number of goals for the home team and the away team.
        int homeGoals = getGoalsByTeam(match, match.getHomeTeam());
        int awayGoals = getGoalsByTeam(match, match.getAwayTeam());

        // Adding 3 points in case of win, in another case 1 point is added to both teams for a draw.
        if (homeGoals > awayGoals) {
            match.getHomeTeam().setPoints(match.getHomeTeam().getPoints() + 3);
        } else if (homeGoals < awayGoals) {
            match.getAwayTeam().setPoints(match.getAwayTeam().getPoints() + 3);
        } else {
            match.getHomeTeam().setPoints(match.getHomeTeam().getPoints() + 1);
            match.getAwayTeam().setPoints(match.getAwayTeam().getPoints() + 1);
        }
        //calculation of goal difference between the home team and away team.
        match.getHomeTeam().setGoalDifference(match.getHomeTeam().getGoalDifference() + homeGoals - awayGoals);
        match.getAwayTeam().setGoalDifference(match.getAwayTeam().getGoalDifference() + awayGoals - homeGoals);
        //Printing the result of each match.
        System.out.println("Match ended. Result: " + match.getHomeTeam().getName() + " " + homeGoals
                + " - " + match.getAwayTeam().getName() + " " + awayGoals);
    }

    //This helper method is responsible for matching between a random player and a goal in the match simulation.
    private static Player getRandomPlayer(Match match) {
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(match.getHomeTeam().getPlayers());
        allPlayers.addAll(match.getAwayTeam().getPlayers());
        return allPlayers.get(Constants.RANDOM.nextInt(allPlayers.size()));
    }

    //This helper method returns a number of goals scored by some team.
    private static int getGoalsByTeam(Match match, Team team) {
        return (int) match.getGoals().stream()
                .filter(goal -> team.getPlayers().contains(goal.getScorer()))
                .count();
    }

    //This method generates all the matches for the soccer league, and sorts them by 9 rounds.
    public static List<List<Match>> generateLeagueMatches(List<Team> teams) {
        //Total number of teams.
        int numberOfTeams = teams.size();
        //Total number of rounds 10-1 (9).
        return IntStream.range(0, numberOfTeams - 1)
                //convert to Integer object.
                .boxed()
                //Creating 10/2 (5) matches for each round.
                .map(round -> IntStream.range(0, numberOfTeams / 2)
                        //convert to Integer object.
                        .boxed()
                        .map(match -> {
                            //This calculation represents the index of each team and ensures that the every team played
                            //vs another just once (rotation algorithm).
                            int firstTeamIndex = (round + match) % (numberOfTeams - 1);
                            int secondTeamIndex = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);
                            //If the index of the match is 0 (first match), the last team in the list will always play
                            //in this match while the other teams will rotate around it.
                            if (match == 0) {
                                secondTeamIndex = numberOfTeams - 1;
                            }
                            //Creating a new match in the league.
                            return new Match(
                                    //Calculating the match id.
                                    round * numberOfTeams / 2 + match + 1,
                                    teams.get(firstTeamIndex),
                                    teams.get(secondTeamIndex),
                                    new ArrayList<>()
                            );
                        })
                        //Collecting this object to list.
                        .collect(Collectors.toList())
                )
                //Collecting this list of Matches that represent a round to list.
                .collect(Collectors.toList());
    }


    //This method prints the league table on the end of each round.
    public static void printLeagueTable(List<Team> teams) {
        System.out.println("\nLeague Table:");
        System.out.println(Constants.TABLE_PARAMETERS);

        // Objects for sorting parameters of different teams.
        Comparator<Team> compareByPoints = Comparator.comparingInt(Team::getPoints).reversed();
        Comparator<Team> compareByGoalDiff = Comparator.comparingInt(Team::getGoalDifference).reversed();
        Comparator<Team> compareByName = Comparator.comparing(Team::getName);

        // Object for sorting by the hierarchy of the instructions.
        Comparator<Team> sortByHierarchy = compareByPoints.thenComparing(compareByGoalDiff).thenComparing(compareByName);

        // Calculate the longest team name to make sure that it aligned with the table.
        int maxTeamNameLength = teams.stream().mapToInt(team -> team.getName().length()).max().orElse(0);

        // Print the table.
        //I use atomic integer to use its value inside the lambda expression.
        AtomicInteger position = new AtomicInteger(1);
        teams.stream()
                .sorted(sortByHierarchy)
                .map(team -> {
                    String teamName = String.format("%-" + maxTeamNameLength + "s", team.getName());
                    String formattedString = String.format(Constants.TABLE_FORMAT_SPECIFIERS, position.getAndIncrement(), teamName, team.getPoints(), team.getGoalDifference());
                    return formattedString;
                })
                .forEach(System.out::println);
    }


    //This method shows the operation menu for the user at the end of each round.
    //Each case of the switch method runs different operation based on user preference.
    //Note: I can let the user choose just one operation at a round because it is restricted to using regular loops, and
    //by using only stream, I did not find a solution for this case.
    public static void showOptionsForUser(LeagueManager leagueManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose an operation:");
        System.out.println("1: Find matches by team ID");
        System.out.println("2: Find top scoring teams");
        System.out.println("3: Find players with at least n goals");
        System.out.println("4: Get team by position");
        System.out.println("5: Get top scorers");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter team ID:");
                int teamId = scanner.nextInt();
                List<Match> matches = leagueManager.findMatchesByTeam(teamId);
                matches.forEach(match -> System.out.println("Match ID: " + match.getId() + ", Home Team: " + match.getHomeTeam().getName() + ", Away Team: " + match.getAwayTeam().getName()));
            }
            case 2 -> {
                System.out.println("Enter number of top teams:");
                int n = scanner.nextInt();
                if (n > 10) {
                    n = 10;
                }
                List<Team> teams = leagueManager.findTopScoringTeams(n);
                teams.forEach(team -> System.out.println("Team Name: " + team.getName()));
            }
            case 3 -> {
                System.out.println("Enter minimum number of goals:");
                int minGoals = scanner.nextInt();
                List<Player> players = leagueManager.findPlayersWithAtLeastNGoals(minGoals);
                players.forEach(player -> System.out.println("Player Name: " + player.getFirstName() + " " + player.getLastName()));
            }
            case 4 -> {
                System.out.println("Enter league position:");
                int position = scanner.nextInt();
                Team team = leagueManager.getTeamByPosition(position);
                System.out.println("Team Name: " + team.getName());
            }
            case 5 -> {
                System.out.println("Enter number of top scorers:");
                int topScorers = scanner.nextInt();
                Map<Integer, Integer> topScorersMap = leagueManager.getTopScorers(topScorers);
                topScorersMap.forEach((playerId, goals) -> {
                    Player player = leagueManager.getPlayerById(playerId);
                    if (player != null) {
                        System.out.println("Player: " + player + ", Goals: " + goals);
                    }
                });
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}

