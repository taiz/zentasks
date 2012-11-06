package zentasks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

/**
 *
 * @author miyabetaiji
 */
public abstract class ParentController extends Controller {
    protected Stage stage;

    public Stage getStage() { return stage; }

    public void setStage(Stage stage) { this.stage = stage; }

    protected void moveTo(ParentController parent) {
        if (stage == null) throw new RuntimeException("Stage is null");
        Stage newStage = StageBuilder.create()
                .scene(
                    SceneBuilder.create()
                        .root(parent.getRoot())
                        .build()
                )
                .build();
        parent.setStage(newStage);
        newStage.show();
        stage.close();
    }
    
    protected Context context = Context.getInstance();
}
