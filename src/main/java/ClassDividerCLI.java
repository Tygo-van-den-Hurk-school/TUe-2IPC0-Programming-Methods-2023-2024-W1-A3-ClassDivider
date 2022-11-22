
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

/**
 * ClassDivider–Divide a class of students into groups.
 *
 * @version 0.10
 * @author Huub de Beer
 * @author Tygo van den Hurk
 *
 * I am adding my name since I wrote some of the code now.
 */
@Command(
        name = "classdivider",
        mixinStandardHelpOptions = true,
        version = "classdivider 0.10",
        description = "Divide a class of students into groups.")
public class ClassDividerCLI implements Callable<Integer> {

    /*
     * Size of the groups to create.
     */
    @CommandLine.Option(
            names = {"-g", "--group-size"},
            description = "target group size.",
            required = true
    )
    private int groupSize;

    /*
     * Number of students that a group can deviate from the target group size.
     *
     * For example, if the group size is 10 and the deviation is 2, all groups in the group set will
     * have sizes between 8 and 12. Defaults to 1.
     */
    @CommandLine.Option(
            names = {"-d", "--deviation"},
            description = "permitted difference of number of students in a group "
            + " and the target group size. Defaults to ${DEFAULT-VALUE}.")
    private int deviation = 1;

    /*
     * Path to file containing student data
     */
    @Parameters(
            index = "0",
            description = "path to file with students data in CSV format."
    )
    private Path studentsFile;

    @Spec
    CommandSpec commandSpec; // injected by picocli

    /*
     * the class devider object that will check if with the current parameters the class is 
     * devidable, and devide it if so.
     */
    private final ClassDivider classDivider = new ClassDivider();

    /**
     * imports the class, and validates user input from it.
     * 
     * @pre true
     */
    private Group<Student> importClass() {
        // Validate user input
        try {
            return StudentsFile.fromCSV(this.studentsFile);
        } catch (IOException e) {
            throw new ParameterException(
                    this.commandSpec.commandLine(),
                    "Unable to open or read students file '%s': %s."
                            .formatted(this.studentsFile, e));
        }
    }
    
    @Override
    public Integer call() {
        
        // Note. "klas" is Dutch for "class". We cannot use "class" because it is a Java keyword.
        Group<Student> klas = importClass();
        
        /*
         * first we check if the devision is indeed possible, if not we throw a exception.
         */
        if (!classDivider.isDividable(klas, groupSize, deviation)) {
            throw new IllegalArgumentException(
                    "With these parameters, it is not possible to devide."
            );
        }

        /*
         * the object groups will be used to store a set of groups of students.
         */
        Set<Group<Student>> groups;

        groups = classDivider.divide(klas, groupSize, deviation);

        // now we finish by printing the groups
        this.printGroups(groups);

        return 0;
    }

    private void printGroups(Set<Group<Student>> groups) {

        // to keep track of index of the for loop
        int index = 0;

        for (Group<Student> group : groups) {
            index++;
            System.out.print("\nGroup " + index + ":\n");
            for (Student student : group) {
                System.out.print(" - " + student.sortName() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ClassDividerCLI()).execute(args);
        System.exit(exitCode);
    }
}
