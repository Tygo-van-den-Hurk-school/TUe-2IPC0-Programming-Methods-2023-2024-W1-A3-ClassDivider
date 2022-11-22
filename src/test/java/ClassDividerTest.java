
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Set;

/**
 * Class that tests ClassDivider.
 *
 * @author Tygo van den Hurk, 1705709
 * @date 18//11/2022
 */
public class ClassDividerTest {

    private int runCounter = 0;
    private ClassDivider instance;
    private PrintGroups groupPrinter = new PrintGroups();

    /**
     * Tests the divide method of the ClassDivider object.
     *
     * @param klas a set of students that we can divide into groups.
     * @param groupSize is the size of the groups the set of students should be divided in, plus
     * minus the deviation.
     * @param deviation the max amount of students a group can miss or have extra.
     */
    private void standartCheckDivide(
            Group<Student> klas,
            int groupSize,
            int deviation,
            boolean expectation
    ) {

        this.instance = new ClassDivider();
        String message = "";
        
        boolean checky =  this.instance.isDividable(klas, groupSize, deviation);
        message = (
                "subTest 1: expectation does not match instance.isDividable"
                + "(klas, groupSize, deviation).\n"
                + "int klas.size() = " + klas.size() + ",\n"
                + "int groupSize = " + groupSize + ",\n"
                + "int deviation = " + deviation + ",\n"
                + "boolean expectation = " + expectation + ",\n"
                + "boolean program = " + checky
                + "\n"
                );

        // subTest 1:
        assertEquals(expectation, checky, message);

        // subTest 2:
        try {
            // subTest 2.1:
            Set<Group<Student>> splittedKlas = this.instance.divide(klas, groupSize, deviation);

            Iterator<Group<Student>> groups = splittedKlas.iterator();
            this.runCounter++;
            System.out.println(
                    "+-----------------------------------------------------------------------+\n"
                    + "    TestRunID = " + this.runCounter + "\n"
                    + "+-----------------------------------------------------------------------+\n"
                    + "int klas.size() = " + klas.size() + ",\n"
                    + "int groupSize = " + groupSize + ",\n"
                    + "int deviation = " + deviation + ";\n"
            );

            /*
             * We will run over every group to see if each group has the right size
             */
            boolean result = true;
            while (groups.hasNext()) {
                /*
                 * the group we will check
                 */
                Group<Student> groupOfStudents = groups.next();
                /* 
                 * If there is one group that is not the right size, then it will be false, since
                 * it is a big conjunction.
                 */
                result = (result
                        && (groupOfStudents.size() <= groupSize + deviation)
                        && (groupOfStudents.size() >= groupSize - deviation));
            }
            /*
             * Now that we have cheched if the result is valid (and therefor True) we will test it,
             * if it is not we print out what the state was using this look a like'dictionary'.
             */
            message = ("dictionary variables = {\n"
                    + "    \"klas\" : \n" + klas.toString() + "\n"
                    + "    \"klas.size()\" : " + klas.size() + ",\n"
                    + "    \"splittedKlas\" : [\n" 
                    + this.groupPrinter.setOfGroupsToString(splittedKlas)
                    + "    ],\n"
                    + "    \"splittedKlas.size()\" : " + splittedKlas.size() + ",\n"
                    + "    \"groupSize\" : " + groupSize + ",\n"
                    + "    \"deviation\" : " + deviation + ",\n"
                    + "    \"TestRunID\" : " + this.runCounter + ",\n"
                    + "};");
            
            assertEquals(
                    true,
                    result,
                    message
            );

        } catch (IllegalArgumentException e) {
            /*
             * if it is not possible to divide it will throw an error, so if it is not possible to 
             * devide we will end up here in the catch, we then see if we expected it to not be 
             * possible to divide, and if it is not possible we are fine, if it was possible when
             * it should not be, we will see the error message:
             */
            //subTest 3:
            message = ("subTest 3: program thought that it was possible to divide the given set "
                    + "into groups in reality it is not possible.");
            assertEquals(false, expectation, message);
        }
    }

    

