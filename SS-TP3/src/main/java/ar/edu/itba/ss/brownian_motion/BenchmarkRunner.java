package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.commons.OutputTypeEnum;
import ar.edu.itba.ss.grid.Particle;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BenchmarkRunner {



    public static void main(String[] args) {
        double L = 6;
        int maxIter = 10000;
        double minVelocity = 0,maxVelocity = 2.0;
        double smallRadius =  0.2,bigRadius = 0.7;
        double smallMass = 0.9,bigMass = 2;

        Ej1(L,maxIter,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass);
    }

    private static void Ej1(double L, int maxIter, double minVelocity, double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){

        List<Integer> Ns = Arrays.asList(100,125,150);
        List<String>  outputFiles  =  Ns.stream().map(n-> String.format("ej1/simulationN%dV%f-%f",n,minVelocity,maxVelocity)).collect(Collectors.toList());
        for (int i  = 0 ; i < Ns.size(); i++){
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(i),L,maxIter,minVelocity,maxVelocity,smallParticleRadius,bigParticleRadius,bigMass,smallMass, outputFiles.get(i));
            List<Particle> particles =  ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm =  new BrownianMotion(particles,L,bigBoi);
            CutCondition bigParticlecc= new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(10000);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            OutputFile.createOutputFile(bm, config.getOutputFile(), OutputTypeEnum.EXYZ);
            OutputFile.createOutputFile(bm, config.getOutputFile(), OutputTypeEnum.JSON);
        }
    }
//    private static void Ej3(double L, int maxIter, double minVelocity, double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){
//        List<Double> minVelocities = Arrays.asList(0.0,1.0,2.0);
//        List<Double> maxVelocities = Arrays.asList(1.0,2.0,3.0);
//
//        List<String>  outputFiles  =  Ns.stream().map(n-> String.format("ej1/simulationN%dV%f-%f",n,minVelocity,maxVelocity)).collect(Collectors.toList());
//        for (int i  = 0 ; i < Ns.size(); i++){
//            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(i),L,maxIter,minVelocity,maxVelocity,smallParticleRadius,bigParticleRadius,bigMass,smallMass, outputFiles.get(i));
//            List<Particle> particles =  ResourcesGenerator.generateParticles(config);
//            Particle bigBoi = particles.get(0);
//            BrownianMotion bm =  new BrownianMotion(particles,L,bigBoi);
//            CutCondition bigParticlecc= new BigParticleCutCondition(bigBoi);
//            CutCondition maxEventscc = new MaxEventsCutCondition(10000);
//            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
//            OutputFile.createOutputFile(bm, config.getOutputFile(), OutputTypeEnum.EXYZ);
//            OutputFile.createOutputFile(bm, config.getOutputFile(), OutputTypeEnum.JSON);
//        }
//    }
}
