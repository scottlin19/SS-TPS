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
        int maxIter = 50000;
        int N = 100;
        double minVelocity = 0, maxVelocity = 2.0;
        double smallRadius = 0.2, bigRadius = 0.7;
        double smallMass = 0.9, bigMass = 2;
        CompletableFuture.allOf(
                CompletableFuture.supplyAsync(() -> varyN(L, maxIter, minVelocity, maxVelocity, smallRadius, bigRadius, bigMass, smallMass)).thenAccept(data->createFiles(data,false)),
                CompletableFuture.supplyAsync(() -> varyVelocity(L, maxIter, N, smallRadius, bigRadius, bigMass, smallMass)).thenAccept(data->createFiles(data,false)),
                CompletableFuture.supplyAsync(()->DCM(25,20,L,maxIter,130,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass)).thenAccept(data->createFiles(data,false))
                ).get();
//        Map<String,SimulationResult> varyNResult =  varyN(L, maxIter, minVelocity, maxVelocity, smallRadius, bigRadius, bigMass, smallMass);
//        createFiles(varyNResult,true);
//        Map<String,SimulationResult> varyVelocityResult =  varyVelocity(L, maxIter, N, smallRadius, bigRadius, bigMass, smallMass);
//        createFiles(varyVelocityResult,true);
//        Map<String,SimulationResult> DCMResult =  DCM(25,20,L,maxIter,130,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass);
//        createFiles(DCMResult,false);
        //CompletableFuture.supplyAsync(()->DCM(5,40,L,maxIter,130,minVelocity,maxVelocity,smallRadius,bigRadius,bigMass,smallMass)).thenAccept(data->createFiles(data,true)).get();

    }

    private static void createFiles(Map<String,SimulationResult>  data,boolean createEXYZ){
        for(Map.Entry<String,SimulationResult> entry : data.entrySet()){
            if(createEXYZ){
                System.out.printf("Writing %s.exyz\n", entry.getKey());
                OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.EXYZ);
            }
            System.out.printf("Writing %s.json\n", entry.getKey());
            OutputFile.createOutputFile(entry.getValue(), entry.getKey(), OutputTypeEnum.JSON);
        }
    }
    private static void createFiles2(String fileName, SimulationResult  data,boolean createEXYZ){

        if(createEXYZ){
            System.out.printf("Writing %s.exyz\n", fileName);
            OutputFile.createOutputFile(data, fileName, OutputTypeEnum.EXYZ);
        }
        System.out.printf("Writing %s.json\n", fileName);
        OutputFile.createOutputFile(data, fileName, OutputTypeEnum.JSON);

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
            //createFiles2(String.format("ej1/simulationN%dV%.2f-%.2f", particles.size(), minVelocity, maxVelocity),bm.getResult(),true);

        }

        return results;
    }
    private static Map<String,SimulationResult> varyVelocity(double L, int maxIter,int N, double smallParticleRadius, double bigParticleRadius, double bigMass, double smallMass){
        List<Pair<Double,Double>> velocities = Arrays.asList(new Pair<>(0.0,0.5),new Pair<>(1.0,2.0),new Pair<>(4.0,6.0));
        Map<String,SimulationResult> mapResults = new HashMap<>();
        List<SimulationResult> results =  new ArrayList<>();
        for (Pair<Double, Double> velocity : velocities) {
            System.out.println("VEL: "+velocity.getLeft()+" , "+velocity.getRight());
            RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N, L, maxIter,velocity.getLeft(), velocity.getRight(), smallParticleRadius, bigParticleRadius, bigMass, smallMass, null);
            List<Particle> particles = ResourcesGenerator.generateParticles(config);
            Particle bigBoi = particles.get(0);
            BrownianMotion bm = new BrownianMotion(particles, L, bigBoi);
            CutCondition bigParticlecc = new BigParticleCutCondition(bigBoi);
            CutCondition maxEventscc = new MaxEventsCutCondition(maxIter);
            bm.simulate((event) -> bigParticlecc.cut(event) || maxEventscc.cut(event));
            results.add(bm.getResult());

        }
        double minTime = results.stream().mapToDouble(SimulationResult::getTotalTime).min().orElseThrow(IllegalStateException::new);
        System.out.println("Vary Velocity Benchmark: MINIMUM TIME = "+minTime);
        for(int i = 0; i < results.size(); i++){
            mapResults.put(String.format("ej3/simulation%dN%dV%.2f-%.2f",i, results.get(i).getSnapshots().get(0).getParticles().size(), velocities.get(i).getLeft(), velocities.get(i).getRight()), getEventsUntilTime(results.get(i),minTime));
        }
        //createFiles2(String.format("ej3/simulationN%dV%.2f-%.2f", particles.size(), velocity.getLeft(), velocity.getRight()),bm.getResult(),true);
        return mapResults;
    }

    private static SimulationResult getEventsUntilTime(SimulationResult results,double minTime){

        int i = 0;
        double totalTime = results.getTotalTime();
        for(SimulationSnapshot ss: results.getSnapshots()){
            double relativeTime = ss.getEvent().getRelativeTime();
            if(relativeTime >= minTime){
                totalTime = relativeTime;
                break;
            }
            i++;
        }
        List<SimulationSnapshot> sublist = results.getSnapshots().subList(0,i);
        int totalCollisions = sublist.size();

        return new SimulationResult(totalCollisions,totalTime,sublist);
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
            mapResults.put(String.format("ej4/simulation%dN%dV%.2f-%.2f",i, results.get(i).getSnapshots().get(0).getParticles().size(), minVelocity, maxVelocity), getSnapshotsAtClock(results.get(i),minTime,stepCount));
        }



        return mapResults;
    }

    private static SimulationResult getSnapshotsAtClock(SimulationResult from,double maxTime,double stepCount){

        if(stepCount == 0){
            throw new IllegalArgumentException();
        }

        List<SimulationSnapshot> snapshots = from.getSnapshots();

        double step = maxTime/stepCount;
        List<Double> clockTimes = DoubleStream.iterate(step, d -> d <= maxTime, d -> d + step).boxed().collect(Collectors.toList());
        int lastIndex = 0;
        List<SimulationSnapshot> clockedEvents = new ArrayList<>();
        clockedEvents.add(snapshots.get(0));
        //ystem.out.println("totalTime: "+maxTime+", stepCount= "+ stepCount+", step= "+step+", clockTimes: "+clockTimes);
        for(Double time: clockTimes){

           double lastTime = 0;
           while(lastIndex < snapshots.size() && lastTime < time) {
               lastTime = snapshots.get(lastIndex++).getEvent().getRelativeTime();

           }


           if(lastIndex ==  snapshots.size()){

               clockedEvents.add(snapshots.get(lastIndex-1));
               break;
           }else if(lastIndex == 0){

               clockedEvents.add(snapshots.get(0));
               break;
           }else{

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
