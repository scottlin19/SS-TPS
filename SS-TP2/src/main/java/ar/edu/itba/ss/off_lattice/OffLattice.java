package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.cim.Grid;
import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.DynamicFile;
import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.commons.StaticFile;
import com.google.gson.Gson;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import java.util.*;


public class OffLattice {

    private Grid grid;
    private final List<Particle> particles;

    private final StaticFile sf;

    private final List<List<ParticleDTO>> snapshots;

    private List<List<Double>> VAs;

    public OffLattice(String staticDataPath, String dynamicDataPath){
        this.particles = new ArrayList<>();
        this.sf =  new StaticFile(staticDataPath,particles);
        DynamicFile.initDynamicData(dynamicDataPath,particles);
        System.out.println(this.particles);
        this.snapshots = new ArrayList<>();
        VAs = new ArrayList<>();
    }

    public void simulate(){
            int iterations = sf.getMaxIterations();
            grid = new Grid(sf.getL(),sf.getMaxM(),sf.getRC(),sf.getHasWalls(),this.particles);
            grid.completeGrid();

            for (int i = 0; i < iterations; i++) {
                grid.updateNeighbours();
                snapshot(); // Obtengo todos los valores de la iteracion actual para la animacion
                //grid.printAll();
//                VAs.add(grid.getVAs(sf.getInitialVelocity()));
                grid.updateParticles();
                grid.updateGrid();
            }
//            VAs.forEach(System.out::println);
    }

    private void snapshot(){
        snapshots.add(grid.snapshot());
    }

    // Guarda a archivo XYZ
    public void saveResults(String fileName){
        OutputFile.createOutputFile(snapshots,fileName);
    }
}
