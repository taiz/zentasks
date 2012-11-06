/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zentasks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 *
 * @author miyabetaiji
 */
public abstract class Controller implements Initializable {
    @FXML
    protected Pane root;
    
    public Pane getRoot() { return root; }
}
