package zentasks;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import zentasks.models.Task;

/**
 *
 * @author miyabetaiji
 */
public class TaskItem implements Initializable, AccessibleRootNode {

    private Task task;
    
    @FXML
    private HBox root;

    @FXML
    private Label titleLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @Override
    public HBox getRootNode() { return root; }

    public void setTask(Task task) {
        this.task = task;
        titleLabel.setText(task.getTitle());
    }
}
