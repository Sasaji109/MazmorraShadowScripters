package LoadTXT;

import game.demiurge.Demiurge;
import java.io.IOException;


public interface LoadTXT {
    Demiurge loadDemiurge(String path) throws ClassNotFoundException, IOException;
}
