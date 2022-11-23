
import java.util.Iterator;
import java.util.Set;

/**
 * helps with dividing a set of students into a set of groups of students where for each group, it
 * in itself is unique, and so are its elements.
 *
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
     * @return {@code \return == true} if and only if it is possible to divide the {@code klas} into
     * n groups of size {@code groupSize} plus minus the allowed {@code deviation}, else it returns
     * false.
     */
    public boolean isDividable(Group<Student> klas, int groupSize, int deviation) {
        /*
         * for easiness sake we'll use classSize instead of klas.size() using this variable:
         */
        int klasSize = klas.size();
        /*
         * first we watch for obvious non posisible combinations.
         * for example its is not possible to make a group of 100
         * when the klas.size() is only 5 and the allowed deviation
         * is 0, or devision by zero. That is just not possible.
         */
        boolean groupSizeToLarge = klasSize < groupSize - deviation;
        boolean groupSizeToSmall = groupSize + deviation <= 0;
        boolean devisionByZero = groupSize <= 0;
        // guard statement
        if (groupSizeToLarge || groupSizeToSmall || devisionByZero) {
            return false;
        }
        /*
         * Now we will take a look at if it is possible based on a few cases:
         *
         * If we have no left overs, then it is definitely posible to create those groups
         */
        int amountLeftOvers = this.amountLeftover(klasSize, groupSize);
        boolean noLeftOver = amountLeftOvers == 0;
        /* 
         * If we have leftovers, but it is possible to create a group that is large enough, such
         * that we can create another group with them, then there are no left overs, and thus we
         * conclude that is possible to create those groups and so we return true.
         */
        boolean leftOversAreGroup = this.isValidGroup(
                amountLeftOvers,
                groupSize,
                deviation
        );
        /*
         * Otherwise, there are two more ways to deal with the leftovers:
         * 
         *  - Either we add them to the already made groups;
         *      This would mean that including the deviation into the group size 
         *      there is enough room into all the groups such that it fits.
         *
         * So we check if we can fit them into other groups with the 
         * extra tolerance the deviation in other groups gives us:
         */
        boolean spreadableOverOtherGroups = this.canSpreadLeftOvers(
                amountLeftOvers,
                this.amountOfGroups(klasSize, groupSize),
                deviation
        );
        /*
         * - Or we steal members from other groups until the leftover group is big enough.
         *      That would only be possible if the total diviation from all
         *      the groups is bigger or equal to the amound of students the 
         *      leftover groups is still missing.
         *
         * that would mean that the amount of people we can take from other groups, plus the amount
         * people we can steal from other groups is also a valid group:
         */
        int stolenPeoplePlusLeftovers = (this.amountOfGroups(klasSize, groupSize) * deviation
                + amountLeftOvers);
        /*
         * now we check if that is a valid group:
         */
        boolean leftOverGroupIsFillable = this.isValidGroup(
                stolenPeoplePlusLeftovers,
                groupSize,
                deviation
        );
        /*
         * Now we see if any of these cases was true, and if it is, then it must be possible to
         * divide with the given parameters:
         */
        return (leftOversAreGroup || spreadableOverOtherGroups || leftOverGroupIsFillable);
    }

    /**
     * splits the set of students into n {@code Groups} of {@code groupSize} {@code Student}'s, plus
     * minus the allowed {@code deviation}.
     *
     * @pre {@code
     *      klas != null
     *      &&
     *      groupSize > 0
     *      &&
     *      groupSize > deviation
     *      &&
     *      !this.isDividable(
     *          klas,
     *          groupSize,
     *          deviation
     *      )
     * }
     * @param klas is the set of students to divide into groups.
     * @param groupSize is the size of the groups to create.
     * @param deviation is the max amount of students a group can miss or have extra.
     * @return a set of groups that all have a big enough group size, and all have unique elements
     * and are all unique themselves.
     * @post {@code (\forall i; (\result).contains(i);
     *   \not (\exists j; (\result).contains(j);
     *     i != j && (\exists s; i.contains(s); j.contains(s))))
     *   && The students are divided randomly over all groups in \result.}
     * @throws IllegalArgumentException when with the current arguments, a division is not possible.
     */
    public Set<Group<Student>> divide(Group<Student> klas, int groupSize, int deviation) {
        /*
         * first we check if it is possible to devide, and if now we throw an
         * IllegalArgumentException.
         * / // we cant because of Npath, but I would have liked to be able to.
        if (!this.isDividable(klas, groupSize, deviation)) {
            String errorMessage = ("With the given arguments, it is not possible to divide "
                    + "the groups.");
            throw new IllegalArgumentException(errorMessage);
        }
        /*
         * now that we know it is possible to divide we will make an attempt:
         *
         * for easiness sake we'll use classSize instead of klas.size() using this variable:
         */
        int klasSize = klas.size();
        /* 
         * first we create a group of, groups of students. This is the Object we wish to return 
         * when all the code has run.
         */
        Group<Group<Student>> returnGroups = new Group<>();
        /*
         * To go through all the students at random, we will need a random iterator. We will use
         * the one provided by the groups object.
         */
        Iterator<Student> students = klas.iterator();
        /*
         * amount of groups that definitely fits is basically: {@code Math.floor( klas.size() / 
         * groupSize)}. This also means that there might be a few leftovers, but we will deal 
         * with these later. In the case distinction.
         */
        int amountOfStartGroups = amountOfGroups(klasSize, groupSize);
        /*
         * Now we'll have to create n studentGroups, where n == AmountOfStartGroups.
         */
        for (int i = 0; i < amountOfStartGroups; i++) {
            /*
             * we'll create a new group
             */
            Group<Student> studentGroup = new Group<>();
            /*
             * and we'll add the GroupSize amount of students to it.
             */
            for (int j = 0; j < groupSize; j++) {
                // now we get the student,
                Student student = students.next();
                // and place it in a group:
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
         *   Case II:   we dump the leftovers to other groups, since they have place.
         *   Case III:  ???
         *
         * now we check in which case we are:
         *
         * Case I: There are no leftovers
         */
        int amountOfLeftovers = this.amountLeftover(klasSize, groupSize);
        if (amountOfLeftovers == 0) {
            /*
             * Since there is no one left to divde, we return our result.
             */
            return returnGroups;
        }
        /*
         * if not, we check the next case:
         *
         * Case II: we can dump all the remaining members to other groups.
         */
        if (this.canSpreadLeftOvers(
                amountOfLeftovers,
                amountOfStartGroups,
                deviation
        )) {
            /*
             * If we can dump the students to the other groups, we proceed:
             */
            returnGroups = this.addToOtherGroups(
                    returnGroups,
                    students,
                    klas,
                    groupSize,
                    deviation
            );

            // then everything should be done, and we can return the result.
            return returnGroups;
            /*
             * else my thing is not bullet proof, and so we throw an Exception:
             */
        } else {
            /*
             * if not we add them to their own group:
             */
            returnGroups = this.groupOfcombinedLeftovers(
                    returnGroups,
                    students,
                    klas
            );
            /*
             * then we return the result
             */
            return returnGroups;
        }
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
     * @return A group of students, where all the elements within are unique. only appear once in
     * total.
     */
    protected Group<Group<Student>> groupOfcombinedLeftovers(
            Group<Group<Student>> returnGroups,
            Iterator<Student> students,
            Group<Student> klas
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
        // we add the group, and return the result:
        returnGroups.add(leftoversGroup);
        // we return the result
        return returnGroups;
    }

    /**
     * Spreads the remaining students over the groups that are already made.
     *
     * @param returnGroups is the set of groups that is already created
     * @param students is the iterator to go over the set of students in the klas
     * @param klas is a set of students.
     * @param groupSize is the size a group should be.
     * @param deviation is the max amount a group of students can miss or have extra.
     * @return the set of groups of students, for which each group is unigue and so are the students
     * within them, with each group having a size of {@code groupSize} plus minus deviation.
     */
    protected Group<Group<Student>> addToOtherGroups(
            Group<Group<Student>> returnGroups,
            Iterator<Student> students,
            Group<Student> klas,
            int groupSize,
            int deviation
    ) {
        Iterator<Group<Student>> groups = returnGroups.iterator();
        /*
         * We'll go over all the groups to add the left overs until non are left without a group
         */
        while (students.hasNext()) {
            Group<Student> group = groups.next();
            group.add(students.next());

            /*
             * to make sure we never run out of groups to add to, we'll reset the iterator, if it
             * had all the groups before.
             */
            if (!groups.hasNext()) {
                groups = returnGroups.iterator();
            }
        }
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
    private int amountOfGroups(int klasSize, int groupSize) {
        return klasSize / groupSize;
    }

    /**
     * calculates the amount of students that will be left over if you were to make n full groups of
     * students. Where n == klasSize / groupSize.
     *
     * @pre {@code groupSize > 0 && klasSize > 0}
     * @param klasSize the size of the klas of students
     * @param groupSize the size of the groups in the klas
     * @return {@code \return == klasSize % groupSize}
     */
    protected int amountLeftover(int klasSize, int groupSize) {
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
        return ((requestedGroupSize - requestedDeviation <= currentGroupSize)
                && (currentGroupSize <= requestedGroupSize + requestedDeviation));
    }

    /**
     * Checks if it possible to spread the students over different groups.
     *
     * @param leftOvers is the amount of students without a group.
     * @param amountOfGroupsMade is the size a group such be plus minus the deviation.
     * @param deviation is the max amount of students a group can be missing or have extra.
     * @return {@code
     *     ((deviation * amountOfGroupsMade) >= leftOvers)
     * }
     */
    protected boolean canSpreadLeftOvers(int leftOvers, int amountOfGroupsMade, int deviation) {
        return ((deviation * amountOfGroupsMade) >= leftOvers);
    }
}
