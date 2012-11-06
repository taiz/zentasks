package zentasks;

import java.net.URL;
import java.util.ArrayList;
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
public class TaskBoard extends Controller {

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
    private CheckBox doneCheckBox;

    private ChangeListener<Boolean> doneListener = new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean done) {
            for (TaskPane taskPane : collectTaskPanes(!done)) taskPane.setDone(done);
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
                            removeTaskPanes(collectTaskPanes(true));
                       }
                    })
                    .build(),
                MenuItemBuilder.create()
                    .text("Remove all tasks")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            removeTaskPanes(taskPanes);
                       }
                    })
                    .build(),
                MenuItemBuilder.create()
                    .text("Delete folder")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            removeFolder();
                       }
                    })
                    .build()
            )
            .build();

    private void removeTaskPanes(List<TaskPane> panes) {
        List<TaskPane> dup = new ArrayList<TaskPane>(panes);
        for (TaskPane taskPane : dup) {
            taskPane.remove();
        }
    }

    private List<TaskPane> collectTaskPanes(boolean done) {
        List<TaskPane> panes = new ArrayList<TaskPane>();
        for (TaskPane taskPane : taskPanes)
            if (taskPane.isDone() == done) panes.add(taskPane);
        return panes;
    }
    
    private void removeFolder() {
        Task.deleteInFolder(project.getId(), folderName.get());
        dashboard.taskBoardRemoved(this);
    }

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
    
    private Dashboard dashboard;
    
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    private TaskPane createTaskPane(Task task) throws FXMLLoadException {
        TaskPane pane = (TaskPane)Util.loadFXML(this, "TaskPane.fxml");
        pane.setTask(task);
        return pane;
    }

    private void addTaskPane(TaskPane taskPane) {
        taskPanes.add(taskPane);
        taskPane.setTaskBoard(this);
        taskItemsPane.getChildren().add(taskPane.getRoot());
    }

    public void taskChanged(TaskPane taskPane) {
        updateRemainLabel();
    }

    public void taskRemoved(TaskPane taskPane) {
        taskPanes.remove(taskPane);
        taskItemsPane.getChildren().remove(taskPane.getRoot());
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
                input.textProperty().unbindBidirectional(folderName);
            }
        });
        folderPane.getChildren().add(input);
    }
}

