package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.cim.Grid;
import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.*;

import java.util.*;
import java.util.stream.Collectors;


public class OffLattice {

    private Grid grid;
    private List<Particle> particles;

    private final StaticData sd;

    private final List<GridSnapshot> snapshots;

    // Contruct OffLattice from static and dynamic files
    public OffLattice(String staticDataPath, String dynamicDataPath){
        this.particles = new ArrayList<>();
        this.sd = new StaticData(staticDataPath,particles);
        DynamicFile.initDynamicData(dynamicDataPath,particles);
        this.snapshots = new ArrayList<>();
    }

    // Contruct OffLattice from provided values (for benchmarking usage)
    public OffLattice(List<Particle> particles, int L, int maxM, double RC, boolean hasWalls, int maxIterations, double ETA){
        this.particles = particles;

        this.sd = new StaticData(L, maxM, RC, hasWalls, maxIterations, ETA);
        this.snapshots = new ArrayList<>();
    }

    public void setParticles(List<Particle> particles){
        this.particles = particles;
    }

    public void simulate(){
            int iterations = sd.getMaxIterations();
            grid = new Grid(sd.getL(),sd.getMaxM(),sd.getRC(),sd.getHasWalls(),this.particles);
            grid.completeGrid();

            for (int i = 0; i < iterations; i++) {
                grid.updateNeighbours();
                snapshot(); // Obtengo todos los valores de la iteracion actual para la animacion
                grid.updateParticles();
                grid.updateGrid();
            }

    }

    private void snapshot(){
        snapshots.add(grid.snapshot());
    }

    // Guarda a archivo XYZ
    public void saveResults(String fileName){
        OutputFile.createOutputFile(snapshots.stream().map(GridSnapshot::getParticlesData).collect(Collectors.toList()), fileName);
        OutputFile.createJsonOutputFile(snapshots.stream().map(GridSnapshot::getParticlesData).collect(Collectors.toList()), "simulation.json");
    }

    //Guarda archivo para los VAs en Json
    public void saveVAResults(String fileName){
//        OutputFile.createVAOutputFile(snapshots.stream().map(GridSnapshot::getVA).collect(Collectors.toList()), sd.getMaxIterations(), sd.getETA(), fileName);
    }


    /////////////////////////////////////////////////////////

    public List<List<ParticleDTO>> getSnapshotsData() {
        return snapshots.stream().map(GridSnapshot::getParticlesData).collect(Collectors.toList());
    }
}
