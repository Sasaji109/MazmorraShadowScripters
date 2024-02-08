package saveTXT.saveTXTImpl;

import game.demiurge.Demiurge;
import saveTXT.SaveTXT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveTXTImpl implements SaveTXT {
    public void saveDemiurge(Demiurge demiurge, String location) throws IOException {
       ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(location));
       oos.writeObject(demiurge);
    }
}
