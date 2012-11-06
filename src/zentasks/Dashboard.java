package zentasks;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import zentasks.models.Project;
import zentasks.models.Task;
import zentasks.models.User;

/**
 *
 * @author miyabetaiji
 */
public class Dashboard extends ParentController {

    @FXML
    private Label userNameLabel;

    @FXML
    private Label emailLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showUser();
        buildBoard();
        VBox vbox = (VBox)projectsTree.getParent();
        vbox.prefHeightProperty().bind(projectsTree.heightProperty());
        //projectsTree.prefHeightProperty().bind(vbox.heightProperty());
    }    

    private void showUser() {
        String email = (String)context.data().get("email");
        String name = User.find(email).getName();
        userNameLabel.setText(email);
        emailLabel.setText("(" + name + ")");
    }

    @FXML
    private Button logout;
    
    @FXML
    private void doLogout(ActionEvent envet) {
        context.data().clear();
        moveToLogin();
    }
    
    private void moveToLogin() {
        try {
            Login login = (Login)Util.loadFXML(this, "Login.fxml");
            login.showInfoMessage("You've been logged out");
            moveTo(login);
        } catch (FXMLLoadException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Call from pageTitle (On Mouse Cliceked)
    @FXML
    private void reloadAll(MouseEvent event) { buildBoard(); }

    // Call from dashboardLink (On Action)
    @FXML
    private void reloadProjectBoard(ActionEvent event) {
        buildProjectBoard();
    }

    private void buildBoard() {
        buildProjectTree();
        buildProjectBoard();
    }

    private void buildProjectBoard() {
        buildBreadcrumb("Dashboard", "Tasks over all projects");
        buildTaskBoard();
    }

    private void buildProjectBoard(Project project) {
        buildBreadcrumb(project);
        buildTaskBoard(project);
    }
    
    @FXML
    private VBox taskBoradsPane;

    private void buildTaskBoard() {
        taskBoradsPane.getChildren().clear();
        String user = (String)context.data().get("email");
        Map<Project,List<Task>> tasks = Task.findGroupByProject(user);
        try {
            for(Entry<Project,List<Task>> entry : tasks.entrySet()) {
                ProjectBoard borad = createProjectBorad(entry.getKey(), entry.getValue());
                taskBoradsPane.getChildren().add(borad.getRoot());
            }
        } catch (FXMLLoadException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        newFolderBtn.setVisible(false);
    }
    
    private Project currentProject;
    
    void buildTaskBoard(Project project) {
        currentProject = project;
        taskBoradsPane.getChildren().clear();

        Map<String,List<Task>> tasks = Task.findByProject(project);
        List<String> folders = new ArrayList<String>(tasks.keySet());
        Collections.sort(folders);
        try {
            for (String folder : folders) {
                TaskBoard board = createTaskBoard(project, folder, tasks.get(folder));
                taskBoradsPane.getChildren().add(board.getRoot());
            }
        } catch (FXMLLoadException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        newFolderBtn.setVisible(true);
    }

    private ProjectBoard createProjectBorad(Project project, List<Task> tasks) throws FXMLLoadException {
        ProjectBoard board = (ProjectBoard)Util.loadFXML(this, "ProjectBoard.fxml");
        board.setDashboard(this);
        board.setProject(project);
        board.setTasks(tasks);
        return board;
    }

    private TaskBoard createTaskBoard(Project project, String folderName) throws FXMLLoadException {
        TaskBoard board = (TaskBoard)Util.loadFXML(this, "TaskBoard.fxml");
        board.setDashboard(this);
        board.setProject(project);
        board.setFolderName(folderName);
        return board;
    }
    
    private TaskBoard createTaskBoard(Project project, String folderName, List<Task> tasks)
            throws FXMLLoadException {
        TaskBoard board = createTaskBoard(project, folderName);
        board.setTasks(tasks);
        return board;
    }

    private TaskPane createTaskPane(Task task) throws FXMLLoadException {
        TaskPane pane = (TaskPane)Util.loadFXML(this, "TaskPane.fxml");
        pane.setTask(task);
        return pane;
    }

    @FXML
    private Button newFolderBtn;
    
    @FXML
    private void addNewFolder(ActionEvent event) throws FXMLLoadException {
        TaskBoard board = createTaskBoard(currentProject, "New Folder");
        board.acceptEdit();
        taskBoradsPane.getChildren().add(board.getRoot());
    }

    public void taskBoardRemoved(TaskBoard board) {
        taskBoradsPane.getChildren().remove(board.getRoot());
    }
    
    @FXML
    private Label breadcrumbFirst;
    
    @FXML
    private Label breadcrumbSecond;

    private void buildBreadcrumb(String first, String second) {
        breadcrumbFirst.setText(first);
        breadcrumbSecond.setText(second);
    }

    private void buildBreadcrumb(Project project) {
        buildBreadcrumb(project.getFolder(), project.getName());
    }
    
    @FXML
    private TreeView projectsTree;

    // Call from newGroupBtn (On Action)
    @FXML
    private void addNewProjectGroup(ActionEvent event) {
        ProjectGroupItem groupItem = new ProjectGroupItem();
        groupItem.acceptEdit();
        addProjectGroup(groupItem);
    }
    
    private void buildProjectTree() {
        TreeItem rootNode = new TreeItem();
        List<Project> projects = Project.findInvolving((String)context.data().get("email"));
        for (Project project : projects) {
            boolean found = false;
            for (Object treeItem : rootNode.getChildren()) {
                if (!(treeItem instanceof ProjectGroupItem)) continue;
                ProjectGroupItem groupItem = (ProjectGroupItem)treeItem;
                if (groupItem.getGroupName().equals(project.getFolder())) {
                    groupItem.getChildren().add(new ProjectItem(groupItem, project));
                    found = true;
                }
            }
            if (found) continue;
            ProjectGroupItem groupItem = new ProjectGroupItem(project.getFolder());
            rootNode.getChildren().add(groupItem);
            groupItem.getChildren().add(new ProjectItem(groupItem, project));
        }
        rootNode.setExpanded(true);
        projectsTree.setRoot(rootNode);
    }

    private void addProjectGroup(ProjectGroupItem groupItem) {
        projectsTree.getRoot().getChildren().add(groupItem);
    }

    private void projectGroupRemoved(ProjectGroupItem groupItem) {
        projectsTree.getRoot().getChildren().remove(groupItem);
    }

    private class ProjectGroupItem extends TreeItem<BorderPane> {
        private StringProperty groupName = new SimpleStringProperty();
        private ContextMenu settingMenu;

        public ProjectGroupItem() { this("NEW GROUP"); }

        public ProjectGroupItem(String groupName) {
            this.groupName.set(groupName);
            this.setValue(createPane());
            this.settingMenu = createSettingMenu();
        }

        public String getGroupName() { return this.groupName.get(); }
        
        private BorderPane createPane() {
            BorderPane pane = BorderPaneBuilder.create().build();
            labelPane = createLabelPane();
            pane.setCenter(labelPane);
            final Button settingBtn = createButton();
            pane.setRight(settingBtn);
            pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    settingBtn.setOpacity(1.0);
                }
            });
            pane.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    settingBtn.setOpacity(0.5);
                }
            });
            return pane;
        }

        private StackPane labelPane;

        private StackPane createLabelPane() {
            StackPane pane = StackPaneBuilder.create().alignment(Pos.CENTER_LEFT).build();
            Label groupLabel = LabelBuilder.create().build();
            groupLabel.textProperty().bind(groupName);
            pane.getChildren().add(groupLabel);
            return pane;
        }

        private Button createButton() {
            final Button btn = ButtonBuilder.create().opacity(0.5).build();
            btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    settingMenu.show(btn, t.getScreenX(), t.getScreenY());
                }
            });
            return btn;
        }
        
        private ContextMenu createSettingMenu() {
            ContextMenu menu = new ContextMenu();
            menu.getItems().addAll(
                MenuItemBuilder.create()
                    .text("New Project")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            addNewProjectItem();
                        }
                    })
                    .build(),
                MenuItemBuilder.create()
                    .text("Remove Group")
                    .onAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            remove();
                        }
                    })
                    .build()
            );
            return menu;
        }

        public void acceptEdit() {
            final TextField input = TextFieldBuilder.create().build();
            input.textProperty().bindBidirectional(groupName);
            input.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    labelPane.getChildren().remove(input);
                }
            });
            labelPane.getChildren().add(input);
        }

        private void addNewProjectItem() {
            User user = User.find((String)context.data().get("email"));
            Project project = new Project("New Project", groupName.get(), user);
            ProjectItem projectItem = new ProjectItem(this, project);
            projectItem.acceptEdit();
            this.getChildren().add(projectItem);
        }

        private void remove() {
            for (TreeItem treeItem : this.getChildren()) {
                if (!(treeItem instanceof ProjectItem)) continue;
                ProjectItem projectItem = (ProjectItem)treeItem;
                projectItem.remove();
            }
            projectGroupRemoved(this);
        }

        public void projectRemoved(ProjectItem projectItem) {
            this.getChildren().remove(projectItem);
        }
    }

    private class ProjectItem extends TreeItem<BorderPane> {
        private ProjectGroupItem groupItem;
        private Project project;

        public ProjectItem(ProjectGroupItem groupItem, Project project) {
            this.groupItem = groupItem;
            this.project = project;
            this.setValue(createPane());
        }

        private BorderPane createPane() {
            BorderPane pane = BorderPaneBuilder.create().build();
            linkPane = createLinkPane();
            pane.setCenter(linkPane);
            pane.setAlignment(linkPane, Pos.CENTER_LEFT);
            final Button deleteBtn = createButton();
            pane.setRight(deleteBtn);
            pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    deleteBtn.setVisible(true);
                }
            });
            pane.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    deleteBtn.setVisible(false);
                }
            });
            return pane;
        }

        private StackPane linkPane;
        private StringProperty porjectName = new SimpleStringProperty();

        private StackPane createLinkPane() {
            porjectName.set(project.getName());
            Hyperlink projectLink = createLink();
            projectLink.textProperty().bind(porjectName);
            StackPane stackPane = StackPaneBuilder.create()
                    .alignment(Pos.CENTER_LEFT).children(projectLink).build();
            return stackPane;
        }

        private Hyperlink createLink() {
            Hyperlink link = new Hyperlink();
            link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    buildProjectBoard(project);
                }
            });
            return link;
        }
        
        private Button createButton() {
            Button btn = ButtonBuilder.create().visible(false).build();
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) { remove(); }
            });
            return btn;
        }

        public void acceptEdit() {
            final TextField input = TextFieldBuilder.create().build();
            input.textProperty().bindBidirectional(porjectName);
            input.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    project.setName(input.getText());
                    project.save();
                    linkPane.getChildren().remove(input);
                }
            });
            linkPane.getChildren().add(input);
        }

        public void remove() {
            project.delete();
            groupItem.projectRemoved(this);
        }
    }
}

