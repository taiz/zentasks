/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zentasks;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author miyabetaiji
 */
public class Zentasks extends Application {
    
    public static void main(String[] args) {
        Application.launch(Zentasks.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Login login = (Login)Util.loadFXML(getClass().getResourceAsStream("Login.fxml"));
        login.setStage(stage);
        stage.setScene(new Scene(login.getRoot()));
        stage.show();
    }
}
