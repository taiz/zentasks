package zentasks;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;

/**
 *
 * @author miyabetaiji
 */
public class ProjectBoard implements Initializable, AccessibleRootNode {

    @FXML
    private VBox root;

    @FXML
    private Hyperlink projectLink;

    @FXML
    private VBox taskItemsPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    @Override
    public VBox getRootNode() { return root; }

    // Call from Dahsborad
        public void setProjectName(String projectName) {
        projectLink.setText(projectName);
    }

    // Call from Dahsborad
    public void addTaskItem(Node taskItem) {
        taskItemsPane.getChildren().add(taskItem);
    }
}
