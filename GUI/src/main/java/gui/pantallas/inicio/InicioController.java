package gui.pantallas.inicio;

import gui.pantallas.common.BasePantallaController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class InicioController extends BasePantallaController {

    //COMPONENTES PANTALLA
    @FXML
    private ImageView logo;
    @FXML
    private ImageView back;
    @FXML
    private ImageView background;


    @FXML
    private void elegirArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");
        FileChooser.ExtensionFilter xmlFilter = new FileChooser.ExtensionFilter("Archivos XML (*.xml)", "*.xml");
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Archivos TXT (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(xmlFilter, txtFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            if (filePath.endsWith(".xml")) {
                getPrincipalController().cargarPartidaDesdeXML(filePath);
            } else {
                try {
                    getPrincipalController().cargarPartidaDesdeTXT(filePath);
                } catch (IOException | ClassNotFoundException ex) {
                    sacarAlertError("Error al cargar el archivo");
                }
            }
        } else {
            sacarAlertError("Ningún archivo fue seleccionado");
        }
    }

    private void sacarAlertError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    //MÉTODOS PARA EJECUTAR CON BOTONES
    @FXML
    private void partidaNuevaInicio() {
        getPrincipalController().partidaNuevaInicio();
    }

    @FXML
    private void salirInicio() {
        getPrincipalController().salirInicio();
    }


    @FXML
    private void cargarImagenLogo(){
        //trabajar con ImageView logo
    }

    @FXML
    private void cargarImagenMenu(){
        //trabajar con ImageView back
    }

    @FXML
    private void cargarImagenFondo(){
        //trabajar con ImageView background
    }

}
