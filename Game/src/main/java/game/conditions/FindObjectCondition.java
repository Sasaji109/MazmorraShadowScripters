package game.conditions;

import game.object.Item;

import java.io.Serializable;

public class FindObjectCondition implements Condition, Serializable {
    Item item;

    public FindObjectCondition(Item item) { this.item = item; }

    @Override
    public boolean check() { return item.isViewed(); }
}
