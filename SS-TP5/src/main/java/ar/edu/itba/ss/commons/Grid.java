package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.CPMConfig;

import static ar.edu.itba.ss.commons.Pedestrian.addNeighbour;
import static ar.edu.itba.ss.commons.Pedestrian.addWall;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.function.Consumer;


public class Grid {


    private final double                    L;
    private final int                       M;
    private final double                    cellLong;
    private final double                    rMin,rMax;
    private final double                    vdMax;
    private final int                       B;
    private final double                    tau;
    private final double                    entranceLength;

    private final Cell[][]                  grid;
    private final List<Pedestrian>          pedestrians;
    private final List<Wall>                walls;

    private final Target                    FIRST_TARGET;
    private final Target                    SECOND_TARGET;

    public Grid(CPMConfig config,boolean generate){
        this.L                              = config.getL();
        this.rMin                           = config.getrMin();
        this.rMax                           = config.getrMax();
        this.vdMax                          = config.getVdMax();
        this.B                              = config.getB();
        this.tau                            = config.getTau();
        this.entranceLength                 = config.getEntranceLength();
        this.M                              = (int) Math.floor((L / rMax ));
        this.cellLong                       = L / M;
        this.grid                           = new Cell[M][M];
        this.walls                          = initWalls();
        this.FIRST_TARGET = new Target(
            new Point2D.Double(L/2 - entranceLength/2 + 0.1, L),
            new Point2D.Double(L/2 + entranceLength/2 - 0.1, L),
            1
        );
        this.SECOND_TARGET = new Target(
            new Point2D.Double(L/2 - 1.5, L+10),
            new Point2D.Double(L/2 + 1.5, L+10),
            2
        );

        if(generate){
            this.pedestrians = generatePedestrians(config);
        }else{
            this.pedestrians                    = new LinkedList<>();
        }
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
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

    public List<Pedestrian> generatePedestrians(CPMConfig config){
        Random r = new Random();
        double deltaPos = (L-rMin-0.01);
        List<Pedestrian> pedestrians = new LinkedList<>();

        Grid grid = new Grid(config,false);
        long N = config.getN();
        int i = 0;
        int iter = 0;
        int maxIter = 50000;

        while (i < N && iter < maxIter) {
            iter++;

            double posX = r.nextDouble()*deltaPos+rMin;
            double posY = r.nextDouble()*deltaPos+rMin;
            Pedestrian pedestrian = new Pedestrian(i,new Point2D.Double(posX,posY),rMin,config.getVe(), FIRST_TARGET);

            int[] index = grid.addPedestrian(pedestrian);
            System.out.println("adding pedestrian: "+ pedestrian);
            grid.addNeighboursForPedestrian(pedestrian, index[0], index[1]);
            if (pedestrian.hasCollisions()) {
                System.out.println("has collisions: "+grid.pedestrians);
                grid.removePedestrian(pedestrian);
                grid.pedestrians.stream().filter(Pedestrian::hasCollisions).forEach(Pedestrian::clearCollisions);


            } else {
                i++;
                iter = 0;
                pedestrians.add(pedestrian);
            }
            System.out.printf("i = %d, iter = %d\n",i,iter);

        }

        return pedestrians;
    }

    public int[] addPedestrian(Pedestrian pedestrian){
        pedestrians.add(pedestrian);
        int gridI = (int) (Math.floor(pedestrian.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(pedestrian.getPosX()/cellLong));
        Cell cell = getCellFromGrid(gridI,gridJ);
        cell.getPedestrians().add(pedestrian);
        return new int[]{gridI,gridJ};
    }

    public void removePedestrian(Pedestrian pedestrian){
        pedestrians.remove(pedestrian);
        int gridI =(int) (Math.floor(pedestrian.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(pedestrian.getPosX()/cellLong));
        Cell cell = getCellFromGrid(gridI,gridJ);
        cell.getPedestrians().remove(pedestrian);
    }

    public void addNeighboursForPedestrian(Pedestrian particle, int gridI, int gridJ){
        Set<Pedestrian> auxSet = new HashSet<>();
        getCellNeighbours(CIMDirections,gridI,gridJ).forEach(c -> auxSet.addAll(c.getPedestrians()));
        addCollisions(particle,auxSet);
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

    private void checkTargetCollisions(Pedestrian p, Consumer<Pedestrian> callback){
        if(p.getTarget().equals(FIRST_TARGET) && FIRST_TARGET.checkCollision(p)){
            p.updateTarget(SECOND_TARGET);
        }
        else if(p.getTarget().equals(SECOND_TARGET) && SECOND_TARGET.checkCollision(p)){
            callback.accept(p);
        }
    }

    public List<Pedestrian> updatePedestrians(double deltaT){
        List<Pedestrian> arrived = new LinkedList<>();
        this.pedestrians.forEach(pedestrian -> {
            pedestrian.update(deltaT);
            checkTargetCollisions(pedestrian, arrived::add);
        });
        // snapshot
        List<Pedestrian> returned = List.copyOf(pedestrians);
        arrived.forEach(this::removePedestrian);

        return returned;
    }

    public boolean allPedestriansLeft(){
        return pedestrians.isEmpty();
    }

    public void updateCollisions(double deltaT){

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasPedestrians()){
                    Set<Pedestrian> auxSet = new HashSet<>(curr.getPedestrians());
                    getCellNeighbours(CIMDirections,i,j).forEach(c -> auxSet.addAll(c.getPedestrians()));

                    for(Pedestrian pedestrian: curr.getPedestrians()){
                        auxSet.remove(pedestrian);

                        addCollisions(pedestrian,auxSet);
                        addWallCollisions(pedestrian);
                    }
                }
            }
        }
        this.pedestrians.forEach((p) -> p.updateCollisions(deltaT, vdMax, rMin, rMax, B, tau));
    }
    public void addWallCollisions(Pedestrian pedestrian){
        this.walls.forEach(wall -> addWall(pedestrian,wall));
    }

    private Pair<Integer> gridPosForPedestrian(Pedestrian p){
        int gridI = (int) (Math.floor(p.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(p.getPosX()/cellLong));
        return new Pair<>(gridI,gridJ);
    }

    public void addCollisions(Pedestrian pedestrian, Set<Pedestrian> cellPedestrians) {
        cellPedestrians.forEach(p -> {
            if(!p.equals(pedestrian)){
                addNeighbour(pedestrian, p);
            }
        });
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

