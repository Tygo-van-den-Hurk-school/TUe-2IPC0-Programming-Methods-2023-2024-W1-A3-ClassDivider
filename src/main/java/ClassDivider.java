import java.util.Set;

/*
 * @author Tygo van den Hurk, 1705709
 * @date 18//11/2022
 */
public class ClassDivider {
    
    /**
     * Checks if it is possible to divide a set of students into n groups of size {@code groupsize}
     * plus minus the allowed {@code deviation}.
     * 
     * @param klas is the set of students to divide into groups.
     * @param groupSize is the size of the groups to create.
     * @param deviation is the max amount of students a group can miss or have extra.
     * @pre {@code groupSize > 0}
     * @return {@code \return == true} if and only if it is possible to divide the {@code klas} 
     * into n groups of size {@code groupSize} plus minus the allowed {@code deviation}, else it
     * returns false.
     */
    public boolean isDividable(Group<Student> klas, int groupSize, int deviation) {
        
        /*if (klas.size() < groupSize){ // we might need this.
            return false;
        }*/
        
        /*
         * If we have no left overs, then it is definitely posible to create those groups
         */
        boolean noLeftovers = klas.size() % groupSize == 0;
        
        /* 
         * If we have leftovers, but it is possible to create a group that is large enough, but not
         * to large that it is to big such that we can create another group with them, then there
         * are no left overs, and thus we conclude that is possible to create those groups and so 
         * we return true.
         */
        int amountLeftOvers = klas.size() % groupSize;
        boolean leftoverCanBeGroup = (
            amountLeftOvers >= groupSize - deviation ||
            amountLeftOvers <= groupSize + deviation
        );
               
        /*
         * Otherwise, there are two ways to deal with the leftovers:
         * 
         *  - Either we add them to the already made groups;
         *      This would mean that including the deviation into the group size 
         *      there is enough room into all the groups such that it fits.
         *  - Or we steal members from other groups until the leftover group is big enough.
         *      That
         * 
         * If either of these are possible, then we can conclude we can make these groups and so
         * we return true.
         */
        
        /*
         * we check if we can fit them into other groups with the 
         * extra tolerance the deviation gives us
         */
        int groupsMade = klas.size() / groupSize;
        int extraTolerance = deviation * groupsMade;
        boolean otherGroupsCanHaveThem = extraTolerance >= amountLeftOvers;

        /*
         * we check if we can steal enough students from other groups to fill up the group of 
         * leftovers such that it is a full enough group so that it is possible.
         */
        int smallestGroupCreatable = groupSize - deviation;
        int extraAmountStudentNeeded = smallestGroupCreatable - amountLeftOvers;
        boolean enoughStudentsTofillGroupLeftovers = extraAmountStudentNeeded <= extraTolerance;
        
        boolean groupable = (
            noLeftovers || leftoverCanBeGroup || 
            otherGroupsCanHaveThem || enoughStudentsTofillGroupLeftovers
        );
        
        return groupable;
    }
    
    /**
     * splits the set of students into n {@code Groups} of {@code groupSize}
     * {@code Student}'s, plus minus the allowed {@code deviation}.
     * 
     * @param klas is the set of students to divide into groups.
     * @param groupSize is the size of the groups to create.
     * @param deviation is the max amount of students a group can miss or have extra.
     * @pre {@code groupSize > 0 && groupSize > deviation}
     * @return a set of groups that all have a big enough group size.
     */
    public Set<Group<Student>> divide(Group<Student> klas, int groupSize, int deviation) { 
        
        Set<Group<Student>> returnGroups = new Group<Group<Student>>();
        
        /* 
         * amount of groups that definitly fits, since it's basically:
         * Math.floor(klas.size() / groupSize)
         * /
        int AmountOfStartGroups = klas.size() / groupSize;

        klas.iterator();
        
        for (int i = 0; i < AmountOfStartGroups; i++) {
                      
            Group<Student> group = new Group<Student>();

            // now we add students to every group
            for (int j = 0; j < groupSize; j++) {
                Student member = klas.pick();
                group.add(member);
                klas.remove(member);
                returnGroups.add(group);
            }
        }*/
        
        return returnGroups;
    }
}
