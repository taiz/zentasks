package zentasks;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import zentasks.models.Project;
import zentasks.models.Task;

/**
 *
 * @author miyabetaiji
 */
public class ProjectBoard extends Controller {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private Dashboard dashboard;
    
    // Call from Dashboard
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @FXML
    private Hyperlink projectLink;
    
    @FXML
    private void showTasks() {
        dashboard.buildTaskBoard(project);
    }
    private Project project;
    
    // Call from Dahsborad
    public void setProject(Project project) {
        this.project = project;
        this.projectLink.setText(project.getName());
    }

    @FXML
    private VBox taskItemsPane;
    
    // Call from Dahsborad
    public void setTasks(List<Task> tasks) {
        taskItemsPane.getChildren().clear();
        try {
            for (Task task : tasks) {
                TaskItem taskItem = createTaskItem(task);
                taskItemsPane.getChildren().add(taskItem.getRoot());
            }
        } catch (FXMLLoadException ex) {
            Logger.getLogger(TaskBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private TaskItem createTaskItem(Task task) throws FXMLLoadException {
        TaskItem taskItem = (TaskItem)Util.loadFXML(this, "TaskItem.fxml");
        taskItem.setTask(task);
        return taskItem;
    }
}
