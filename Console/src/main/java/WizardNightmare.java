import console.Console;
import game.Interactive;
import game.demiurge.Demiurge;

public class WizardNightmare {
    public static void main(String[] args) {
        Demiurge demiurge = new Demiurge();
        Interactive console = new Console(demiurge);
        console.configure();
        console.start();
    }
}