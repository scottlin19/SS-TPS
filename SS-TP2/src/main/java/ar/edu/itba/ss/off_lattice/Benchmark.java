package ar.edu.itba.ss.off_lattice;

import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Benchmark {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        double noiseStep = 0.1;
        double maxNoise = 7;
        int varyEtaIterations = 1500;
        double density = 4.0;
        double RC = 1;
        double initialVelocity = 0.03;
        List<Integer> Ns = Arrays.asList(36,100,400,1024);
        CompletableFuture<Map<Integer,Map<Double,List<Double>>>> c1 = CompletableFuture.supplyAsync(()->varyETA(maxNoise,noiseStep,varyEtaIterations,density,RC,initialVelocity,Ns));
        double densityStep = 0.1;
        double maxDensity = 5;
        int varyDensityIterations = 1500;
        int L = 20;
        List<Double> etas = Arrays.asList(0.1,2.0,5.0);
        CompletableFuture<Map<Double,Map<Double,List<Double>>>> c2 = CompletableFuture.supplyAsync(()->varyDensity(maxDensity,densityStep,varyDensityIterations,L,RC,initialVelocity,etas));

        CompletableFuture<Void> combined = c1.thenCombineAsync(c2, (data, data2)->{
            OutputFile.createEtaBenchmarkOutputFile(data, "benchmark_varyEta3.json",varyEtaIterations,RC,density);
            OutputFile.createDensityBenchmarkOutputFile(data2, "benchmark_varyDensity3.json",varyDensityIterations,RC,L);
            return null;
        });
        combined.get();
    }

    private static Map<Double, Map<Double, List<Double>>> varyDensity(double maxDensity,double step,int iterations, int L, double RC, double initialVelocity, List<Double> etas) {

        double particleRadius = 0;
        List<Double> densities = IntStream.range(1,(int)(maxDensity*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        System.out.println("densities: "+densities);

        int surface = (int)Math.pow(L,2);
        List<Integer> Ns = densities.stream().map(d-> (int)(d*surface)).collect(Collectors.toList());
        System.out.println(densities);
        System.out.println("Ns: "+Ns);



        Map<Double,Map<Double,List<Double>>> data = new HashMap<>();
           /* Map<Integer,Map<Double, List<Double>>> data
                N-> (Noise-> ( lista Vas))
            */

        for (Double eta : etas) {
            data.put(eta, new HashMap<>());
            System.out.println("--------------------------" + eta + "--------------------------");
            for (int j = 0; j < densities.size(); j++) {
                System.out.println("DENSITY = " + densities.get(j));
                RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(j), L, RC, false, eta, iterations, initialVelocity, particleRadius, null);
                List<Particle> particles = ResourcesGenerator.generateParticles(config);
                OffLattice ol = new OffLattice(particles, L, (int) Math.floor(L / RC), RC, config.getHasWalls(), iterations, eta);
                ol.simulate();
                data.get(eta).put(densities.get(j), ol.getVAs());
            }

        }
        return data;
    }

    private static Map<Integer, Map<Double,List<Double>>> varyETA(double maxNoise,double step,int iterations,double density, double RC,double initialVelocity,List<Integer> Ns) {

        double particleRadius = 0;
        List<Double> etas = IntStream.range(0,(int)(maxNoise*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        System.out.println(etas);

//        List<Integer> Ns = Arrays.asList(32,64,256,1024,4096,16384);

        List<Integer> Ls = Ns.stream().map(N-> (int)Math.sqrt(N/density)).collect(Collectors.toList());
        System.out.println("Ls: "+Ls);
       // System.out.println("Ls: "+Ls);

        Map<Integer,Map<Double,List<Double>>> data = new HashMap<>();
            /* Map<Integer,Map<Double, List<Double>>> data
                N-> (Noise-> ( lista Vas))
            */
        for (int i = 0; i < Ns.size(); i++){
            Integer N = Ns.get(i);
            data.put(N,new HashMap<>());
            System.out.println("--------------------------" + N + "--------------------------");
            for(Double eta: etas){
                RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N,Ls.get(i),RC,false,eta,iterations,initialVelocity,particleRadius, null);
                List<Particle> particles = ResourcesGenerator.generateParticles(config);
                OffLattice ol = new OffLattice(particles,Ls.get(i), (int) Math.floor(Ls.get(i)/RC),RC, config.getHasWalls(), iterations,eta);
                ol.simulate();
                data.get(N).put(eta,ol.getVAs());
            }

        }
        return data;
    }


}