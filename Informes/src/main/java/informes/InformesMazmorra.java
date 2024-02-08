package informes;

import game.character.Wizard;
import game.dungeon.Room;

import java.util.List;

public interface InformesMazmorra {
    void HistorialAcciones(List<String> acciones);
    void CeldasVisitadas(List<Room> celdas);
    void HojaPersonaje(Wizard personaje);
}
