package gui.main;

import gui.Constantes;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gui.pantallas.principal.PrincipalController;

import java.io.IOException;

public class MainFX {
    @Inject
    FXMLLoader fxmlLoader;

    private static final Logger logger = LogManager.getLogger(MainFX.class);

    public void start(@Observes @StartupScene Stage stage) {
        try {
            Parent fxmlParent = fxmlLoader.load(getClass().getResourceAsStream(Constantes.FXML_PRINCIPAL_FXML));
            PrincipalController controller = fxmlLoader.getController();
            controller.setStage(stage);

            stage.setScene(new Scene(fxmlParent));
            stage.show();

        } catch (IOException e) {
            logger.error(Constantes.ERROR_ON_LOADING_THE_FXML_FILE, e);
            System.exit(0);
        }
    }

}
