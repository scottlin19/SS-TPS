package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.commons.OutputTypeEnum;
import ar.edu.itba.ss.grid.CimConfig;
import ar.edu.itba.ss.grid.Particle;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

public class SimulationRunner {
    public static void main(String[] args) {


        simulateWithConfig();
        //simulateWithResourceFiles();

    }

    public static void simulateWithResourceFiles(){
        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/cim_config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            CimConfig config = new Gson().fromJson(bufferedReader, CimConfig.class);
            String static_file = config.getStaticFile();
            String dynamic_file = config.getDynamicFile();
//            BrownianMotion brownianMotion = new BrownianMotion();
//            OffLattice offLattice = new OffLattice(static_file, dynamic_file);
//            offLattice.simulate();
//            offLattice.saveResults(config.getOutputFile());

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }

    }

    public static void simulateWithConfig(){
        URL config_url = ResourcesGenerator.class.getClassLoader().getResource("config/resources_generator_config.json");
        if(config_url == null){
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            RandomParticlesGeneratorConfig config = new Gson().fromJson(bufferedReader, RandomParticlesGeneratorConfig.class);
            List<Particle> particles = ResourcesGenerator.generateParticles(config);
            Particle bigParticle = particles.get(0);
            BrownianMotion bm = new BrownianMotion(particles, config.getL(), bigParticle);
            CutCondition bigParticlecc= new BigParticleCutCondition(bigParticle);
            CutCondition maxEventscc = new MaxEventsCutCondition(10000);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));

            OutputFile.createOutputFile(bm.getResult(),config.getOutputFile(), OutputTypeEnum.EXYZ);
            OutputFile.createOutputFile(bm.getResult(),"simulation", OutputTypeEnum.JSON);
//            ol.saveResults(config.getOutputFile());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}


