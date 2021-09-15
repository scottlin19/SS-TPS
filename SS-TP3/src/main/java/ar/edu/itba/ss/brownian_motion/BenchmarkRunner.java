package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.*;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;


public class BenchmarkRunner {



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        double L = 6;
        int maxIter = 20000;
        int N = 100;
        double minVelocity = 0, maxVelocity = 2.0;
        double smallRadius = 0.2, bigRadius = 0.7;
        double smallMass = 0.9, bigMass = 2;
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> varyN(L, maxIter, minVelocity, maxVelocity, smallRadius, bigRadius, bigMass, smallMass)).thenAccept(data->createFiles(data,true)),
                CompletableFuture.supplyAsync(() -> varyN(L, maxIter, minVelocity, maxVelocity, smallRadius, bigRadius, bigMass, smallMass)).thenAccept(data->createFiles(data,true)),
                CompletableFuture.supplyAsync(()->DCM(5,40,L,maxIter,130,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass)).thenAccept(data->createFiles(data,true))
                ).get();
//        CompletableFuture<Map<String, SimulationResult>> c1 = CompletableFuture.supplyAsync(() -> varyN(L, maxIter, minVelocity, maxVelocity, smallRadius, bigRadius, bigMass, smallMass));
//
//        CompletableFuture<Map<String, SimulationResult>> c2 = CompletableFuture.supplyAsync(() -> varyVelocity(L, maxIter, N, smallRadius, bigRadius, bigMass, smallMass));
//
//        CompletableFuture<Map<String, SimulationResult>> c3 = CompletableFuture.supplyAsync(()->DCM(10,10,L,maxIter,130,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass));


