import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Class that tests ClassDivider.
 * 
 * @author Tygo van den Hurk, 1705709
 * @date 18//11/2022
 */
public class ClassDividerTest {
    
    ClassDivider instance;
    
    /**
     * method returns a klas of n students that are inspired by harry potter.
     * 
     * @param n the amount of students the klas needs.
     * @pre {@code n < 9 && n > 0}.
     * @return klas with n students.
     * @throws outOfBoundsExecepition when {@code n > 8}.
     */
    private Group<Student> createKlasOfSize(int n) {
        
        Group<Student> returnKlas = new Group<Student>();
        
        String[][] firstLastNamePlusID = {
            {"crookshanks", "", "cat"},
            {"Albus", "Dumbledore", "long beard"},
            {"", "", "heWhoShallNotBeNamed"},
            {"Severus", "Snape", "proffesorSnake"},
            {"Draco", "Malfroy", "theSnake"},
            {"Hermione", "Granger", "theKnowItAll"},
            {"Ron", "Weasley", "theGinger"},
            {"Harry", "Potter", "theBoyWhoLived"}
        };
                
        for (int i = 0; i < n; i++) {
            
            // pick the information from the array
            String firstName = firstLastNamePlusID[i][0];
            String lastName = firstLastNamePlusID[i][1];
            String studentID = firstLastNamePlusID[i][2];

            Student member = new Student(firstName, lastName, studentID);
            
            returnKlas.add(member);
        }
        
        return returnKlas;
    }
        
    /**
     * Does a few standart tests to the ClassDivide.isDividable method
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
            true,
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
    
    /**
     * Tests the divide method of the ClassDivider object.
     * 
     * @param klas a set of students that we can divide into groups.
     * @param groupSize is the size of the groups the set of students should be divided in, plus 
     * minus the deviation.
     * @param deviation the max amount of students a group can miss or have extra.
     */
    private void standartCheckDivide(Set<Student> klas, int groupSize, int deviation, boolean x){
        this.instance = new ClassDivider();

        set<Group<Student>> splittedKlas = this.instance.divide(klas, groupSize, deviation);
        
        // we will use the and on this time and time again, and then see if it still true.
        boolean result = true;
        
        for ( groupOfStudents : splittedKlas) {
            
            result = (
                result
                &&
                (groupOfStudents.size() >= groupSize + deviation)
                &&
                (groupOfStudents.size() <= groupSize - deviation)
            );
        }
        
        String message = splittedKlas.toSting();
        
        assertEquals(x, result, "this was the object: \n\n" + splittedKlas + "\n\n");
    }
            
    @Test
    public void divideTest0() {
        Group<Student> klas = createKlasOfSize(1);
        
        this.instance = new ClassDivider();
        
        // subtest 1
        standartCheckDivide(klas, 1, 0, true);
    }
}
