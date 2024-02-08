package gui.pantallas.inicio;

import gui.pantallas.common.BasePantallaController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InicioController extends BasePantallaController {

    //COMPONENTES PANTALLA
    @FXML
    private ImageView logo;
    @FXML
    private ImageView back;
    @FXML
    private ImageView background;

    @FXML
    private void seleccionarArchivoCargarPartida() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de partida");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos TXT", "*.txt"),
                new FileChooser.ExtensionFilter("Archivos XML", "*.xml")
        );

        Stage stage = (Stage) logo.getScene().getWindow();
        var selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
                getPrincipalController().cargarPartidaDesdeTXT();
            } else if (selectedFile.getName().toLowerCase().endsWith(".xml")) {
                getPrincipalController().cargarPartidaDesdeXML();
            } else {
                mostrarError("Formato de archivo no admitido");
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK);
        alert.showAndWait();
    }

    //MÉTODOS PARA EJECUTAR CON BOTONES
    @FXML
    private void partidaNuevaInicio() {
        getPrincipalController().partidaNuevaInicio();
    }

    @FXML
    private void ajustesInicio() {
        getPrincipalController().ajustesInicio();
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
