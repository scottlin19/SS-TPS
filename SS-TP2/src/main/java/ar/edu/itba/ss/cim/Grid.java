package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.off_lattice.ParticleDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Grid {

    private static final String RESULTS_DIRECTORY = "results/";
    final private int L;
    final private int M;
    final private double RC;
    final private double cellLong;
    final private boolean hasWalls;
    private final Cell[][] grid;
    private final List<Particle> particles;

    private final List<int[]> directions = new ArrayList<>(){
        {
            //add(new int[]{0, 0});
            add(new int[]{-1, 0});
            add(new int[]{-1, 1});
            add(new int[]{0, 1});
            add(new int[]{1, 1});
        }
    };

    public Grid(int L, int M,double RC,boolean hasWalls,List<Particle> particles){
        this.L = L;
        this.M = M;
        this.RC = RC;
        this.hasWalls = hasWalls;
        this.cellLong = (double)L / M; // Tiene que ser entero?
        this.grid = new Cell[M][M];
        this.particles = particles;
        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                this.grid[i][j] = new Cell();
            }
        }
    }

    public List<ParticleDTO> snapshot(){
        return this.particles.stream().map(ParticleDTO::fromParticle).collect(Collectors.toList());
    }

    public int[] addParticle(Particle particle){
        particles.add(particle);
        int gridI = (int) (Math.floor(particle.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(particle.getPosX()/cellLong));
        Cell cell = getCellFromGrid(gridI,gridJ);
        cell.getParticles().add(particle);
        return new int[]{gridI,gridJ};
    }

    public void removeParticle(Particle particle){
        particles.remove(particle);
        int gridI =(int) (Math.floor(particle.getPosY()/cellLong));
        int gridJ = (int) (Math.floor(particle.getPosX()/cellLong));
        Cell cell = getCellFromGrid(gridI,gridJ);
        cell.getParticles().remove(particle);
        Set<Integer> neigh_ids = particle.getNeighbours().keySet();
        particles.stream().filter(p -> neigh_ids.contains(p.getId())).forEach(p->p.getNeighbours().remove(particle.getId()));
    }

    public void addNeighboursForParticle(List<int[]> directions,Particle particle, int gridI, int gridJ){
        Cell cell = getCellFromGrid(gridI,gridJ);
        Map<Cell,List<int[]>> neighbourCells = getCellNeighbours(directions,gridI,gridJ);
        addNeighbours(particle,cell.getParticles(),neighbourCells,RC);
    }

    public void completeGrid(){

        particles.forEach(particle ->{
            int gridI = (int) (Math.floor(particle.getPosY()/cellLong));
            int gridJ = (int) (Math.floor(particle.getPosX()/cellLong));
            grid[gridI][gridJ].getParticles().add(particle);
        } );

        //particles.forEach(p -> grid[(int) (Math.floor(p.posX/M)-1)][(int) (Math.floor(p.posY/M)-1)].getParticles().add(p));
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
//        for (int i = 0; i < M; i++) {
//            for (int j = 0; j < M; j++) {
//                Set<Particle> gridParticles = grid[i][j].getParticles();
//                for (Particle p: gridParticles) {
//                    int gridI =(int) (Math.floor(p.getPosY()/cellLong));
//                    int gridJ = (int) (Math.floor(p.getPosX()/cellLong));
//                    if(i != gridI || j != gridJ ){
//                        grid[gridI][gridJ].getParticles().add(p);
//                        grid[i][j].getParticles().remove(p);
//                    }
//                }
//            }
//        }
        clearGrid();
        completeGrid();
        // M^2 + N
    }

    public void updateParticles(){
        this.particles.forEach(particle -> particle.update(L));
    }



    public void updateNeighbours(){
//        particleList.forEach(p ->{
//            int gridI =(int) (Math.floor(p.getPosY()/cellLong));
//            int gridJ = (int) (Math.floor(p.getPosX()/cellLong));
//            Cell curr = this.grid[gridI][gridJ];
//            Map<Cell,List<int[]>> neighbourCells = getCellNeighbours(this.directions,gridI,gridJ);
////            curr.getParticles().forEach(particle-> addNeighbours(particle,curr.getParticles(),neighbourCells,this.RC));
//            addNeighbours(p,curr.getParticles(),neighbourCells,this.RC);
////            Set<Particle> auxSet = new HashSet<>(curr.getParticles());
////            for(Particle particle: curr.getParticles()){
////                auxSet.remove(particle);
////
////                addNeighbours(particle,auxSet,neighbourCells,this.RC);
////            }
//
//        });

        for(int i = 0; i < M; i++){
            for(int j = 0; j < M; j++){
                Cell curr = this.grid[i][j];
                if(curr.hasParticles()){
                    Map<Cell,List<int[]>> neighbourCells = getCellNeighbours(this.directions,i,j);
                    Set<Particle> auxSet = new HashSet<>(curr.getParticles());
                    for(Particle particle: curr.getParticles()){
                        auxSet.remove(particle);

                        addNeighbours(particle,auxSet,neighbourCells,this.RC);
                    }
                }
            }
        }



    }

    public void bruteForceNeighbours(Particle p){
        for(int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                Cell curr = this.grid[i][j];
                if (curr.hasParticles()) {
                    for(Particle cell_particle : curr.getParticles()){
                        if(!cell_particle.equals(p)){
                            double dist = Math.sqrt(Math.pow((cell_particle.getPosX() - (p.getPosX())),2) + Math.pow((cell_particle.getPosY() - (p.getPosY())),2)) - cell_particle.getRadius() - p.getRadius();
                            if(dist <= RC){
                                p.getNeighbours().put(cell_particle.getId(), cell_particle.getDirection());
                                cell_particle.getNeighbours().put(p.getId(), p.getDirection());
                            }
                        }
                    }
                }
            }
        }
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

    private boolean isNeighbour(Particle p1, Particle p2, List<int[]> overflows,double RC){
        for(int[] overflow: overflows){
            double dist = Math.sqrt(Math.pow((p1.getPosX() - (p2.getPosX() + overflow[0])),2) + Math.pow((p1.getPosY() - (p2.getPosY() + overflow[1])),2)) - p1.getRadius() - p2.getRadius();
            if(dist <= RC){
                return true;
            }
        }
        return false;
    }

    public void addNeighbours(Particle particle,Set<Particle> cellParticles,Map<Cell,List<int[]>> neighbourCells,double RC){

        cellParticles.forEach(neighbour-> addNeighbour(particle,neighbour,new ArrayList<>(){{add(new int[]{0,0});}},RC));
        for(Map.Entry<Cell,List<int[]>> cell: neighbourCells.entrySet()){
            List<int[]> overflow = cell.getValue();
            cell.getKey().getParticles().forEach(neighbour-> addNeighbour(particle,neighbour,overflow,RC));

        }
    }


    private void addNeighbour(Particle particle, Particle neighbour,List<int[]> overflows,double RC){
        if(!particle.equals(neighbour)){
            if(isNeighbour(particle,neighbour,overflows,RC)){
                particle.getNeighbours().put(neighbour.getId(), neighbour.getDirection());
                neighbour.getNeighbours().put(particle.getId(), particle.getDirection());
            }
        }
    }



    public Map<Cell,List<int[]>> getCellNeighbours(List<int[]> directions,int i, int j){
        // Cell --> {0,-L}

        Map<Cell, List<int[]>> cells = new HashMap<>();

        for (int[] dir : directions){
            int overflowX = 0;
            int overflowY = 0;
            int dj = j+dir[1];
            int di = i+dir[0];

            if(this.hasWalls && ((dj < 0 || dj >= this.M) || (di < 0 || di >= this.M))) {
                continue;
            }

            if(dj < 0){
                overflowX = -this.L; // No pasa en nuestro caso
            }else if(dj >= this.M){
                overflowX = this.L;
            }

            if(di < 0){
                overflowY = -this.L;
            }else if(di >= this.M){
                overflowY = this.L;
            }

            int realJ = (dj + M) % this.M;
            int realI = (di + M) % this.M;
//          System.out.println("Neigbour cell["+realI+"]["+realJ+"] OVERFLOW = {"+overflowX+", "+overflowY+"}");
            Cell currentCell = this.grid[realI][realJ];
            if(cells.containsKey(currentCell)) {
                cells.get(currentCell).add(new int[]{overflowX,overflowY});
            }
            else{
                List<int[]> overflows = new ArrayList<>();
                overflows.add(new int[]{overflowX,overflowY});
                cells.put(currentCell,overflows);
            }
        }

        return cells;

    }


    public void dropDataToJSONFile(String jsonpath){
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()){
            if(!directory.mkdir()){
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(Particle.class, new ParticleSerializer()).create();
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"L\":").append(this.L).append(",\n");
        sb.append("\"M\":").append(this.M).append(",\n");
        sb.append("\"RC\":").append(this.RC).append(",\n");
        sb.append("\"hasWalls\":").append(this.hasWalls).append(",\n");
        sb.append("\"particles\": [\n");

        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.M; j++) {
                for(Particle p : this.grid[i][j].getParticles()){
                    sb.append(gson.toJson(p));
                    sb.append(",\n");
                }
            }
        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("]}");

        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + jsonpath);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Double> getVAs(double initialVelocity) {
        double counterX = 0;
        double counterY = 0;
        for (Particle p: particles) {
            counterX += p.getVelocity()*Math.cos(p.getDirection());
            counterY += p.getVelocity()*Math.sin(p.getDirection());
        }
        return List.of(Math.abs(counterX)/(particles.size()*initialVelocity),Math.abs(counterY)/(particles.size()*initialVelocity));
    }
}

