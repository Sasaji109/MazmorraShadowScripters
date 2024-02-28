package gui.pantallas.principal;

import java.nio.file.Path;
import java.nio.file.Paths;
import LoadTXT.LoadTXT;
import LoadTXT.loadTXTImpl.LoadTXTImpl;
import game.demiurge.Demiurge;
import game.dungeon.Room;
import gui.Constantes;
import gui.pantallas.common.BasePantallaController;
import gui.pantallas.common.Pantallas;
import informes.InformesMazmorra;
import informes.impl.InformesMazmorraImpl;
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
import saveTXT.saveTXTImpl.SaveTXTImpl;
import saveXML.SaveXML;
import saveXML.saveXMLImpl.saveXMLImp;
import java.io.IOException;
import java.util.Collections;
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
        demiurge = new Demiurge();
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
        SaveXML saveXML = new saveXMLImp();
        String downloadsFolder = System.getProperty("user.home") + "\\Downloads";
        Path filePath = Paths.get(downloadsFolder, "mazmorraSave.txt");
        saveXML.saveDemiurge(demiurge, String.valueOf(filePath));
        primaryStage.close();
    }

    public void guardarYSalirTXT() throws IOException {
        SaveTXT saveTXT = new SaveTXTImpl();
        String downloadsFolder = System.getProperty("user.home") + "\\Downloads";
        Path filePath = Paths.get(downloadsFolder, "mazmorraSave.txt");
        saveTXT.saveDemiurge(demiurge, String.valueOf(filePath));
        primaryStage.close();
    }

    public void generarInformeHistorialAcciones() {
        InformesMazmorra informesMazmorra = new InformesMazmorraImpl();
        List<String> acciones = (List<String>) demiurge.getWizard().getWearables();
        informesMazmorra.HistorialAcciones(acciones);
    }

    public void generarInformeCeldasVisitadas() {
        InformesMazmorra informesMazmorra = new InformesMazmorraImpl();
        List<Room> celdas = Collections.singletonList(demiurge.getDungeon().getRoom(1));
        informesMazmorra.CeldasVisitadas(celdas);
    }

    public void generarInformeHojaPersonaje() {
        InformesMazmorra informesMazmorra = new InformesMazmorraImpl();
        informesMazmorra.HojaPersonaje(demiurge.getWizard());
    }

    public void partidaNuevaInicio() {
        demiurge = new Demiurge();
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeTXT(String filePath) throws IOException, ClassNotFoundException {
        LoadTXT cargarTXT=new LoadTXTImpl();
        demiurge = cargarTXT.loadDemiurge(filePath);
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeXML(String filePath) {
        //LoadXml cargarXML= new LoadXMLImpl();
        //demiurge = loadXML.load("");
        //cargarPantalla(Pantallas.MENU_JUEGO);
        //menuPrincipal.setVisible(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mantenimiento");
        alert.setHeaderText(null);
        alert.setContentText("Lo sentimos, esta función está actualmente en mantenimiento. Por favor, inténtelo de nuevo más tarde.");
        alert.showAndWait();
    }

    public void salirInicio() {
        Platform.exit();
    }
}
