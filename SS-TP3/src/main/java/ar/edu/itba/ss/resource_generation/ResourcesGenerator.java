package ar.edu.itba.ss.resource_generation;

import ar.edu.itba.ss.brownian_motion.BrownianMotion;
import ar.edu.itba.ss.grid.Particle;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ResourcesGenerator {


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


            int N = config.getN();
            double L = config.getL();
            ThreadLocalRandom r = ThreadLocalRandom.current();

//            double cellLong  = (double)L/M;
            int i = 0;
            int iter = 0;
            Particle bigParticle = new Particle(i++, L/2, L/2, config.getBigParticleRadius(), config.getBigMass(), 0 ,0);

            List<Particle> result = new ArrayList<>();
            result.add(bigParticle);
            double smallRadius =  config.getSmallParticleRadius();
            double smallMass = config.getSmallMass();
            while (i < N && iter < config.getMaxIter()) {
                iter++;
                double posx, posy;
                double velx, vely;

                posx = 0.01+smallRadius + (L-2*smallRadius-0.01) * r.nextDouble();
                posy = 0.01+smallRadius + (L-2*smallRadius-0.01) * r.nextDouble();
                double maxVel = config.getMaxVelocity();
                velx = r.nextDouble() * 2* maxVel -  maxVel;
                double maxVelY = Math.sqrt(Math.pow(maxVel,2)-Math.pow(velx,2));
                vely = r.nextDouble() * 2 * maxVelY - maxVelY;
                Particle particle = new Particle(i, posx, posy, smallRadius, smallMass, velx, vely);
                if(!hasNeighbours(particle,result)){
                    result.add(particle);
                    i++;
                    iter = 0;
                }

            }
            return result;

    }

    private static boolean hasNeighbours(Particle p1, List<Particle> particles){

        for(Particle p2 : particles){
            double dist = Math.sqrt(Math.pow(p1.getPosX() - p2.getPosX(),2) + Math.pow(p1.getPosY() - p2.getPosY(),2)) - p1.getRadius() - p2.getRadius();
            if(dist <= 0){
                return true;
            }
        }
        return false;
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


