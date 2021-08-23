package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.cim.Grid;
import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.*;

import java.util.*;
import java.util.stream.Collectors;


public class OffLattice {

    private Grid grid;
    private final List<Particle> particles;

    private final StaticFile sf;

    private final List<GridSnapshot> snapshots;

    public OffLattice(String staticDataPath, String dynamicDataPath){
        this.particles = new ArrayList<>();
        this.sf =  new StaticFile(staticDataPath,particles);
        DynamicFile.initDynamicData(dynamicDataPath,particles);
        System.out.println(this.particles);
        this.snapshots = new ArrayList<>();
    }

    public void simulate(){
            int iterations = sf.getMaxIterations();
            grid = new Grid(sf.getL(),sf.getMaxM(),sf.getRC(),sf.getHasWalls(),this.particles);
            grid.completeGrid();

            for (int i = 0; i < iterations; i++) {
                grid.updateNeighbours();
                snapshot(); // Obtengo todos los valores de la iteracion actual para la animacion
                grid.updateParticles();
                grid.updateGrid();
            }
            System.out.println(snapshots.stream().mapToDouble(GridSnapshot::getVA).average().orElse(0));
    }

    private void snapshot(){
        snapshots.add(grid.snapshot());
    }

    // Guarda a archivo XYZ
    public void saveResults(String fileName){
        OutputFile.createOutputFile(snapshots.stream().map(GridSnapshot::getParticlesData).collect(Collectors.toList()), fileName);
    }
}
