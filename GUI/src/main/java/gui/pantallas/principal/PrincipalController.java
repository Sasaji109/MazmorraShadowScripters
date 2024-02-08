package gui.pantallas.principal;

import game.demiurge.Demiurge;
import game.dungeon.Dungeon;
import gui.Constantes;
import gui.pantallas.common.BasePantallaController;
import gui.pantallas.common.Pantallas;
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
import java.io.IOException;
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
    //private GuardarDungeon guardarDungeon;
    //private LeerDungeonTXT leerDungeonTXT;
    //private LeerDungeonXML leerDungeonXML;

    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        //this.demiurge = demiurge;
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
    private void menuClick(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "menuItemSalirSinGuardar" -> salirSinGuardar();
            case "menuItemGuardarYSalir" -> guardarYSalir();
            case "menuItemGenerarInforme" -> generarInforme();
            default -> throw new IllegalStateException(Constantes.VALOR_NO_PREVISTO + ((MenuItem) actionEvent.getSource()).getId());
        }
    }

    public void salirSinGuardar() {
        Platform.exit();
    }

    public void guardarYSalir() {
        Dungeon dungeon = demiurge.getDungeon();
        //guardarDungeon.saveDungeon(dungeon);
        primaryStage.close();
    }

    public void generarInforme() {

    }

    public void partidaNuevaInicio() {
        //demiurge.setDungeon(null);
        //demiurge.setDungeon(new Dungeon());
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeTXT() {
        //Dungeon dungeon = leerDungeonTXT.getDungeonfromTXT();
        //cargarDungeon(dungeon);
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void cargarPartidaDesdeXML() {
        //Dungeon dungeon = leerDungeonXML.getDungeonfromXML();
        //cargarDungeon(dungeon);
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    private void cargarDungeon(Dungeon dungeon) {
        demiurge.setDungeon(dungeon);
        cargarPantalla(Pantallas.MENU_JUEGO);
        menuPrincipal.setVisible(true);
    }

    public void ajustesInicio() {

    }

    public void salirInicio() {
        Platform.exit();
    }
}
