package ar.edu.itba.ss.commons;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final Set<Pedestrian> pedestrians;


    Cell(){
        this.pedestrians = new HashSet<>();
    }

    public Set<Pedestrian> getPedestrians(){
        return pedestrians;
    }

    public boolean hasPedestrians(){
        return !this.pedestrians.isEmpty();
    }

    @Override
    public String toString(){
        return pedestrians.toString();
    }
}
