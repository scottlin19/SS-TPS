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
        double step = 1;
        double maxNoise = 5;
        List<Double> noises = IntStream.range(0,(int)(maxNoise*(1/step) + 1)).mapToDouble(i-> i*step).boxed().collect(Collectors.toList());
        System.out.println(noises);
        int iterations = 200;
        double density = 4.0;
        double RC = 1;
        double initialVelocity = 0.03;
        double particleRadius = 0;
        List<Integer> Ns = Arrays.asList(32,64,256);
//        List<Integer> Ns = Arrays.asList(32,64,256,1024,4096,8464);
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
            for(Double noise: noises){

                List<List<Double>> iterationVAs = new ArrayList<>();
                for(int j = 0; j < simulationIterations; j++){
                    RandomParticlesGeneratorConfig config = new RandomParticlesGeneratorConfig(N,Ls.get(i),RC,false,noise,iterations,initialVelocity,particleRadius);
                    List<Particle> particles = ResourcesGenerator.generateParticles(config);
                    OffLattice ol = new OffLattice(particles,Ls.get(i), (int) Math.floor(Ls.get(i)/RC),RC, config.getHasWalls(), iterations,noise);
                    ol.simulate();
                    iterationVAs.add(ol.getVAs());
                }
                data.get(N).put(noise,calculatePromStd(iterationVAs));
            }

        }
        OutputFile.createVAOutputFile(data, "benchmark_results.json");
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