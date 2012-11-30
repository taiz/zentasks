package zentasks;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import zentasks.models.Task;

/**
 *
 * @author miyabetaiji
 */
public class TaskPane extends Controller {
    private Task task;
    private TaskBoard taskBoard;
    private TaskItem taskItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            taskItem = (TaskItem)Util.loadFXML(this, "TaskItem.fxml");
            ((BorderPane)root).setCenter(taskItem.getRoot());
            BorderPane.setAlignment(taskItem.getRoot(), Pos.CENTER_LEFT);
            doneProperty = doneCheckBox.selectedProperty();
            //doneProperty.addListener(doneListener);
        } catch (FXMLLoadException ex) {
            Logger.getLogger(TaskPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private CheckBox doneCheckBox;
    
    private BooleanProperty doneProperty;

    private ChangeListener<Boolean> doneListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean done) {
            task.setDone(done);
            task.update();
            taskBoard.taskChanged(TaskPane.this);
        }
    };

    @FXML
    private Button removeBtn;

    // Call from removeBtn (On Action)
    @FXML
    private void removeTask(ActionEvent event) {
        remove();
    }
    
    @FXML
    private void showRemoveBtn(MouseEvent evnet) {
        removeBtn.setOpacity(1.0);
    }
    
    @FXML
    private void shadowRemoveBtn(MouseEvent evnet) {
        removeBtn.setOpacity(0.5);
    }
    
    void remove() {
        task.delete();
        taskBoard.taskRemoved(this);
    }
    
    // Call from Dahsborad
    void setTask(Task task) {
        this.task = task;
        taskItem.setTask(task);
        doneProperty.set(task.isDone());
        doneProperty.addListener(doneListener);
    }

    // Call from Dahsborad
    void setTaskBoard(TaskBoard taskBoard) {
        this.taskBoard = taskBoard;
    }
    
    boolean isDone() {
        return task.isDone();
    }
    
    void setDone(boolean done) {
        doneProperty.set(done);
    }
}

