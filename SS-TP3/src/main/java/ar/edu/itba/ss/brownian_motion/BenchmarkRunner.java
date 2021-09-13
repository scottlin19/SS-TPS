package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.commons.OutputTypeEnum;
import ar.edu.itba.ss.commons.Pair;
import ar.edu.itba.ss.grid.Particle;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


public class BenchmarkRunner {



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        double L = 6;
        int maxIter = 20000;
        int N = 100;
        double minVelocity = 0,maxVelocity = 2.0;
        double smallRadius =  0.2,bigRadius = 0.7;
        double smallMass = 0.9,bigMass = 2;
        CompletableFuture<Map<String,BrownianMotion>> c1 = CompletableFuture.supplyAsync(()->varyN(L,maxIter,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass));

        CompletableFuture<Map<String,BrownianMotion>> c2 = CompletableFuture.supplyAsync(()->varyVelocity(L,maxIter,N,smallRadius,bigRadius,bigMass,smallMass));

        CompletableFuture<Void> combined = c1.thenCombineAsync(c2, (data, data2)->{
            createFiles(data);
            createFiles(data2);
            return null;
        });
        combined.get();

    }

    private static void createFiles(Map<String,BrownianMotion>  data){
        for(Map.Entry<String,BrownianMotion> entry : data.entrySet()){
            OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.EXYZ);
            OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.JSON);
        }
    }



    private static Map<String,BrownianMotion> varyN(double L, int maxIter, double minVelocity, double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){

        List<Integer> Ns = Arrays.asList(100,125,150);
//        List<String>  outputFiles  =  Ns.stream().map(n-> String.format("ej1/simulationN%dV%d-%d",n,(int) minVelocity,(int) maxVelocity)).collect(Collectors.toList());
        Map<String,BrownianMotion> results = new HashMap<>();
        for (int i  = 0 ; i < Ns.size(); i++){
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(i),L,maxIter,minVelocity,maxVelocity,smallParticleRadius,bigParticleRadius,bigMass,smallMass, null);
            List<Particle> particles =  ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm =  new BrownianMotion(particles,L,bigBoi);
            CutCondition bigParticlecc= new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            results.put(String.format("ej1/simulationN%dV%d-%d",particles.size(),(int) minVelocity,(int) maxVelocity),bm);

        }
        return results;
    }
    private static Map<String,BrownianMotion> varyVelocity(double L, int maxIter,int N, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){
        List<Pair<Double,Double>> velocities = Arrays.asList(new Pair<>(0.0,1.0),new Pair<>(1.0,2.0),new Pair<>(2.0,3.0));
//        List<String>  outputFiles  =  velocities.stream().map(p-> String.format("ej3/simulationN%dV%f-%f",N,p.getLeft(),p.getRight())).collect(Collectors.toList());
        Map<String,BrownianMotion> results = new HashMap<>();
        for (int i  = 0 ; i < velocities.size(); i++){
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N,L,maxIter,velocities.get(0).getLeft(),velocities.get(0).getRight(),smallParticleRadius,bigParticleRadius,bigMass,smallMass, null);
            List<Particle> particles =  ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm =  new BrownianMotion(particles,L,bigBoi);
            CutCondition bigParticlecc= new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            results.put(String.format("ej3/simulationN%dV%d-%d",particles.size(),velocities.get(i).getLeft().intValue(),velocities.get(i).getRight().intValue()),bm);

        }
        return results;
    }
}
