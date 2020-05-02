package pt.isel.ls.model;

import java.util.List;

public abstract class Entity {

    public abstract List<String> getValues();

    @Override
    public boolean equals(Object obj) {
        Entity e = (Entity) obj;
        return getValues().equals(e.getValues());
    }

}
