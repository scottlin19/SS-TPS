package ar.edu.itba.ss;

import ar.edu.itba.ss.cim.Particle;
import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.off_lattice.OffLattice;
import ar.edu.itba.ss.resource_generation.RandomParticlesGeneratorConfig;
import ar.edu.itba.ss.resource_generation.ResourcesGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Benchmark {

    public static void main(String[] args) {
        Map<Integer,Map<Double,Map<String,List<Double>>>> data = varyETA();

        OutputFile.createEtaBenchmarkOutputFile(data, "benchmark_varyEta.json");

        Map<Double,Map<Double,Map<String,List<Double>>>> data2 = varyDensity();

        OutputFile.createDensityBenchmarkOutputFile(data2, "benchmark_varyDensity.json");
    }

    private static Map<Double, Map<Double, Map<String, List<Double>>>> varyDensity() {

        double maxDensity = 10;
        double step = 0.5;
        List<Double> densities = IntStream.range(1,(int)(maxDensity*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        int L = 20;
        int surface = (int)Math.pow(L,2);
        List<Integer> Ns = densities.stream().map(d-> (int)(d*surface)).collect(Collectors.toList());
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
        for (int i = 0; i < etas.size(); i++){
            Double eta = etas.get(i);
            data.put(eta,new HashMap<>());
            System.out.println("--------------------------" + eta + "--------------------------");
            for(int j = 0; j < densities.size(); j++){
                List<List<Double>> iterationVAs = new ArrayList<>();
                for(int k = 0; k < simulationIterations; k++){
                    RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(Ns.get(j),L,RC,false,eta,iterations,initialVelocity,particleRadius);
                    List<Particle> particles = ResourcesGenerator.generateParticles(config);
                    OffLattice ol = new OffLattice(particles,L, (int) Math.floor(L/RC),RC, config.getHasWalls(), iterations,eta);
                    ol.simulate();
                    iterationVAs.add(ol.getVAs());
                }

                data.get(eta).put(densities.get(j),calculatePromStd(iterationVAs));
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
        System.out.println("Ls: "+Ls);

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

                List<List<Double>> iterationVAs = new ArrayList<>();
                for(int j = 0; j < simulationIterations; j++){
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