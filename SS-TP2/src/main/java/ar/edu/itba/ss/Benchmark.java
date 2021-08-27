package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.off_lattice.OffLattice;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Benchmark {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Map<Integer,Map<Double,Map<String,List<Double>>>>> c1 = CompletableFuture.supplyAsync(Benchmark::varyETA);
        CompletableFuture<Map<Double,Map<Double,Map<String,List<Double>>>>> c2 = CompletableFuture.supplyAsync(Benchmark::varyDensity);

        CompletableFuture<Void> combined = c1.thenCombineAsync(c2, (data, data2)->{
            OutputFile.createEtaBenchmarkOutputFile(data, "benchmark_varyEta.json");
            OutputFile.createDensityBenchmarkOutputFile(data2, "benchmark_varyDensity.json");
            return null;
        });
        combined.get();
    }

    private static Map<Double, Map<Double, Map<String, List<Double>>>> varyDensity() {

        double maxDensity = 10;
        double step = 0.5;
        List<Double> densities = IntStream.range(1,(int)(maxDensity*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        System.out.println("densities: "+densities);
        int L = 20;
        int surface = (int)Math.pow(L,2);
        List<Integer> Ns = densities.stream().map(d-> (int)(d*surface)).collect(Collectors.toList());
        System.out.println("Ns: "+Ns);
        List<Double> etas = Arrays.asList(0.1,2.0,5.0);
        int iterations = 1000;
        double RC = 1;
        double initialVelocity = 0.03;
        double particleRadius = 0;
        Map<Double,Map<Double,Map<String,List<Double>>>> data = new HashMap<>();
           /* Map<Integer,Map<Double, Map<String,List<Double>>>> data
                N-> (Noise-> ({ "avgs": lista Vas,
                                "stds": lista desviaciones estandar
                               }))
            */
        int simulationIterations = 5;
        for (Double eta : etas) {
            data.put(eta, new HashMap<>());
            System.out.println("--------------------------" + eta + "--------------------------");
            for (int j = 0; j < densities.size(); j++) {
                System.out.println("DENSITY = " + densities.get(j));
                List<List<Double>> iterationVAs = new ArrayList<>();
                for (int k = 0; k < simulationIterations; k++) {
                    System.out.println("density iter = " + k);
                    RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(j), L, RC, false, eta, iterations, initialVelocity, particleRadius);
                    List<Particle> particles = ResourcesGenerator.generateParticles(config);
                    OffLattice ol = new OffLattice(particles, L, (int) Math.floor(L / RC), RC, config.getHasWalls(), iterations, eta);
                    ol.simulate();
                    iterationVAs.add(ol.getVAs());
                }

                data.get(eta).put(densities.get(j), calculatePromStd(iterationVAs));
            }

        }
        return data;
    }

    private static Map<Integer, Map<Double, Map<String, List<Double>>>> varyETA() {
        double step = 0.5;
        double maxNoise = 5;
        List<Double> etas = IntStream.range(0,(int)(maxNoise*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        System.out.println(etas);
        int iterations = 1000;
        double density = 4.0;
        double RC = 1;
        double initialVelocity = 0.03;
        double particleRadius = 0;
//        List<Integer> Ns = Arrays.asList(32,64,256);
        List<Integer> Ns = Arrays.asList(32,64,256,1024,4096,8464);
//        List<Integer> Ns = Arrays.asList(32,64,256,1024,4096,16384);

        List<Integer> Ls = Ns.stream().map(N-> (int)Math.sqrt(N/density)).collect(Collectors.toList());
       // System.out.println("Ls: "+Ls);

        int simulationIterations = 5;
        Map<Integer,Map<Double,Map<String,List<Double>>>> data = new HashMap<>();
           /* Map<Integer,Map<Double, Map<String,List<Double>>>> data
                N-> (Noise-> ({ "avgs": lista Vas,
                                "stds": lista desviaciones estandar
                               }))
            */
        for (int i = 0; i < Ns.size(); i++){
            Integer N = Ns.get(i);
            data.put(N,new HashMap<>());
            System.out.println("--------------------------" + N + "--------------------------");
            for(Double eta: etas){
               // System.out.println("ETA = "+eta);
                List<List<Double>> iterationVAs = new ArrayList<>();
                for(int j = 0; j < simulationIterations; j++){
                 //   System.out.println("eta iter = "+j);
                    RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N,Ls.get(i),RC,false,eta,iterations,initialVelocity,particleRadius);
                    List<Particle> particles = ResourcesGenerator.generateParticles(config);
                    OffLattice ol = new OffLattice(particles,Ls.get(i), (int) Math.floor(Ls.get(i)/RC),RC, config.getHasWalls(), iterations,eta);
                    ol.simulate();
                    iterationVAs.add(ol.getVAs());
                }
                data.get(N).put(eta,calculatePromStd(iterationVAs));
            }

        }
        return data;
    }

    public static Map<String, List<Double>> calculatePromStd(List<List<Double>> iterationVAs){
        Map<String, List<Double>> map = new HashMap<>(){{
            put("avgs", new ArrayList<>());
            put("stds", new ArrayList<>());
        }};

        for(int i = 0; i < iterationVAs.get(0).size(); i++){
            int finalI = i;
            List<Double> values = iterationVAs.stream().map(valist -> valist.get(finalI)).collect(Collectors.toList());
            double avg = values.stream().mapToDouble(va -> va).average().orElse(0);
            map.get("avgs").add(avg);

            double standardDeviation = values.stream().reduce(0.0, (a,b) -> a + Math.pow(b - avg,2));

            map.get("stds").add(Math.sqrt(standardDeviation/values.size()));

        }

        return map;
    }
}