package gui.pantallas.principal;

import LoadTXT.LoadTXT;
import game.character.Wizard;
import game.demiurge.Demiurge;
import game.dungeon.Dungeon;
import game.dungeon.Room;
import gui.Constantes;
import gui.pantallas.common.BasePantallaController;
import gui.pantallas.common.Pantallas;
import informes.InformesMazmorra;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.log4j.Log4j2;
import saveTXT.SaveTXT;
import saveXML.SaveXML;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PrincipalController {
    Instance<Object> instance;

    @FXML
    private MenuBar menuPrincipal;
    @FXML
    private Stage primaryStage;
    @FXML
    public BorderPane root;

    private Demiurge demiurge;

    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
    }

    private void cargarPantalla(Pantallas pantalla) {
        cambiarPantalla(cargarPantalla(pantalla.getRoot()));
    }

    private Pane cargarPantalla(String ruta) {
        Pane panePantalla = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BasePantallaController screenController = fxmlLoader.getController();
            screenController.setPrincipalController(this);
            screenController.principalLoaded();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return panePantalla;
    }

    private void cambiarPantalla(Pane nuevaPantalla) {
        root.setCenter(nuevaPantalla);
    }

    public void initialize() {
        menuPrincipal.setVisible(false);
        cargarPantalla(Pantallas.INICIO);
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private void closeWindowEvent(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("Salir del juego");
        alert.setContentText("¿Estás seguro que deseas salir del juego?");
        alert.initOwner(primaryStage.getOwner());
        Optional<ButtonType> res = alert.showAndWait();

        res.ifPresent(buttonType -> {
            if (buttonType == ButtonType.CANCEL) {
                event.consume();
            }
        });
    }

    @FXML
    private void menuClick(ActionEvent actionEvent) throws IOException {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "menuItemSalirSinGuardar" -> salirSinGuardar();
            case "menuItemGuardarYSalirXML" -> guardarYSalirXML();
            case "menuItemGuardarYSalirTXT" -> guardarYSalirTXT();
            case "menuItemGenerarInformeHistorialAcciones" -> generarInformeHistorialAcciones();
            case "menuItemGenerarInformeCeldasVisitadas" -> generarInformeCeldasVisitadas();
            case "menuItemGenerarInformeHojaPersonaje" -> generarInformeHojaPersonaje();
            default -> throw new IllegalStateException(Constantes.VALOR_NO_PREVISTO + ((MenuItem) actionEvent.getSource()).getId());
        }
    }

    public void salirSinGuardar() {
        Platform.exit();
    }

    public void guardarYSalirXML() {
        SaveXML saveXML = null;
        saveXML.saveDemiurge(demiurge,"");
        primaryStage.close();
    }

    public void guardarYSalirTXT() throws IOException {
        SaveTXT saveTXT = null;
        saveTXT.saveDemiurge(demiurge,"");
        primaryStage.close();
    }

    public void generarInformeHistorialAcciones() {
        InformesMazmorra informesMazmorra = null;
        List<String> acciones = null;
        informesMazmorra.HistorialAcciones(acciones);
    }

    public void generarInformeCeldasVisitadas() {
        InformesMazmorra informesMazmorra = null;
        List<Room> celdas = null;
        informesMazmorra.CeldasVisitadas(celdas);
    }

    public void generarInformeHojaPersonaje() {
        InformesMazmorra informesMazmorra = null;
        informesMazmorra.HojaPersonaje(demiurge.getWizard());
    }

    public void partidaNuevaInicio() {
        demiurge.setDungeon(null);
        demiurge.setDungeon(new Dungeon());
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeTXT() throws IOException, ClassNotFoundException {
        LoadTXT loadTXT = null;
        demiurge = loadTXT.loadDemiurge("");
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeXML() {
        //LoadXml loadXML = null;
        //demiurge = loadXML.load("");
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void ajustesInicio() {

    }

    public void salirInicio() {
        Platform.exit();
    }
}
