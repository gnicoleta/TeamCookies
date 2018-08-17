package group.msg.blogic;

import group.msg.entities.RightType;
import group.msg.entities.RoleType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RightsForRoleGetterAndSetter implements Serializable {

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
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).equals(rightType)) {
                return i;
            }
        }
        return -1;


    }

    public boolean addRight(RightType rightType, int listIndex) {


        int index = findRightIndex(rightType, listOfLists.get(listIndex));
        if (index == -1) {
            listOfLists.get(listIndex).add(rightType);
            return true;
        } else {

            return false;
        }
    }

    public boolean deleteADMright(RightType rightType, int listIndex) {

        int index = findRightIndex(rightType, listOfLists.get(listIndex));
        if (index == -1) {
            return false;
        } else {
            listOfLists.get(listIndex).remove(index);
            return true;
        }
    }

    public List<RightType> getRights(RoleType roleType) {
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
        return  null;
    }


}
