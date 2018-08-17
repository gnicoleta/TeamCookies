package group.msg.beans;

import group.msg.entities.Bug;
import group.msg.entities.StatusType;
import group.msg.entities.User;
import lombok.Data;
import org.primefaces.model.chart.*;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@ManagedBean
@Named
@ViewScoped
@Data
public class ReportBean implements Serializable {

    @EJB
    private BugServiceEJB bugServiceEJB;

    private PieChartModel pieChartModel;
    private BarChartModel barModel;
    private BarChartModel barModel2;

    @EJB
    private UserServiceEJB userServiceEJB;

    private int newBug, inProgressBug, fixedBug, closedBug, rejectedBug, infoNeededBug;

    @PostConstruct
    public void init() {
        createPieChart();
        createBarModels();
    }

    public void createBarModels() {
        createBarModel();
        createBarModel2();
    }

    public void createPieChart() {
        newBug = inProgressBug = fixedBug = closedBug = rejectedBug = infoNeededBug = 0;
        for (Bug bug : bugServiceEJB.getAllBugs()) {
            if (bug.getStatusType().equals(StatusType.NEW))
                newBug++;
            else if (bug.getStatusType().equals(StatusType.IN_PROGRESS))
                inProgressBug++;
            else if (bug.getStatusType().equals(StatusType.FIXED))
                fixedBug++;
            else if (bug.getStatusType().equals(StatusType.CLOSED))
                closedBug++;
            else if (bug.getStatusType().equals(StatusType.REJECTED))
                rejectedBug++;
            else if (bug.getStatusType().equals(StatusType.INFO_NEEDED))
                infoNeededBug++;
        }
        pieChartModel = new PieChartModel();
        pieChartModel.set(StatusType.NEW.toString(), newBug);
        pieChartModel.set(StatusType.IN_PROGRESS.toString(), inProgressBug);
        pieChartModel.set(StatusType.FIXED.toString(), fixedBug);
        pieChartModel.set(StatusType.CLOSED.toString(), closedBug);
        pieChartModel.set(StatusType.REJECTED.toString(), rejectedBug);
        pieChartModel.set(StatusType.INFO_NEEDED.toString(), infoNeededBug);

        pieChartModel.setTitle("Bug status report");
        pieChartModel.setLegendPosition("w");
        pieChartModel.setShadow(false);
    }

    public PieChartModel getPiecharModel() {
        return pieChartModel;
    }

    private BarChartModel initBarModel() {
        int fixedBugCount = 0;
        BarChartModel model = new BarChartModel();
        ChartSeries userBugFIxed = new ChartSeries();
        for (User user : userServiceEJB.getAllUsers()) {
            for (Bug bug : user.getAssignedBugs()) {
                if (bug.getStatusType().equals(StatusType.FIXED))
                    fixedBugCount++;
            }
            userBugFIxed.set(user.getUsername(), fixedBugCount);
            fixedBugCount = 0;
        }
        userBugFIxed.setLabel("Fixed Bugs");
        model.addSeries(userBugFIxed);
        return model;
    }

    private BarChartModel initBarModel2() {
        int createdBugCount = 0;
        int rejectedBugCount = 0;

        BarChartModel model = new BarChartModel();

        ChartSeries bugsRejected = new ChartSeries();
        ChartSeries bugsCreated = new ChartSeries();
        for (Bug bug : bugServiceEJB.getAllBugs()) {
            createdBugCount++;
            if (bug.getStatusType().equals(StatusType.REJECTED)) {
                rejectedBugCount++;
            }
            bugsCreated.set("", createdBugCount);
            bugsRejected.set("", rejectedBugCount);
        }
        bugsCreated.setLabel("Created Bugs");
        bugsRejected.setLabel("Rejected Bugs");
        model.addSeries(bugsCreated);
        model.addSeries(bugsRejected);
        return model;
    }
    private void createBarModel() {
        barModel = initBarModel();
        barModel.setTitle("Fixed bugs for each user.");
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Username");

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("No. of fixed bugs");
        yAxis.setMin(0);
        yAxis.setMax(30);
    }

    private void createBarModel2() {
        barModel2 = initBarModel2();

        barModel2.setTitle("Created/Rejected bugs");
        barModel2.setLegendPosition("ne");

        Axis yAxis = barModel2.getAxis(AxisType.Y);
        yAxis.setLabel("No. of created/rejected bugs");
        yAxis.setMin(0);
        yAxis.setMax(30);
    }
}