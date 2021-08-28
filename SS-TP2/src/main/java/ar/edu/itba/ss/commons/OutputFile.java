package ar.edu.itba.ss.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutputFile {

    private static final String RESULTS_DIRECTORY = "results/";

    public static void createOutputFile(List<List<ParticleDTO>> snapshots,String fileName){
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()){
            if(!directory.mkdir()){
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
        StringBuilder sb = new StringBuilder();

        for(List<ParticleDTO> snapshot: snapshots){
            sb.append(snapshots.get(0).size()).append("\n\n");
            for(ParticleDTO dto: snapshot){
                double direction = dto.getDirection() < 0 ?  2*Math.PI - Math.abs(dto.getDirection()) : dto.getDirection();
                boolean isFirstHalf = direction < Math.PI;
                double colorProportion = isFirstHalf ? direction / Math.PI : (direction-Math.PI) / Math.PI;

                double redProportion = isFirstHalf ? 1 - colorProportion : colorProportion;
                double greenProportion = 1-redProportion/4;
                double blueProportion = 1-redProportion;

                sb.append(dto.getPosX()).append(" ").append(dto.getPosY()).append(" ").append(direction).append(" ").append(redProportion).append(" ").append(greenProportion).append(" ").append(blueProportion).append("\n");
            }
        }
        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* Map<Integer,Map<Double, Map<String,List<Double>>>> data
        N-> (Noise-> ({ "avgs": lista Vas,
                        "stds": lista desviaciones estandar
                       }))
    */
   public static void createEtaBenchmarkOutputFile(Map<Integer,Map<Double, List<Double>>> datas, String fileName,int iterations,double RC, double density) {
       JsonObject resp = new JsonObject();
       Set<Integer> Ns = datas.keySet();
       JsonArray datasJson = new JsonArray();

       resp.addProperty("iterations",iterations);
       resp.addProperty("RC",RC);
       resp.addProperty("density",density);
       for (Integer n: Ns) {
           JsonObject data = new JsonObject();
           JsonArray etasJson = new JsonArray();

           Map<Double,List<Double>> etas = datas.get(n);
           for (Double eta: etas.keySet()) {
               JsonObject etaJson = new JsonObject();
               List<Double> etaData = etas.get(eta);
               JsonArray vas = new JsonArray();

               etaData.forEach(vas::add);

               etaJson.addProperty("eta",eta);
               etaJson.add("vas",vas);
               etasJson.add(etaJson);
           }

           data.addProperty("n",n);
           data.add("etas",etasJson);
           datasJson.add(data);
       }

       resp.add("data",datasJson);

       try {
           FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
           fw.write(new Gson().toJson(resp));
           fw.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }


    public static void createDensityBenchmarkOutputFile(Map<Double, Map<Double, List<Double>>> datas, String fileName,int iterations,double RC, int L) {
        JsonObject resp = new JsonObject();
        Set<Double> etas = datas.keySet();
        JsonArray datasJson = new JsonArray();
        resp.addProperty("iterations",iterations);
        resp.addProperty("RC",RC);
        resp.addProperty("L",L);
        for (Double eta : etas) {
            JsonObject data = new JsonObject();
            JsonArray densitiesJson = new JsonArray();

            Map<Double, List<Double>> densities = datas.get(eta);
            for (Double density : densities.keySet()) {
                JsonObject densityJson = new JsonObject();
                List<Double> etaData = densities.get(density);
                JsonArray vas = new JsonArray();
                etaData.forEach(vas::add);

                densityJson.addProperty("density", density);
                densityJson.add("vas", vas);

                densitiesJson.add(densityJson);
            }

            data.addProperty("eta", eta);
            data.add("densities", densitiesJson);
            datasJson.add(data);
        }

        resp.add("data", datasJson);

        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
            fw.write(new Gson().toJson(resp));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
