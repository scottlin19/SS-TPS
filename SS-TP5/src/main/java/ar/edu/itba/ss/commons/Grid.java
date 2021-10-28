package ar.edu.itba.ss.commons;
import java.util.*;


public class Grid {

    private static final String RESULTS_DIRECTORY = "results/";
    final private double L;
    final private int M;
    final private double cellLong;
    private final Cell[][] grid;
    private final List<Particle> particles;

    final private double rMin,rMax;
    final private double vdMax;

    public Grid(double L,double rMin,double rMax,double vdMax, List<Particle> particles){
        this.L = L;
        this.rMin = rMin;
        this.rMax = rMax;
        this.vdMax = vdMax;
        this.M = (int) Math.floor((L / (rMax + 2 *rMax)));
        this.cellLong = L / M; // Tiene que ser entero?
        this.grid = new Cell[M][M];
        this.particles = particles;
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }

    public void completeGrid(){
        particles.forEach(particle ->{
            int gridI = (int) (Math.floor(particle.getPosY()/cellLong));
            int gridJ = (int) (Math.floor(particle.getPosX()/cellLong));
            grid[gridI][gridJ].getParticles().add(particle);
        } );

    }

    public Cell getCellFromGrid(int i, int j){
        return grid[i][j];
    }


    public void clearGrid(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                grid[i][j].getParticles().clear();
            }
        }
    }

    public void updateGrid(){
        clearGrid();
        completeGrid();
    }

    public void updateParticles(){
        this.particles.forEach(particle -> particle.update(L));
    }

    public List<Cell> getCellNeighbours(List<int[]> directions,int i, int j){
        List<Cell> cells = new ArrayList<>();
        for (int[] dir : directions){
            int di = i+dir[0];
            int dj = j+dir[1];
            if(dj < 0 || dj >= this.M || di < 0 || di >= this.M) {
                continue;
            }
            cells.add(this.grid[di][dj]);
        }
        return cells;
    }


    public void printAll(){
        System.out.println("###############################################################################");
        particles.forEach(System.out::println);
    }
    public void printGrid(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasParticles()){
                    Set<Particle> particles = curr.getParticles();
                    for(Particle particle: particles){
                        System.out.println(particle);
                    }
                }
                System.out.println("###############################################################################");
            }
        }
    }



}

