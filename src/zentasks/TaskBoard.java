package zentasks;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import zentasks.models.Project;
import zentasks.models.Task;

/**
 *
 * @author miyabetaiji
 */
public class TaskBoard implements Initializable, AccessibleRootNode {

    private Project project;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        doneCheckBox.selectedProperty().addListener(doneListener);
        folderLabel.textProperty().bind(folderName);
        taskPanes.addListener(remainListener);
    }

    // Call from Dahsborad
    public void setProject(Project project) { this.project = project; }
    
    @FXML
    private VBox root;

    @Override
    public VBox getRootNode() { return root; }

    @FXML
    private CheckBox doneCheckBox;

    private ChangeListener<Boolean> doneListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean done) {
            for (TaskPane taskPane : taskPanes)
                if (taskPane.isDone() != done) taskPane.setDone(done);
        }
    };

    @FXML
    private Label folderLabel;
    // Binder of folderLabel.textProperty()
    private StringProperty folderName = new SimpleStringProperty();

    // Call from Dahsborad
    public void setFolderName(String folderName) { this.folderName.set(folderName); }

    @FXML
    private Label remainLabel;

    private ListChangeListener remainListener = new ListChangeListener<TaskPane>() {
        @Override
        public void onChanged(Change<? extends TaskPane> change) {
            updateRemainLabel();
        }
    };

    private void updateRemainLabel() {
        Integer remain = 0;
        for (TaskPane taskPane : taskPanes) if (!taskPane.isDone()) remain++;
        remainLabel.setText(remain.toString());
    }

    @FXML
    private Button taskOperationBtn;

    // Call from taskOperationBtn (On Action)
    @FXML
    private void showMenu(MouseEvent event) {
        removeMenu.show(taskOperationBtn, event.getScreenX(), event.getScreenY());
    }

    private ContextMenu removeMenu = ContextMenuBuilder.create()
            .items(
                MenuItemBuilder.create()
                    .text("Remove complete tasks")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //removeCompletedTasks();
                       }
                    })
                    .build(),
                MenuItemBuilder.create()
                    .text("Remove all tasks")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //removeAllTasks();
                       }
                    })
                    .build(),
                MenuItemBuilder.create()
                    .text("Delete folder")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //removeFolder();
                       }
                    })
                    .build()
            )
            .build();

    @FXML
    private TextField newTaskText;

    // Call from newTaskText (On Action)
    @FXML
    private void registerNewTask(ActionEvent event) {
        Task task = new Task(newTaskText.getText(), folderName.get(), project);
        task.save();
        try {
            addTaskPane(createTaskPane(task));
        } catch (FXMLLoadException ex) {
            task.delete();
            Logger.getLogger(TaskBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        newTaskText.clear();
    }

    @FXML
    private VBox taskItemsPane;

    private ObservableList<TaskPane> taskPanes = FXCollections.observableArrayList();

    // Call from Dashboard
    public void setTasks(List<Task> tasks) {
        try {
            for (Task task : tasks) {
                addTaskPane(createTaskPane(task));
            }
        } catch (FXMLLoadException ex) {
            Logger.getLogger(TaskBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private TaskPane createTaskPane(Task task) throws FXMLLoadException {
        TaskPane pane = (TaskPane)Util.loadFXML(this, "TaskPane.fxml");
        pane.setTask(task);
        return pane;
    }

    private void addTaskPane(TaskPane taskPane) {
        taskPanes.add(taskPane);
        taskPane.setTaskBoard(this);
        taskItemsPane.getChildren().add(taskPane.getRootNode());
    }

    public void taskChanged(TaskPane taskPane) {
        updateRemainLabel();
    }

    public void taskRemoved(TaskPane taskPane) {
        taskPanes.remove(taskPane);
        taskItemsPane.getChildren().remove(taskPane.getRootNode());
        updateRemainLabel();
    }

    @FXML
    private StackPane folderPane;

    public void acceptEdit() {
       final TextField input = TextFieldBuilder.create().build();
        input.textProperty().bindBidirectional(folderName);
        input.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                folderPane.getChildren().remove(input);
                input.clear();
            }
        });
        folderPane.getChildren().add(input);
    }
}

