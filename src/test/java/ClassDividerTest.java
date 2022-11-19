import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

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
     * @pre {@code n < 9 && n > 0}
     * @return klas with n students
     */
    private Group<Student> createKlasOfSize(int n) {
        
        Group<Student> returnKlas = new Group<Student>();
        
        /*
         * Thanks to the FallThrough macanic of the switch, it will go from the top,
         * n steps down, creating n students in the process without any duplicate code.
         * the problem with this approch is that moto moto does not like theses FallThroughs
         * and sees them as checkSytle errors.
         */
        switch (n) {
            case 8:
                Student crookshanks = new Student("crookshanks", "", "cat");
                returnKlas.add(crookshanks);
            case 7:
                Student profAlbus = new Student("Albus", "Dumbledore", "long beard");
                returnKlas.add(profAlbus);                
            case 6:
                Student heWhoShallNotBeNamed = new Student("", "", "heWhoShallNotBeNamed");
                returnKlas.add(heWhoShallNotBeNamed);                
            case 5:
                Student profSnape = new Student("Severus", "Snape", "proffesorSnake");
                returnKlas.add(profSnape);
            case 4:
                Student dracoMalfroy = new Student("Draco", "Malfroy", "theSnake");
                returnKlas.add(dracoMalfroy);
            case 3:
                Student hermioneGranger = new Student("Hermione", "Granger", "theKnowItAll");
                returnKlas.add(hermioneGranger);
            case 2:
                Student ronWeasley = new Student("Ron", "Weasley", "theGinger");
                returnKlas.add(ronWeasley);
            case 1:
                Student harryPotter = new Student("Harry", "Potter", "theBoyWhoLived");
                returnKlas.add(harryPotter);
                break;
            default:
                System.out.println("That was not a legal number of students.");
        }
        
        return returnKlas;
    }
        
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
}
