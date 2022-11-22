
import java.util.Iterator;
import java.util.Set;

/**
 * converts Sets or Groups of Groups or students to a String, only for the tests
 * @author Tygo van den Hurk
 */
public class PrintGroups {
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
    public String setOfGroupsToString(Set<Group<Student>> splittedKlas) {
        return setOfGroupsToString(splittedKlas);
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
    public String setOfGroupsToString(Group<Group<Student>> splittedKlas) {
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
    public String groupsToString(Set<Student> klas) {
        return groupsToString(klas);
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
    public String groupToString(Group<Student> klas) {
        String returnString = "";
        /*
         * We get an iterator to go over the groups
         */
        Iterator<Student> students = klas.iterator();
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
        returnString = returnString + " ]";
        // we return the result
        return returnString;
    }
}
