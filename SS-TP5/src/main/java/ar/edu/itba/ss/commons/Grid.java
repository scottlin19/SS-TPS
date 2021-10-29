package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.CPMConfig;

import static ar.edu.itba.ss.commons.Pedestrian.addNeighbour;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Supplier;


public class Grid {

    private static final String             RESULTS_DIRECTORY = "results/";

    private final double                    L;
    private final int                       M;
    private final double                    cellLong;
    private final Cell[][]                  grid;
    private final List<Pedestrian>          pedestrians;
    private final List<Wall>                walls;
    private final double                    rMin,rMax;
    private final double                    vdMax;
    private final int                       B;
    private final double                    tau;
    private final double                    entranceLength;

    public Grid(CPMConfig config){
        this.L                              = config.getL();
        this.rMin                           = config.getrMin();
        this.rMax                           = config.getrMax();
        this.vdMax                          = config.getVdMAX();
        this.B                              = config.getB();
        this.tau                            = config.getTau();
        this.entranceLength                 = config.getEntranceLength();
        this.M                              = (int) Math.floor((L / rMax ));
        this.cellLong                       = L / M;
        this.grid                           = new Cell[M][M];
        this.pedestrians                    = generatePedestrians(config);
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
        this.walls                          = initWalls();
    }

    private List<Wall> initWalls() {
        return List.of(
            new Wall(new Point2D.Double(0,0), new Point2D.Double(L,0), Wall.Orientation.HORIZONTAL),
            new Wall(new Point2D.Double(0,0), new Point2D.Double(0,L), Wall.Orientation.VERTICAL),
            new Wall(new Point2D.Double(L,0), new Point2D.Double(L,L), Wall.Orientation.VERTICAL),
            new Wall(new Point2D.Double(0,L), new Point2D.Double(L/2 - entranceLength/2,L), Wall.Orientation.HORIZONTAL),
            new Wall(new Point2D.Double(L/2 + entranceLength/2,L), new Point2D.Double(L,L), Wall.Orientation.HORIZONTAL)
        );
    }

    private List<Pedestrian> generatePedestrians(CPMConfig config){
        Random r = new Random();
        double deltaPos = (L-rMin-0.01);
        List<Pedestrian> pedestrians = new ArrayList<>();

        Grid grid = new Grid(config);
        long N = config.getN();
        int i = 0;
        int iter = 0;
        int maxIter = 1000;

        while (i < N && iter < maxIter) {
            iter++;

            double posX = r.nextDouble()*deltaPos + rMin;
            double posY = L * r.nextDouble();

        }
        Pedestrian pedestrian = new Pedestrian(i,new Point2D.Double(posX,posY),rMin,vdMax);

        int[] index = grid.addParticle(particle);
        grid.addNeighboursForParticle(directions, particle, index[0], index[1]);
        if (particle.hasNeighbours()) {
            grid.removeParticle(particle);
        } else {
            i++;
            iter = 0;
            result.add(particle);

        }
        return pedestrians;
    }

    public void removePedestrian(Pedestrian pedestrian){
        pedestrians.remove(pedestrian);
        int gridI =(int) (Math.floor(pedestrian.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(pedestrian.getPosX()/cellLong));
        Cell cell = getCellFromGrid(gridI,gridJ);
        cell.getPedestrians().remove(pedestrian);
        Set<Integer> neigh_ids = pedestrian.getc().keySet();
        particles.stream().filter(p -> neigh_ids.contains(p.getId())).forEach(p->p.getNeighbours().remove(particle.getId()));
    }

    public void addNeighboursForParticle(List<int[]> directions,Particle particle, int gridI, int gridJ){
        Cell cell = getCellFromGrid(gridI,gridJ);
        Map<Cell,List<int[]>> neighbourCells = getCellNeighbours(directions,gridI,gridJ);
        addNeighbours(particle,cell.getParticles(),neighbourCells,RC);
    }


    public void completeGrid(){
        pedestrians.forEach(p ->{
            int gridI = (int) (Math.floor(p.getPosY()/cellLong));
            int gridJ = (int) (Math.floor(p.getPosX()/cellLong));
            grid[gridI][gridJ].getPedestrians().add(p);
        } );

    }

    public Cell getCellFromGrid(int i, int j){
        return grid[i][j];
    }


    public void clearGrid(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                grid[i][j].getPedestrians().clear();
            }
        }
    }

    public void updateGrid(){
        clearGrid();
        completeGrid();
    }

    private final static List<int[]> CIMDirections = new LinkedList<>(){{
        add(new int[]{0,-1});
        add(new int[]{1,-1});
        add(new int[]{0,0});
        add(new int[]{1,0});
        add(new int[]{1,1});
    }};

    public void updatePedestrians(double deltaT){
        this.pedestrians.forEach(pedestrian -> pedestrian.update(deltaT));
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasPedestrians()){
                    Set<Pedestrian> auxSet = new HashSet<>(curr.getPedestrians());
                    getCellNeighbours(CIMDirections,i,j).forEach(c -> auxSet.addAll(c.getPedestrians()));

                    for(Pedestrian pedestrian: curr.getPedestrians()){
                        auxSet.remove(pedestrian);

                        addNeighbours(pedestrian,auxSet);
                        addWallCollisions(pedestrian);
                    }
               }
            }
        }
        this.pedestrians.forEach((p) -> p.updateCollisions(deltaT, vdMax, rMin, rMax, B, tau));
    }

    public void addWallCollisions(Pedestrian pedestrian){

    }

    public void addNeighbours(Pedestrian pedestrian, Set<Pedestrian> cellPedestrians) {
        cellPedestrians.forEach(p -> addNeighbour(pedestrian,p));
    }

    private Pair<Integer> gridPosForPedestrian(Pedestrian p){
        int gridI = (int) (Math.floor(p.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(p.getPosX()/cellLong));
        return new Pair<>(gridI,gridJ);
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
        pedestrians.forEach(System.out::println);
    }
    public void printGrid(){
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasPedestrians()){
                    Set<Pedestrian> pedestrians = curr.getPedestrians();
                    for(Pedestrian pedestrian: pedestrians){
                        System.out.println(pedestrian);
                    }
                }
                System.out.println("###############################################################################");
            }
        }
    }



}

