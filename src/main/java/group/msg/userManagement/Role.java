package group.msg.userManagement;

import lombok.Data;

import java.util.List;

enum RoleType {

    Administrator, ProjectManager, TestManager,
    Developer, Tester

}

enum Rights {

    PERMISSION_MANAGEMENT, USER_MANAGEMENT, BUG_MANAGEMENT,
    BUG_CLOSE, BUG_EXPORT_PDF
}

@Data
public class Role {

    private RoleType roleType;

    private List<Rights> rights;


}
