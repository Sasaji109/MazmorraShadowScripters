package gui.pantallas.common;

import gui.Constantes;
import lombok.Getter;

@Getter
public enum Pantallas {

    INICIO(Constantes.FXML_INICIO_FXML),
    MENU_JUEGO(Constantes.FXML_MENU_JUEGO_FXML);

    private final String root;
    Pantallas(String root) {
        this.root=root;
    }
}
