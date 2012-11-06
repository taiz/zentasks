package zentasks;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import zentasks.models.User;

/**
 *
 * @author miyabetaiji
 */
public class Login extends ParentController {

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private HBox pageTitle;

    // Call from pageTitle (On Mouse Clicked)
    @FXML
    private StackPane messagePane;

    @FXML
    private void reload(MouseEvent event) { clearAll(); }

    private void clearAll() {
        messagePane.getChildren().clear();
        VBox.setMargin(messagePane, Insets.EMPTY);
        email.clear();
        password.clear();
    }

    @FXML
    private TextField email;
    
    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;

    @FXML
    private void doLogin(ActionEvent event) {
        if (User.authenticate(email.getText(), password.getText()) == null) {
            showErrorMessage("Invalid user or password");
            return;
        }
        context.data().put("email", email.getText());
        moveToDashboard();
    }

    private void moveToDashboard() {
        try {
            moveTo((Dashboard)Util.loadFXML(this, "Dashboard.fxml"));
        } catch (FXMLLoadException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showInfoMessage(String message) {
        showMessage(
                LabelBuilder.create()
                    .text(message)
                    .styleClass("info-message")
                    .build()
                );
    }

    public void showErrorMessage(String message) {
        showMessage(
                LabelBuilder.create()
                    .text(message)
                    .styleClass("error-message")
                    .build()
                );
    }

    private void showMessage(Label label) {
        messagePane.getChildren().clear();
        messagePane.getChildren().add(label);
        VBox.setMargin(messagePane, new Insets(10,0,10,0));
    }
}
