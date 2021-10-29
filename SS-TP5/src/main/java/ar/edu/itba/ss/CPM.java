package ar.edu.itba.ss;

import ar.edu.itba.ss.commons.Grid;

public class CPM {

    private final Grid grid;


    public CPM(CPMConfig config) {

        this.grid = new Grid(config);
    }
    public void simulate(double deltaT) {
        while(true){
            grid.updatePedestrians(deltaT);
        }
    }

    public static void main(String[] args) {

    }
}
