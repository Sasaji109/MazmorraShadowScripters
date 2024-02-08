package LoadTXT.loadTXTImpl;

import LoadTXT.LoadTXT;
import game.demiurge.Demiurge;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadTXTImpl implements LoadTXT {
    public Demiurge loadDemiurge(String filename) throws ClassNotFoundException, IOException {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            return (Demiurge) ois.readObject();
    }
}
