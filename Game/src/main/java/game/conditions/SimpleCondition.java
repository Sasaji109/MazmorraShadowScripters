package game.conditions;

import java.io.Serializable;

public class SimpleCondition implements Condition, Serializable {

    @Override
    public boolean check() { return true; }
}
