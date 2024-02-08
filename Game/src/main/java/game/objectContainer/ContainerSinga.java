package game.objectContainer;

import game.Domain;
import game.object.Item;

import java.io.Serializable;

public abstract class ContainerSinga extends Container  implements Serializable {

    public ContainerSinga(int c) { super(Domain.NONE, c); }

    public int singa(){
        int singa = 0;
        for (Item i : items)
            singa += i.getValue();
        return singa;
    }

}
