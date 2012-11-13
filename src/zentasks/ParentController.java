package zentasks;

import javafx.scene.SceneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageBuilder;

/**
 *
 * @author miyabetaiji
 */
public abstract class ParentController extends Controller {
    protected Stage stage;

    Stage getStage() { return stage; }

    void setStage(Stage stage) { this.stage = stage; }

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
