package zentasks;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
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
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private HBox detailsPane;
    
    void setTask(Task task) {
        this.task = task;
        titleLabel.setText(task.getTitle());
        if (task.getAssignedTo() != null)
            detailsPane.getChildren().add(createAssingLable(task.getAssignedTo().getEmail()));
        if (task.getDueDate() != null)
            detailsPane.getChildren().add(createDateLabel(task.getDueDate()));
    }
    
    private Label createAssingLable(String email) {
        return LabelBuilder.create()
                .text(email)
                .graphic(null)
                .build();
    }
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM dd yyyy");
    
    private Label createDateLabel(Date date) {
        return LabelBuilder.create()
                .text(formatter.format(date))
                .graphic(null)
                .build();
    }
}
