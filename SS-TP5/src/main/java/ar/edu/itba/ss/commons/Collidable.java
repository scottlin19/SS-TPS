package ar.edu.itba.ss.commons;

public abstract class Collidable {

    protected Type type;

    protected enum Type{
        PEDESTRIAN,
        WALL,
        TARGET

    }

}
