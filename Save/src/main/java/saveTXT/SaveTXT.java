package saveTXT;

import game.demiurge.Demiurge;

import java.io.IOException;

public interface SaveTXT {
    void saveDemiurge(Demiurge demiurge, String path) throws IOException;
}
