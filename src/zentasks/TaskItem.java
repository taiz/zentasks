package zentasks;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPaneBuilder;
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
                .styleClass("assign-email")
                .build();
    }
    
    private static SimpleDateFormat formatter = new SimpleDateFormat("MM dd yyyy");
    
    private Pane createDateLabel(Date date) {
        return StackPaneBuilder.create()
                .children(
                    LabelBuilder.create()
                        .text(formatter.format(date))
                        .graphic(null)
                        .styleClass("duedate-label")
                        //.opacity(1.0)
                        .build()
                )
                //.padding(new Insets(0, 5, 0, 5))
                //.styleClass("duedate-pane")
                //.opacity(0.7)
                .build()
                ;
    }
}
