import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Constants {
    //Lists of first names and last names of random football players.
    public static final List<String> FIRST_NAMES = Arrays.asList("Baruch", "Idan", "Esi", "Yonatan", "Mai", "Nirel", "Rudi", "Omer", "Dolev", "Yarden", "Ofir", "Illay", "Bar", "Shai", "Adi", "Sergei", "Or", "Ofri", "Roy", "Dorothy", "Michael", "Nehoray", "Dan", "Yosef", "Yossi", "Haim", "Eli", "Boris", "Yehuda", "Benjamin");
    public static final List<String> LAST_NAMES = Arrays.asList("Dego", "Shreekey", "Bozaglo", "Revivo", "Mazal", "Mahfud", "Hadad", "Atzili", "Haziza", "Shua", "Kriaf", "Madmon", "Cohen", "Givati", "Tamir", "Zahavi", "Boaron", "Sason", "Sirakovich", "Dabush", "Azaria", "Benayoun", "Revivo", "Ohana", "Krihalli", "Shnaps", "Gamliel", "Lieberman", "Perez", "Golan");
    //Random object for randomizing different outputs in the simulator.
    public static final Random RANDOM = new Random();
    //The parameters for different values in the league table.
    public static final String TABLE_PARAMETERS = "Position\tTeam\t\t\tPoints\tGoal Difference";
    //A string that represents the format of the league table to make everything look symmetric.
    //%d is a place for an int, and %-20s is a place for a string with max length of 20 characters.
    public static final String TABLE_FORMAT_SPECIFIERS = "%d\t\t%-20s\t%d\t%d";
    //Path to the teams csv file.
    public static final String PATH_TO_TEAMS_CSV = "teams.csv";

}
