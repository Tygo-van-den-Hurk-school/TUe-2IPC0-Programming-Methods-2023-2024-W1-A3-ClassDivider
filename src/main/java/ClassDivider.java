import java.util.Iterator;
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
     * @pre {@code 
     *   (klas != null && klas.size() > 0)
     *   && 
     *   (groupSize > 0 && groupsize - devitation < klas.size())
     *   &&
     *   (devitation < groupSize && devitation >= 0)
     * }
     * @param klas is the set of students to divide into groups.
     * @param groupSize is the size of the groups to create.
     * @param deviation is the max amount of students a group can miss or have extra.
     * @return {@code \return == true} if and only if it is possible to divide the {@code klas} 
     * into n groups of size {@code groupSize} plus minus the allowed {@code deviation}, else it
     * returns false.
     */
    public boolean isDividable(Group<Student> klas, int groupSize, int deviation) {
        
        /*
         * first we watch for obvious non posisible combinations.
         * for example its is not possible to make a group of 100
         * when the klas.size() is only 5 and the allowed deviation
         * is 0, or devision by zero. That is just not possible.
         */
        boolean groupSizeToLarge = klas.size() < groupSize - deviation;
        boolean groupSizeToSmall = groupSize + deviation <= 0;
        boolean devisionByZero = groupSize == 0;
        
        // guard statement
        if (groupSizeToLarge || groupSizeToSmall || devisionByZero) {
            return false;
        }
        
        /*
         * If we have no left overs, then it is definitely posible to create those groups
         */
        int amountLeftOvers = this.amountLeftover(klas.size(), groupSize);
        boolean noLeftovers = (amountLeftOvers == 0);
        
        /* 
         * If we have leftovers, but it is possible to create a group that is large enough, but not
         * to large that it is to big such that we can create another group with them, then there
         * are no left overs, and thus we conclude that is possible to create those groups and so 
         * we return true.
         */
        boolean fitsLowerLimit = amountLeftOvers >= groupSize - deviation;
        boolean fitsUpperLimit =  amountLeftOvers <= groupSize + deviation;
        boolean fits = fitsLowerLimit && fitsUpperLimit;
               
        /*
         * Otherwise, there are two ways to deal with the leftovers:
         * 
         *  - Either we add them to the already made groups;
         *      This would mean that including the deviation into the group size 
         *      there is enough room into all the groups such that it fits.
         *  - Or we steal members from other groups until the leftover group is big enough.
         *      That would only be possible if the total diviation from all
         *      the groups is bigger or equal to the amound of students the 
         *      leftover groups is still missing.
         * 
         * These are different methods but they outcome is identical, one option just perferes
         * larger groups, and the other perferes more groups. If one of these is possible, then
         * both are possible. So that means we only have to do one of them to check for both.
         *
         * So we check if we can fit them into other groups with the 
         * extra tolerance the deviation gives us:
         */
        int groupsMade = this.amountOfGroups(klas.size(), groupSize);
        int extraTolerance = deviation * groupsMade;
        boolean otherGroupsCanHaveThem = extraTolerance >= amountLeftOvers; 
           
        /* 
         * we now check if any of the above given solutions are a possible, and if so then it 
         * returns true, otherwise, it returns false.
         */
        return (noLeftovers || fits || otherGroupsCanHaveThem);
    }
    
    /**
     * splits the set of students into n {@code Groups} of {@code groupSize}
     * {@code Student}'s, plus minus the allowed {@code deviation}.
     * 
     * @pre {@code groupSize > 0 && groupSize > deviation}
     * @param klas is the set of students to divide into groups.
     * @param groupSize is the size of the groups to create.
     * @param deviation is the max amount of students a group can miss or have extra.
     * @return a set of groups that all have a big enough group size, and all have unique elements
     * and are all unique themselves.
     */
    public Set<Group<Student>> divide(Group<Student> klas, int groupSize, int deviation) { 
        
        /*
         * first we create a group of, groups of students. This is the Object we wish to return 
         * when all the code has run. For now this group is ofcourse empty, since we have not added
         * any groups of students to this.
         */
        Group<Group<Student>> returnGroups = new Group<>();
        
        /*
         * first we check if the devision is indeed possible, if not we return an empty group.
         */
        if (!this.isDividable(klas, groupSize, deviation)) {
            System.out.print("this is not possible.");
            return returnGroups;
        }
        
        /*
         * to go through all the students, we will need a random iterator.
         */
        Iterator<Student> students = klas.iterator();
        
        /** 
         * amount of groups that definitely fits is basically: {@code Math.floor( klas.size() / 
         * groupSize)}, this also means that there might be a few leftovers, but we will deal 
         * with these later.
         */
        int amountOfStartGroups = amountOfGroups(klas.size(), groupSize);
        
        for (int i = 0; i < amountOfStartGroups; i++) {
                      
            Group<Student> studentGroup = new Group<>();

            // now we add students to every group.
            for (int j = 0; j < groupSize; j++) {
                Student student = students.next();
                studentGroup.add(student);
            }
            
            // now we add the created group to the set of groups.
            returnGroups.add(studentGroup);
        }
        
        /*
         * We Have now created n studentGroups, where n == AmountOfStartGroups.
         * We now have m leftover students, where m == klas.size() % groupSize.
         * We now have one of three senerairios:
         *   case I:    there are no leftOvers and the groups is complete.
         *   Case II:   there are leftovers, but there are enough to make a group of, and we can then call
         *              it a day, and return the groups.
         *   Case III:  there are not enough left overs to make a group out of, but there are enough 
         *              students in other groups that we can steal to fill up this group.
         *
         * the most efficent way to in my opinion do this is to make a few if statements, and then 
         * make a method call depending on the case, and then return the outcome of that method.
         */
        int leftOvers = amountLeftover(klas.size(), groupSize);
        /*
         * now we check in which case we are:
         *
         * if(leftOvers == 0){ // Case I:
         *    // do nothing and let it reach the return statement , since the groups are already
         *    // done, and no extra actions are needed.
         *   
         * } else ...
         */
        if (this.isValidGroup(leftOvers, groupSize, deviation)) { // Case II:
            // add all the leftovers to their own group and then add that to the set of groups.
            returnGroups.add(
                this.groupOfcombinedLeftovers(
                    returnGroups,
                    students,
                    klas,
                    groupSize,
                    deviation
                )
            );
        
        } else if (this.canSpreadLeftOvers(leftOvers, amountOfStartGroups, deviation)) { // Case III:
            /* 
             * we now make a group of the left overs and fill it by stealing members from other 
             * groups, using the stealFromOtherGroups method.
             */
            returnGroups = this.stealFromOtherGroups(
                returnGroups,
                students,
                klas,
                groupSize,
                deviation
            );
        }
        
        // then everything should be done, and we can return the result.
        return returnGroups;
    }
    
    /**
     * fills the leftover group with everyone that is still remaining without a group.
     * 
     * @pre {@code 
     *     (returnGroups != null && students != null)
     *     &&
     *     (klas != null && groupSize != 0)
     * }
     * @param returnGroups is the set of groups that is already created
     * @param students is the iterator to go over the set of students in the klas
     * @param klas is a set of students.
     * @param groupSize is the size a group should be.
     * @param deviation is the max amount a group of students can miss or have extra.
     * @return A group of students, where all the elements within are unique.
     * only appear once in total.
     */
    protected Group<Student> groupOfcombinedLeftovers(
        Group<Group<Student>> returnGroups,
        Iterator<Student> students,
        Group<Student> klas,
        int groupSize,
        int deviation
    ) {
        /*
         * we create an new group where we put the leftovers in.
         */
        Group<Student> leftoversGroup = new Group<>();

        /*
         * while there are still students without a group, we add them to the left over group.
         */
        while (students.hasNext()) {
            leftoversGroup.add(students.next());
        }
        
        // we return the result
        return leftoversGroup;
    }
    
    /**
     * creates a group of students that did not have a group before, then fills it up with students
     * from other groups until it is big enough, it then returns this, so that it has created n 
     * groups with each having x students plus minus the deviation.
     * 
     * @param returnGroups
     * @param students
     * @param klas
     * @param groupSize
     * @param deviation
     * @return the set of groups of students, for which each group is unigue and so are the 
     * students within them, with each group having a size of {@code groupSize} plus minus 
     * deviation.
     */
    protected Group<Group<Student>> stealFromOtherGroups(
        Group<Group<Student>> returnGroups,
        Iterator<Student> students,
        Group<Student> klas,
        int groupSize,
        int deviation
    ) {
        
        Group<Student> leftoversGroup = groupOfcombinedLeftovers(
            returnGroups, 
            students, 
            klas,
            groupSize, 
            deviation
        );
       
        /*
         * we now need n students to complete this leftover group. thankfully there are x 
         * groups we can steal students from. Since if we steal from all the groups an equal
         * amount of students, they will form a new group together with the leftovers that meets
         * the requirements, as well as keeps the requirements for the other groups in tact.
         */
        int Leftovers = amountLeftover(klas.size(), groupSize);
        int studentsToComplete = (groupSize - deviation) - Leftovers;

        Iterator<Group<Student>> returnGroupsIterator = returnGroups.iterator();

        /*
         * now we just go over the groups at random, taking one from them until the leftover 
         * group is filled up enough.
         */
        for (int studentStolen = 0; studentStolen < studentsToComplete; studentsToComplete++) {
            Group<Student> groupToStealFrom = returnGroupsIterator.next();
            Student member = groupToStealFrom.pick();
            groupToStealFrom.remove(member);
            leftoversGroup.add(member);
        }

        /* 
         * we add the leftover group to the group of groups and then we are done
         */
        returnGroups.add(leftoversGroup);
            
        // we return the result
        return returnGroups;
    }
    
    /**
     * calculates the amount of groups that are full that can definitely be made.
     * 
     * @pre {@code groupSize > 0 && klasSize > 0}
     * @param klasSize the size of the klas of students
     * @param groupSize the size of the groups in the klas
     * @return {@code \return == klasSize / groupSize}
     */
    protected int amountOfGroups(int klasSize, int groupSize){
        return klasSize / groupSize;
    }
    
    /**
     * calculates the amount of students that will be left over if you were to make n full 
     * groups of students. Where n == klasSize / groupSize.
     * 
     * @pre {@code groupSize > 0 && klasSize > 0}
     * @param klasSize the size of the klas of students
     * @param groupSize the size of the groups in the klas
     * @return {@code \return == klasSize % groupSize}
     */
    protected int amountLeftover(int klasSize, int groupSize){
        return klasSize % groupSize;
    }
    
    /** 
     * returns whether or not the group that is currently is the right size. Meaning it cannot be to
     * small, nor to big.
     * 
     * @param currentGroupSize the size of the group of students that has been created.
     * @param requestedGroupSize the size of the groups that are requested.
     * @param requestedDeviation is the max amount of students a group can miss or have extra.
     * @return Whether a given group has a valid size, with respect to it's given groupSize and its
     * requested Deviation.
     */
    protected boolean isValidGroup(
        int currentGroupSize,
        int requestedGroupSize,
        int requestedDeviation
    ) {
        return (
            (requestedGroupSize - requestedDeviation <= currentGroupSize)
            &&
            (currentGroupSize <= requestedGroupSize + requestedDeviation)
        );
    }
    
    /**
     * checks if it possible to spread the students over different groups
     * 
     * @param leftOvers is the amount of students without a group.
     * @param groupsize is the size a group such be plus minus the deviation.
     * @param deviation is the max amount of students a group can be missing or have extra.
     * @return {@code
     *     ((deviation * amountOfGroups) >= leftOvers)
     * }
     */
    protected boolean canSpreadLeftOvers(int leftOvers, int amountOfGroups, int deviation) {
        return ((deviation * amountOfGroups) >= leftOvers);
    }
}