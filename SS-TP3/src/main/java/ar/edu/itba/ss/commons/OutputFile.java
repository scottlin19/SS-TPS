package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.BrownianMotion;
import ar.edu.itba.ss.grid.Particle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFile {

    private static final String RESULTS_DIRECTORY = "results/";

    public static void createOutputFile(BrownianMotion bm, String outPath, OutputTypeEnum outputType){
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()){
            if(!directory.mkdir()){
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
        String formattedOutput = outputType.formatOutput(bm);

        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + outputType.addExtension(outPath));
            fw.write(formattedOutput);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void createJsonOutputFile(List<List<ParticleDTO>> snapshots,String fileName){
//        File directory = new File(RESULTS_DIRECTORY);
//        if (!directory.exists()){
//            if(!directory.mkdir()){
//                System.out.println("Couldn't create directory results, exiting...");
//                System.exit(-1);
//            }
//        }
//        JsonObject resp = new JsonObject();
//        JsonArray datasJson = new JsonArray();
//        for(List<ParticleDTO> snapshot: snapshots){
//            JsonObject iteration = new JsonObject();
//            JsonArray particles = new JsonArray();
//            for(ParticleDTO dto: snapshot){
//                JsonObject particleHolder = new JsonObject();
//                particleHolder.addProperty("x",dto.getPosX());
//                particleHolder.addProperty("y",dto.getPosY());
//                particleHolder.addProperty("direction",dto.getDirection());
//                particles.add(particleHolder);
//            }
//            iteration.add("particles", particles);
//            datasJson.add(iteration);
//        }
//        resp.add("data",datasJson);
//        try {
//            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
//            fw.write(new Gson().toJson(resp));
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//   /* Map<Integer,Map<Double, Map<String,List<Double>>>> data
//        N-> (Noise-> ({ "avgs": lista Vas,
//                        "stds": lista desviaciones estandar
//                       }))
//    */
//   public static void createEtaBenchmarkOutputFile(Map<Integer,Map<Double, List<Double>>> datas, String fileName,int iterations,double RC, double density) {
//       JsonObject resp = new JsonObject();
//       Set<Integer> Ns = datas.keySet();
//       JsonArray datasJson = new JsonArray();
//
//       resp.addProperty("iterations",iterations);
//       resp.addProperty("RC",RC);
//       resp.addProperty("density",density);
//       for (Integer n: Ns) {
//           JsonObject data = new JsonObject();
//           JsonArray etasJson = new JsonArray();
//
//           Map<Double,List<Double>> etas = datas.get(n);
//           for (Double eta: etas.keySet()) {
//               JsonObject etaJson = new JsonObject();
//               List<Double> etaData = etas.get(eta);
//               JsonArray vas = new JsonArray();
//
//               etaData.forEach(vas::add);
//
//               etaJson.addProperty("eta",eta);
//               etaJson.add("vas",vas);
//               etasJson.add(etaJson);
//           }
//
//           data.addProperty("n",n);
//           data.add("etas",etasJson);
//           datasJson.add(data);
//       }
//
//       resp.add("data",datasJson);
//
//       try {
//           FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
//           fw.write(new Gson().toJson(resp));
//           fw.close();
//       } catch (IOException e) {
//           e.printStackTrace();
//       }
//
//   }
//
//
//    public static void createDensityBenchmarkOutputFile(Map<Double, Map<Double, List<Double>>> datas, String fileName,int iterations,double RC, int L) {
//        JsonObject resp = new JsonObject();
//        Set<Double> etas = datas.keySet();
//        JsonArray datasJson = new JsonArray();
//        resp.addProperty("iterations",iterations);
//        resp.addProperty("RC",RC);
//        resp.addProperty("L",L);
//        for (Double eta : etas) {
//            JsonObject data = new JsonObject();
//            JsonArray densitiesJson = new JsonArray();
//
//            Map<Double, List<Double>> densities = datas.get(eta);
//            for (Double density : densities.keySet()) {
//                JsonObject densityJson = new JsonObject();
//                List<Double> etaData = densities.get(density);
//                JsonArray vas = new JsonArray();
//                etaData.forEach(vas::add);
//
//                densityJson.addProperty("density", density);
//                densityJson.add("vas", vas);
//
//                densitiesJson.add(densityJson);
//            }
//
//            data.addProperty("eta", eta);
//            data.add("densities", densitiesJson);
//            datasJson.add(data);
//        }
//
//        resp.add("data", datasJson);
//
//        try {
//            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
//            fw.write(new Gson().toJson(resp));
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
