package zentasks;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import zentasks.models.Task;

/**
 *
 * @author miyabetaiji
 */
public class TaskItem extends Controller {

    private Task task;
    
    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setTask(Task task) {
        this.task = task;
        titleLabel.setText(task.getTitle());
    }
}