    private Group<Student> createKlasOfSize(int n) {

        Group<Student> returnKlas = new Group<Student>();

        String[][] firstLastNamePlusID = {
            {"", "", ""}
        /* example:
             * {"crookshanks", "", "cat"},
             * {"Albus", "Dumbledore", "long beard"},
             * {"", "", "heWhoShallNotBeNamed"},
             * {"Severus", "Snape", "proffesorSnake"},
             * {"Draco", "Malfroy", "theSnake"},
             * {"Hermione", "Granger", "theKnowItAll"},
             * {"Ron", "Weasley", "theGinger"},
             * {"Harry", "Potter", "theBoyWhoLived"}
         */
        };

        for (int i = 0; i < n; i++) {

            // pick the information from the array
            String firstName = firstLastNamePlusID[i % firstLastNamePlusID.length][0] + i;
            String lastName = firstLastNamePlusID[i % firstLastNamePlusID.length][1] + i;
            String studentID = firstLastNamePlusID[i % firstLastNamePlusID.length][2] + i;

            Student member = new Student(firstName, lastName, studentID);

            returnKlas.add(member);
        }

        return returnKlas;
    }

    /**
     * Does a few standard tests to the ClassDivide.isDividable method.
     *
     * @pre true
     * @param klas a klas of students to split
     * @post true
     */
    private void standardChecks(Group<Student> klas) {

        this.instance = new ClassDivider();
        boolean result;

        // subTest 1
        result = this.instance.isDividable(klas, klas.size() + 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 1, please check in which method for the specific case"
        );

        // subTest 2
        result = this.instance.isDividable(klas, klas.size() + 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 2, please check in which method for the specific case"
        );

        // subTest 3
        result = this.instance.isDividable(klas, klas.size(), 0);
        assertEquals(
                true,
                result,
                "it failed subTest 3, please check in which method for the specific case"
        );

        /*
         * subtest 4 and5 has to be specific since klas.size() - 1 as the groupSize is not possible
         * if n == 1 sinze that would mean that groupSize == 0, which is an error. That would
         * lead to devision by zero.
         */
        // subTest 6
        result = this.instance.isDividable(klas, klas.size() + 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 6, please check in which method for the specific case"
        );

        // subTest 7
        result = this.instance.isDividable(klas, klas.size() + 20, 20);
        assertEquals(
                true,
                result,
                "it failed subTest 7, please check in which method for the specific case"
        );

        // subTest 8 is under specific
        // subTest 9
        result = this.instance.isDividable(klas, klas.size() - 20, 20);
        assertEquals(
                false,
                result,
                "it failed subTest 9, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest0() {

        Group<Student> klas = createKlasOfSize(1);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                false, // since klas.size() == 1, so klas.size() - 1 == 0, and groupSize > 0
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest1() {

        Group<Student> klas = createKlasOfSize(2);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                true, // since klas.size() - 1 == 1, so it will make 2 groups of 1
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest2() {
        Group<Student> klas = createKlasOfSize(3);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                true, // since group size will be klas.size() - 2 == 1, which is possible (3*1)
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest3() {
        Group<Student> klas = createKlasOfSize(4);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                true, // since group size will be klas.size() - 2 == 2, which is possible (2*2)
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest4() {
        Group<Student> klas = createKlasOfSize(5);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest5() {
        Group<Student> klas = createKlasOfSize(6);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest6() {
        Group<Student> klas = createKlasOfSize(7);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void isDividableTest7() {
        Group<Student> klas = createKlasOfSize(8);

        // try the standart test sweet
        standardChecks(klas);

        this.instance = new ClassDivider();
        boolean result;

        // room for more specific tests:      
        // subTest 4
        result = this.instance.isDividable(klas, klas.size() - 1, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 4, please check in which method for the specific case"
        );

        // subTest 5
        result = this.instance.isDividable(klas, klas.size() - 1, 1);
        assertEquals(
                true,
                result,
                "it failed subTest 5, please check in which method for the specific case"
        );

        // subTest 8
        result = this.instance.isDividable(klas, klas.size() - 2, 0);
        assertEquals(
                false,
                result,
                "it failed subTest 8, please check in which method for the specific case"
        );
    }

    @Test
    public void divideTest0() {
        Group<Student> klas = createKlasOfSize(1);

        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);

    }

    @Test
    public void divideTest1() {
        Group<Student> klas = createKlasOfSize(2);

        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest2() {
        Group<Student> klas = createKlasOfSize(3);

        standartCheckDivide(klas, 3, 0, true);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, false);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest3() {
        Group<Student> klas = createKlasOfSize(4);

        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 0, false);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest4() {
        Group<Student> klas = createKlasOfSize(5);

        standartCheckDivide(klas, 5, 0, true);
        standartCheckDivide(klas, 5, 1, true);
        standartCheckDivide(klas, 4, 0, false);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 0, false);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, false);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest5() {
        Group<Student> klas = createKlasOfSize(6);

        standartCheckDivide(klas, 6, 0, true);
        standartCheckDivide(klas, 6, 1, true);
        standartCheckDivide(klas, 5, 0, false);
        standartCheckDivide(klas, 5, 1, true);
        standartCheckDivide(klas, 4, 0, false);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 0, true);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest6() {
        Group<Student> klas = createKlasOfSize(7);

        standartCheckDivide(klas, 7, 0, true);
        standartCheckDivide(klas, 7, 1, true);
        standartCheckDivide(klas, 6, 0, false);
        standartCheckDivide(klas, 6, 1, true);
        standartCheckDivide(klas, 5, 0, false);
        standartCheckDivide(klas, 5, 1, false);
        standartCheckDivide(klas, 4, 0, false);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 0, false);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, false);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest7() {
        Group<Student> klas = createKlasOfSize(8);

        standartCheckDivide(klas, 8, 0, true);
        standartCheckDivide(klas, 8, 1, true);
        standartCheckDivide(klas, 7, 0, false);
        standartCheckDivide(klas, 7, 1, true);
        standartCheckDivide(klas, 6, 0, false);
        standartCheckDivide(klas, 6, 1, false);
        standartCheckDivide(klas, 5, 0, false);
        standartCheckDivide(klas, 5, 1, true);
        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 0, false);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest8() {
        Group<Student> klas = createKlasOfSize(16);

        standartCheckDivide(klas, 16, 3, true);
        standartCheckDivide(klas, 16, 2, true);
        standartCheckDivide(klas, 16, 1, true);
        standartCheckDivide(klas, 16, 0, true);

        standartCheckDivide(klas, 15, 3, true);
        standartCheckDivide(klas, 15, 2, true);
        standartCheckDivide(klas, 15, 1, true);
        standartCheckDivide(klas, 15, 0, false);

        standartCheckDivide(klas, 14, 3, true);
        standartCheckDivide(klas, 14, 2, true);
        standartCheckDivide(klas, 14, 1, false);
        standartCheckDivide(klas, 14, 0, false);

        standartCheckDivide(klas, 13, 3, true);
        standartCheckDivide(klas, 13, 2, false);
        standartCheckDivide(klas, 13, 1, false);
        standartCheckDivide(klas, 13, 0, false);

        standartCheckDivide(klas, 12, 3, false);
        standartCheckDivide(klas, 12, 2, false);
        standartCheckDivide(klas, 12, 1, false);
        standartCheckDivide(klas, 12, 0, false);

        standartCheckDivide(klas, 11, 3, true);
        standartCheckDivide(klas, 11, 2, false);
        standartCheckDivide(klas, 11, 1, false);
        standartCheckDivide(klas, 11, 0, false);

        standartCheckDivide(klas, 10, 3, true);
        standartCheckDivide(klas, 10, 2, true);
        standartCheckDivide(klas, 10, 1, false);
        standartCheckDivide(klas, 10, 0, false);

        standartCheckDivide(klas, 9, 3, true);
        standartCheckDivide(klas, 9, 2, true);
        standartCheckDivide(klas, 9, 1, true);
        standartCheckDivide(klas, 9, 0, false);

        standartCheckDivide(klas, 8, 3, true);
        standartCheckDivide(klas, 8, 2, true);
        standartCheckDivide(klas, 8, 1, true);
        standartCheckDivide(klas, 8, 0, true);

        standartCheckDivide(klas, 7, 3, true);
        standartCheckDivide(klas, 7, 2, true);
        standartCheckDivide(klas, 7, 1, true);
        standartCheckDivide(klas, 7, 0, false);

        standartCheckDivide(klas, 6, 3, true);
        standartCheckDivide(klas, 6, 2, true);
        standartCheckDivide(klas, 6, 1, false);
        standartCheckDivide(klas, 6, 0, false);

        standartCheckDivide(klas, 5, 3, true);
        standartCheckDivide(klas, 5, 2, true);
        standartCheckDivide(klas, 5, 1, true);
        standartCheckDivide(klas, 5, 0, false);

        standartCheckDivide(klas, 4, 3, true);
        standartCheckDivide(klas, 4, 2, true);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 4, 0, true);

        standartCheckDivide(klas, 3, 3, true);
        standartCheckDivide(klas, 3, 2, true);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 3, 0, false);

        standartCheckDivide(klas, 2, 3, true);
        standartCheckDivide(klas, 2, 2, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 2, 0, true);

        standartCheckDivide(klas, 1, 3, true);
        standartCheckDivide(klas, 1, 2, true);
        standartCheckDivide(klas, 1, 1, true);
        standartCheckDivide(klas, 1, 0, true);
    }

    @Test
    public void divideTest9() {
        
        Group<Student> klas = createKlasOfSize(32);

        standartCheckDivide(klas, 32, 0, true);
        standartCheckDivide(klas, 32, 1, true);

        standartCheckDivide(klas, 31, 0, false);
        standartCheckDivide(klas, 31, 1, true);

        standartCheckDivide(klas, 30, 0, false);
        standartCheckDivide(klas, 30, 1, false);

        standartCheckDivide(klas, 29, 0, false);
        standartCheckDivide(klas, 29, 1, false);

        standartCheckDivide(klas, 28, 0, false);
        standartCheckDivide(klas, 28, 1, false);

        standartCheckDivide(klas, 27, 0, false);
        standartCheckDivide(klas, 27, 1, false);

        standartCheckDivide(klas, 26, 0, false);
        standartCheckDivide(klas, 26, 1, false);

        standartCheckDivide(klas, 25, 0, false);
        standartCheckDivide(klas, 25, 1, false);

        standartCheckDivide(klas, 24, 0, false);
        standartCheckDivide(klas, 24, 1, false);

        standartCheckDivide(klas, 23, 0, false);
        standartCheckDivide(klas, 23, 1, false);

        standartCheckDivide(klas, 22, 0, false);
        standartCheckDivide(klas, 22, 1, false);

        standartCheckDivide(klas, 21, 0, false);
        standartCheckDivide(klas, 21, 1, false);

        standartCheckDivide(klas, 20, 0, false);
        standartCheckDivide(klas, 20, 1, false);

        standartCheckDivide(klas, 19, 0, false);
        standartCheckDivide(klas, 19, 1, false);

        standartCheckDivide(klas, 18, 0, false);
        standartCheckDivide(klas, 18, 1, false);

        standartCheckDivide(klas, 17, 0, false);
        standartCheckDivide(klas, 17, 1, true);

        standartCheckDivide(klas, 16, 0, true);
        standartCheckDivide(klas, 16, 1, true);

        standartCheckDivide(klas, 15, 0, false);
        standartCheckDivide(klas, 15, 1, true);

        standartCheckDivide(klas, 14, 0, false);
        standartCheckDivide(klas, 14, 1, false);

        standartCheckDivide(klas, 13, 0, false);
        standartCheckDivide(klas, 13, 1, false);

        standartCheckDivide(klas, 12, 0, false);
        standartCheckDivide(klas, 12, 1, false);

        standartCheckDivide(klas, 11, 0, false);
        standartCheckDivide(klas, 11, 1, false);

        standartCheckDivide(klas, 10, 0, false);
        standartCheckDivide(klas, 10, 1, false);

        standartCheckDivide(klas, 9, 0, false);
        standartCheckDivide(klas, 9, 1, true);

        standartCheckDivide(klas, 8, 0, true);
        standartCheckDivide(klas, 8, 1, true);

        standartCheckDivide(klas, 7, 0, false);
        standartCheckDivide(klas, 7, 1, true);

        standartCheckDivide(klas, 6, 0, false);
        standartCheckDivide(klas, 6, 1, false);

        standartCheckDivide(klas, 5, 0, false);
        standartCheckDivide(klas, 5, 1, true);

        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);

        standartCheckDivide(klas, 3, 0, false);
        standartCheckDivide(klas, 3, 1, true);

        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);

        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }
}
