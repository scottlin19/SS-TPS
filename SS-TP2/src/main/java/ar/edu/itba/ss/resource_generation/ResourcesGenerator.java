package ar.edu.itba.ss.resource_generation;

import ar.edu.itba.ss.cim.Grid;
import ar.edu.itba.ss.cim.Particle;
import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

public class ResourcesGenerator {

    private static final int MAX_ITER = 50000;

    public static void main(String[] args) {
        // pass the path to the file as a parameter
        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/resources_generator_config.json");
        if(config_url == null){
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            RandomParticlesGeneratorConfig config = new Gson().fromJson(bufferedReader, RandomParticlesGeneratorConfig.class);
            final List<int[]> directions = new ArrayList<>(){
                {
                    // add(new int[]{0, 0});
                    add(new int[]{-1, 0});
                    add(new int[]{-1, 1});
                    add(new int[]{0, 1});
                    add(new int[]{1, 1});
                    add(new int[]{1, 0});
                    add(new int[]{0, -1});
                    add(new int[]{-1, -1});
                    add(new int[]{1, -1});


                }
            };
            int N = config.getN();
            int L = config.getL();
            double velocity = config.getInitialVelocity();
            int RC = 0;
//            boolean hasWalls = false;
            Random r = new Random();
            double radiusLimit = 0.7;
            double minRadius = 0.1;

            int M = (int) Math.floor((L/(RC + 2*(radiusLimit+minRadius))));
//            double cellLong  = (double)L/M;
            Grid grid = new Grid(L,M,RC,false, new ArrayList<>());
            int i = 0;
            int iter = 0;
            StringBuilder static_data_sb = new StringBuilder();
            StringBuilder dynamic_data_sb = new StringBuilder("t0\n");

            Supplier<Double> radius_supplier = config.getParticle_radius() < 0 ? () -> r.nextDouble()*radiusLimit + minRadius : config::getParticle_radius;

            while(i < N && iter < MAX_ITER){
                System.out.println("ITER: "+iter + ", i: "+i);
                iter++;
                double posx,posy;
                double radius = radius_supplier.get();
                posx = L * r.nextDouble();
                posy = L * r.nextDouble();

                Particle particle = new Particle(i,posx,posy,radius,0.0,0.0);

                int[] index = grid.addParticle(particle);
                grid.addNeighboursForParticle(directions,particle, index[0], index[1]);
                if(particle.hasNeighbours()){
                    grid.removeParticle(particle);
                }
                else{
                    i++;
                    iter = 0;

                    double direction = r.nextDouble()*2*Math.PI;

                    dynamic_data_sb.append(posx).append(" ").append(posy).append(" ").append(velocity).append(" ").append(direction).append("\n");
                    static_data_sb.append(radius).append("\n");
                }
            }
            String header = i + "\n" + L + "\n" + config.getRC() + "\n" + (config.getHasWalls() ? "1" : "0") + "\n" + config.getETA() + "\n" + config.getMaxIter() + "\n" + config.getInitialVelocity() + "\n";
            static_data_sb.insert(0,header);
            dynamic_data_sb.append("t1\n");
            grid.printGrid();
            writeToResources("random_particles_dynamic_data.txt",dynamic_data_sb.toString());
            writeToResources("random_particles_static_data.txt",static_data_sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void writeToResources(String resource, String content){
        String path = "src/main/resources/" + resource;

        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