//        CompletableFuture<Void> combined = c1.thenCombineAsync(c2, (data, data2)->{
//            createFiles(data,true);
//            createFiles(data2,true);
//            return null;
//        });
//        combined.get();


    }

    private static void createFiles(Map<String,SimulationResult>  data,boolean createEXYZ){
        for(Map.Entry<String,SimulationResult> entry : data.entrySet()){
            if(createEXYZ){
                OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.EXYZ);
            }

            OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.JSON);
        }
    }



    private static Map<String,SimulationResult> varyN(double L, int maxIter, double minVelocity, double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){

        List<Integer> Ns = Arrays.asList(100,115,130);
//        List<String>  outputFiles  =  Ns.stream().map(n-> String.format("ej1/simulationN%dV%d-%d",n,(int) minVelocity,(int) maxVelocity)).collect(Collectors.toList());
        Map<String,SimulationResult> results = new HashMap<>();
        for (Integer n : Ns) {
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(n, L, maxIter, minVelocity, maxVelocity, smallParticleRadius, bigParticleRadius, bigMass, smallMass, null);
            List<Particle> particles = ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm = new BrownianMotion(particles, L, bigBoi);
            CutCondition bigParticlecc = new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            results.put(String.format("ej1/simulationN%dV%d-%d", particles.size(), (int) minVelocity, (int) maxVelocity), bm.getResult());

        }
        return results;
    }
    private static Map<String,SimulationResult> varyVelocity(double L, int maxIter,int N, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){
        List<Pair<Double,Double>> velocities = Arrays.asList(new Pair<>(0.0,5.0),new Pair<>(0.5,1.0),new Pair<>(1.0,2.0),new Pair<>(4.0,6.0));
//        List<String>  outputFiles  =  velocities.stream().map(p-> String.format("ej3/simulationN%dV%f-%f",N,p.getLeft(),p.getRight())).collect(Collectors.toList());
        Map<String,SimulationResult> results = new HashMap<>();
        for (Pair<Double, Double> velocity : velocities) {
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N, L, maxIter, velocities.get(0).getLeft(), velocities.get(0).getRight(), smallParticleRadius, bigParticleRadius, bigMass, smallMass, null);
            List<Particle> particles = ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm = new BrownianMotion(particles, L, bigBoi);
            CutCondition bigParticlecc = new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            results.put(String.format("ej3/simulationN%dV%d-%d", particles.size(), velocity.getLeft().intValue(), velocity.getRight().intValue()), bm.getResult());

        }
        return results;
    }

    private static Map<String,SimulationResult> DCM(int simulationIterations,int stepCount,double L, int maxIter,int N,double minVelocity,double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){
//        List<String>  outputFiles  =  velocities.stream().map(p-> String.format("ej3/simulationN%dV%f-%f",N,p.getLeft(),p.getRight())).collect(Collectors.toList());
        Map<String,SimulationResult> mapResults = new HashMap<>();
        List<SimulationResult> results = new ArrayList<>();
       for(int i = 0; i< simulationIterations; i++){
           RandomParticlesGeneratorConfig config =  new RandomParticlesGeneratorConfig(N, L, maxIter, minVelocity, maxVelocity, smallParticleRadius, bigParticleRadius, bigMass, smallMass, null);
           while(config.getN() != N){
               config =  new RandomParticlesGeneratorConfig(N, L, maxIter, minVelocity, maxVelocity, smallParticleRadius, bigParticleRadius, bigMass, smallMass, null);
           }
           List<Particle> particles = ResourcesGenerator.generateParticles(config);
           Particle bigBoi = particles.get(0);
           BrownianMotion bm = new BrownianMotion(particles, L, bigBoi);
           CutCondition bigParticlecc = new BigParticleCutCondition(bigBoi);
           CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
           bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
           results.add(bm.getResult());
       }
       double minTime = results.stream().mapToDouble(SimulationResult::getTotalTime).min().orElseThrow(IllegalStateException::new);
        for(int i = 0; i < results.size(); i++){
            mapResults.put(String.format("ej4/simulation%dN%dV%d-%d",i, results.get(i).getSnapshots().get(0).getParticles().size(), (int)minVelocity, (int)maxVelocity), getSnapshotsAtClock(results.get(i),minTime,stepCount));
        }



        return mapResults;
    }

    private static SimulationResult getSnapshotsAtClock(SimulationResult from,double maxTime,double stepCount){
        System.out.println("maxtime: "+maxTime);
        if(stepCount == 0){
            throw new IllegalArgumentException();
        }

        List<SimulationSnapshot> snapshots = from.getSnapshots();

        double step = maxTime/stepCount;
        List<Double> clockTimes = DoubleStream.iterate(step, d -> d <= maxTime, d -> d + step).boxed().collect(Collectors.toList());
        int lastIndex = 0;
        List<SimulationSnapshot> clockedEvents = new ArrayList<>();
        //System.out.println("totalTime: "+totalTime+", stepCount= "+ stepCount+", step= "+step+", clockTimes: "+clockTimes);
        for(Double time: clockTimes){
           // System.out.println(".Time: "+time);
           double lastTime = 0;
           while(lastIndex < snapshots.size() && lastTime < time) {
               lastTime = snapshots.get(lastIndex++).getEvent().getRelativeTime();

           }
           //System.out.println(lastIndex-1+") upper lastTime: "+lastTime);

           if(lastIndex ==  snapshots.size()){
              // System.out.println("lastIndex == size");
               clockedEvents.add(snapshots.get(lastIndex-1));
               break;
           }else if(lastIndex == 0){
               clockedEvents.add(snapshots.get(0));
               break;
           }else{
               //System.out.println("LASTINDEX: "+lastIndex);
               SimulationSnapshot upper= snapshots.get(lastIndex-1);
               SimulationSnapshot lower = snapshots.get(lastIndex-2);
               double upperDiff = Math.abs(time - upper.getEvent().getTime());
               double lowerDiff = Math.abs(time - lower.getEvent().getTime());
               if(lowerDiff < upperDiff){
                   clockedEvents.add(lower);
                   lastIndex--;
               }else{
                   clockedEvents.add(upper);
               }
           }

        }
        return new SimulationResult(from.getTotalCollisions(), maxTime,clockedEvents);
    }
}
