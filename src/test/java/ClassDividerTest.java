
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
        /*
         * else we procceed:
         *
         * subTest 1:
         *
         * SetUp:
         */
        Set<Group<Student>> splittedKlas = this.instance.divide(klas, groupSize, deviation);
        Iterator<Group<Student>> groups = splittedKlas.iterator();
        this.runCounter++;
        /*
         * print what it is working on:
         */
        System.out.println(
                "+-----------------------------------------------------------------------+\n"
                + "    TestRunID = " + this.runCounter + "\n"
                + "+-----------------------------------------------------------------------+\n"
                + "int klas.size() = " + klas.size() + ",\n"
                + "int groupSize = " + groupSize + ",\n"
                + "int deviation = " + deviation + ";\n"
        );
        /*
         * we'll use these variables to keep track of what is done well
         */
        boolean goodSize = this.checkSize(groups, groupSize, deviation);
        boolean noDoubles = this.checkForDoubles(splittedKlas);
        /*
         * if the size was good, and there are no doubles, then the result is also good, 
         * otherwise, the result is bad.
         */
        boolean result = goodSize && noDoubles;
        /*
         * Now that we have cheched if the result is valid (and therefor True) we will test it,
         * if it is not we print out what the state was using this look a like'dictionary'.
         */
        message = ("dictionary variables = {\n"
                + "    \"klas\" : " + klas.toString() + "\n"
                + "    \"klas.size()\" : " + klas.size() + ",\n"
                + "    \"splittedKlas\" : [\n" 
                + this.setOfGroupsToString(splittedKlas)
                + "    ],\n"
                + "    \"splittedKlas.size()\" : " + splittedKlas.size() + ",\n"
                + "    \"groupSize\" : " + groupSize + ",\n"
                + "    \"deviation\" : " + deviation + ",\n"
                + "    \"TestRunID\" : " + this.runCounter + ",\n"
                + "    \"result\" : " + result + ",\n"
                + "    \"goodSize\" : " + goodSize + ",\n"
                + "    \"noDoubles\" : " + noDoubles + ",\n"
                + "};");

        assertEquals(
                expectation,
                result,
                message
        );
    }
    
    private boolean checkSize(Iterator<Group<Student>> groups, int groupSize, int deviation) {
        boolean goodSize = true;
        /*
         * We will run over every group to see if each group has the right size
         */
        while (groups.hasNext()) {
            /*
             * the group we will check
             */
            Group<Student> groupOfStudents = groups.next();
            /* 
             * If there is one group that is not the right size, then it will be false, since
             * it is a big conjunction.
             */
            goodSize = (goodSize
                    && (groupOfStudents.size() <= groupSize + deviation)
                    && (groupOfStudents.size() >= groupSize - deviation));
        }
        return goodSize;
    }
    
    private boolean checkForDoubles(Set<Group<Student>> splittedKlas) {
        boolean noDoubles = true;
        /*
         * now that we have checked the size of all groups, we will see if they containany
         * students that are also in other groups:
         */
        for (Group<Student> group1 : splittedKlas) {
            for (Group<Student> group2 : splittedKlas) {
                if (!(group1.equals(group2))) {
                    noDoubles = noDoubles && noDoubleGanger(group1, group2);
                }
            }
        }
        return noDoubles;
    }
    
    private boolean noDoubleGanger(Group<Student> group1, Group<Student> group2) { 
        for (Student s : group1) { // \exists s
            if (group2.contains(s)) {
                return false;
            }
        }
        return true;
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
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest3() {
        Group<Student> klas = createKlasOfSize(4);

        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);
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
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest5() {
        Group<Student> klas = createKlasOfSize(6);

        standartCheckDivide(klas, 6, 0, true);
        standartCheckDivide(klas, 6, 1, true);
        standartCheckDivide(klas, 5, 1, true);
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
        standartCheckDivide(klas, 6, 1, true);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 3, 1, true);
        standartCheckDivide(klas, 2, 1, true);
        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    @Test
    public void divideTest7() {
        Group<Student> klas = createKlasOfSize(8);

        standartCheckDivide(klas, 8, 0, true);
        standartCheckDivide(klas, 8, 1, true);
        standartCheckDivide(klas, 7, 1, true);
        standartCheckDivide(klas, 5, 1, true);
        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);
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

        standartCheckDivide(klas, 14, 3, true);
        standartCheckDivide(klas, 14, 2, true);

        standartCheckDivide(klas, 13, 3, true);


        standartCheckDivide(klas, 11, 3, true);

        standartCheckDivide(klas, 10, 3, true);
        standartCheckDivide(klas, 10, 2, true);

        standartCheckDivide(klas, 9, 3, true);
        standartCheckDivide(klas, 9, 2, true);
        standartCheckDivide(klas, 9, 1, true);

        standartCheckDivide(klas, 8, 3, true);
        standartCheckDivide(klas, 8, 2, true);
        standartCheckDivide(klas, 8, 1, true);
        standartCheckDivide(klas, 8, 0, true);

        standartCheckDivide(klas, 7, 3, true);
        standartCheckDivide(klas, 7, 2, true);
        standartCheckDivide(klas, 7, 1, true);

        standartCheckDivide(klas, 6, 3, true);
        standartCheckDivide(klas, 6, 2, true);

        standartCheckDivide(klas, 5, 3, true);
        standartCheckDivide(klas, 5, 2, true);
        standartCheckDivide(klas, 5, 1, true);

        standartCheckDivide(klas, 4, 3, true);
        standartCheckDivide(klas, 4, 2, true);
        standartCheckDivide(klas, 4, 1, true);
        standartCheckDivide(klas, 4, 0, true);

        standartCheckDivide(klas, 3, 3, true);
        standartCheckDivide(klas, 3, 2, true);
        standartCheckDivide(klas, 3, 1, true);

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

        standartCheckDivide(klas, 31, 1, true);














        standartCheckDivide(klas, 17, 1, true);

        standartCheckDivide(klas, 16, 0, true);
        standartCheckDivide(klas, 16, 1, true);

        standartCheckDivide(klas, 15, 1, true);






        standartCheckDivide(klas, 9, 1, true);

        standartCheckDivide(klas, 8, 0, true);
        standartCheckDivide(klas, 8, 1, true);

        standartCheckDivide(klas, 7, 1, true);


        standartCheckDivide(klas, 5, 1, true);

        standartCheckDivide(klas, 4, 0, true);
        standartCheckDivide(klas, 4, 1, true);

        standartCheckDivide(klas, 3, 1, true);

        standartCheckDivide(klas, 2, 0, true);
        standartCheckDivide(klas, 2, 1, true);

        standartCheckDivide(klas, 1, 0, true);
        standartCheckDivide(klas, 1, 1, true);
    }

    /**
     * This gets the ID's of every student in a Group, then puts that into an array, and then puts
     * all those array's for every group into an Array of it self, and then returns it as a String.
     *
     * @pre {@code splittedKlas != null && splittedKlas.contains(group)} where
     * {@code group == Group<Students> && group != null && group.contains(student)} where
     * {@code Student != null}
     * @param splittedKlas a Set of groups of students.
     * @return a string what looks like an array of array's of Students.
     */
    private String setOfGroupsToString(Set<Group<Student>> splittedKlas) {
        String returnString = "";
        /*
         * We get an iterator to go over the groups
         */
        Iterator<Group<Student>> groups = splittedKlas.iterator();
        /*
         * for as long as there groups, we go over them
         */
        while (groups.hasNext()) {
            /*
             * we pick the group
             */
            Group<Student> group = groups.next();
            /*
             * and go over it at random
             */
            Iterator<Student> students = group.iterator();
            /*
             * we begin the array
             */
            returnString = returnString + "        [ ";
            /*
             * while there are students, we print them
             */
            while (students.hasNext()) {
                /*
                 * we get the student
                 */
                Student student = students.next();
                /*
                 * and add their ID to the String and seperate it with the Sting ", "
                 */
                returnString = returnString + student.getID() + ", ";
            }
            /*
             * we close the this array
             */
            returnString = returnString + " ],\n";
        }
        // we return the result
        return returnString;
    }
}
