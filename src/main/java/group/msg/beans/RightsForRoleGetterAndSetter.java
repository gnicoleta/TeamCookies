package group.msg.beans;

import group.msg.entities.RightType;
import group.msg.entities.RoleType;
import lombok.Data;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;


public class RightsForRoleGetterAndSetter implements Serializable {

    @Inject
    private Logger logger;

    private List<RightType> admRights = new ArrayList<>();
    private List<RightType> pmRights = new ArrayList<>();
    private List<RightType> tmRights = new ArrayList<>();
    private List<RightType> devRights = new ArrayList<>();
    private List<RightType> testRights = new ArrayList<>();

    private List<List<RightType>> listOfLists = new ArrayList<>();

    {
        admRights.add(RightType.PERMISSION_MANAGEMENT);
        admRights.add(RightType.USER_MANAGEMENT);

        pmRights.add(RightType.BUG_MANAGEMENT);
        pmRights.add(RightType.BUG_CLOSE);
        pmRights.add(RightType.BUG_EXPORT_PDF);

        tmRights.add(RightType.BUG_MANAGEMENT);
        tmRights.add(RightType.BUG_CLOSE);
        tmRights.add(RightType.BUG_EXPORT_PDF);

        devRights.add(RightType.BUG_MANAGEMENT);
        devRights.add(RightType.BUG_EXPORT_PDF);

        testRights.add(RightType.BUG_MANAGEMENT);
        testRights.add(RightType.BUG_EXPORT_PDF);


        listOfLists.add(admRights);
        listOfLists.add(pmRights);
        listOfLists.add(tmRights);
        listOfLists.add(devRights);
        listOfLists.add(testRights);

    }


    private int findRightIndex(RightType rightType, List<RightType> list) {
        try {
            try {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (list.get(i).equals(rightType)) {
                        return i;
                    }
                }
            } catch (NullPointerException e) {
                logger.info(Arrays.toString(e.getStackTrace()));
            }
        } catch (Exception e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return -1;

    }

    public boolean addRight(RightType rightType, int listIndex) {
        int index = 0;

        try {
            index = findRightIndex(rightType, listOfLists.get(listIndex));
        } catch (IndexOutOfBoundsException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        if (index == -1) {
            listOfLists.get(listIndex).add(rightType);
            return true;
        } else {

            return false;
        }
    }

    public boolean deleteADMright(RightType rightType, int listIndex) {

        int index = -1;
        try {
            index = findRightIndex(rightType, listOfLists.get(listIndex));
        } catch (IndexOutOfBoundsException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        if (index == -1) {
            return false;
        } else {
            listOfLists.get(listIndex).remove(index);
            return true;
        }
    }

    public List<RightType> getRights(RoleType roleType) {
        try {
            if (roleType.equals(RoleType.ADM)) {
                return admRights;
            }

            if (roleType.equals(RoleType.PM)) {
                return pmRights;
            }
            if (roleType.equals(RoleType.TM)) {

                return tmRights;
            }
            if (roleType.equals(RoleType.DEV)) {
                return devRights;
            }
            if (roleType.equals(RoleType.TEST)) {
                return testRights;
            }
        } catch (NullPointerException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }


}
