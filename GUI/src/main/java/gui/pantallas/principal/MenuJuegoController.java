package gui.pantallas.principal;
import game.character.Wizard;
import game.demiurge.Demiurge;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import game.dungeon.Room;
import game.object.SingaStone;
import game.objectContainer.*;
import game.spellContainer.Knowledge;
import game.util.Value;
import gui.pantallas.common.BasePantallaController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MenuJuegoController extends BasePantallaController {

    @FXML
    private TextArea statsArea;

    @FXML
    private TextArea equipoArea;

    @FXML
    private TextField tiempoJugado;

    @FXML
    private TextArea consoleArea;

    private Demiurge demiurge;

    @Override
    public void principalLoaded() {
        demiurge = new Demiurge();
        if (demiurge != null) {
            demiurge = crearDemiurgeBasico();
            statsArea.setText(String.valueOf(demiurge.getWizard().getNumberOfAttacks()));
            equipoArea.setText(String.valueOf(demiurge.getWizard().getWearables()));

            StringBuilder dungeonInfo = new StringBuilder("Dungeon Information:\n");
            Dungeon dungeon = demiurge.getDungeon();

            if (dungeon != null) {
                for (Room room : dungeon.getRooms()) {
                    dungeonInfo.append("Room ").append(room.getID()).append(": ").append(room.getDescription()).append("\n");
                }
            } else {
                dungeonInfo.append("No dungeon information available.\n");
            }

            consoleArea.setText(dungeonInfo.toString());
            tiempoJugado.setText("Día: " + demiurge.getDay());

        }
    }

    private Demiurge crearDemiurgeBasico() {
        Demiurge newDemiurge = new Demiurge();

        Dungeon dungeon = new Dungeon();
        dungeon.addRoom(new Room(1, "¡Bienvenido a la mazmorra!",new RoomSet(1)));
        dungeon.addRoom(new Room(2, "Parece que has encontrado una sala secreta.",new RoomSet(2)));
        newDemiurge.setDungeon(dungeon);

        Home home = new Home("Tu Hogar",1,2,3,new Chest(1),new Knowledge());
        home.setComfort(new Value(50, 0, 100));
        home.setSinga(new SingaStone(1,2));
        newDemiurge.setHome(home);


        Wizard wizard = new Wizard("Gandalf",4,10,22,1,new Wearables(5, 2, 3),null,null);
        wizard.setEnergy(1);
        wizard.setCrystalCarrier(new CrystalCarrier(1));
        wizard.setJewelryBag(new JewelryBag(1));
        newDemiurge.setWizard(wizard);

        return newDemiurge;
    }
}
