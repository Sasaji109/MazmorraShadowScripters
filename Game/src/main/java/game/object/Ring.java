package game.object;

import game.Domain;

public class Ring extends Item {

    public Ring(Domain d, int v) { super(d, v); }

    public static Ring createRing(Domain d, int v) throws ItemCreationErrorException {
        if (d == Domain.ELECTRICITY || d == Domain.FIRE || d == Domain.AIR)
            return new Ring(d, v);
        else
            throw new ItemCreationErrorException();
    }
}
