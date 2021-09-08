package ar.edu.itba.ss.resource_generation;

import ar.edu.itba.ss.cim.Grid;
import ar.edu.itba.ss.cim.Particle;
import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class ResourcesGenerator {

    private static final int MAX_ITER = 50000;
//    public static void main(String[] args) {
//        // pass the path to the file as a parameter
//        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/resources_generator_config.json");
//        if(config_url == null){
//            System.out.println("Config file not found, exiting...");
//            return;
//        }
//        try {
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
//            RandomParticlesGeneratorConfig config = new Gson().fromJson(bufferedReader, RandomParticlesGeneratorConfig.class);
//
//            StringBuilder static_data_sb = new StringBuilder();
//            StringBuilder dynamic_data_sb = new StringBuilder("t0\n");
//
//            List<Particle> particles = generateParticles(config);
//            String header = particles.size() + "\n" + config.getL() + "\n" + config.getRC() + "\n" + "1" + "\n" + config.getETA() + "\n" + config.getMaxIter() + "\n" + config.getInitialVelocity() + "\n";
//            static_data_sb.insert(0,header);
//            for(Particle particle: particles){
//                dynamic_data_sb.append(particle.getPosX()).append(" ").append(particle.getPosY()).append(" ").append(particle.getVelocity()).append(" ").append(particle.getDirection()).append("\n");
//                static_data_sb.append(particle.getRadius()).append("\n");
//
//            }
//            dynamic_data_sb.append("t1\n");
//
//            writeToResources("random_particles_dynamic_data.txt",dynamic_data_sb.toString());
//            writeToResources("random_particles_static_data.txt",static_data_sb.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }

    public static List<Particle> generateParticles(final RandomParticlesGeneratorConfig config) {
        if (config == null) {
            throw new NullPointerException("Config can't be null.");
        }

            final List<int[]> directions = new ArrayList<>() {
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

            int RC = 0;
//            boolean hasWalls = false;
            ThreadLocalRandom r = ThreadLocalRandom.current();
            double radiusLimit = 0.7;
            double minRadius = 0.2;

            int M = (int) Math.floor((L / (RC + 2 * (radiusLimit + minRadius))));

//            double cellLong  = (double)L/M;
            Grid grid = new Grid(L, M, RC, true, new ArrayList<>());
            int i = 0;
            int iter = 0;
            Particle bigParticle = new Particle(i++, (double)L/2, (double)L/2, radiusLimit, 2, 0 ,0);
            grid.addParticle(bigParticle);
//            Supplier<Double> radius_supplier = config.getParticleRadius() < 0 ? () -> r.nextDouble() * radiusLimit + minRadius : config::getParticleRadius;

            List<Particle> result = new ArrayList<>();
            while (i < N && iter < MAX_ITER) {
                iter++;
                double posx, posy;
                double velx, vely;

                posx = L * r.nextDouble();
                posy = L * r.nextDouble();
                velx = r.nextDouble() * 4 - 2;
                vely = Math.sqrt(4-Math.pow(velx,2));
                Particle particle = new Particle(i, posx, posy, minRadius, 0.7, velx, vely);

                int[] index = grid.addParticle(particle);
                grid.addNeighboursForParticle(directions, particle, index[0], index[1]);
                if (particle.hasNeighbours()) {
                    grid.removeParticle(particle);
                } else {
                    i++;
                    iter = 0;
                    result.add(particle);

                }
            }
            return result;

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


