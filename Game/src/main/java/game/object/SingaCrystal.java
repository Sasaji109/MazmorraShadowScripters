package game.object;

import game.Domain;

public class SingaCrystal extends Item{

    //TODO se ha cambiado a publico
    public SingaCrystal(int s) { super(Domain.NONE, s); }

    public static SingaCrystal createCrystal(int maximum) { return new SingaCrystal((int) (Math.random() * maximum + 1)); }

}
