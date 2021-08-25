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
                double colorProportion = direction < Math.PI ? direction / Math.PI : (direction-Math.PI) / Math.PI;

                double redProportion = direction < Math.PI ? 1 - colorProportion : colorProportion;
                double greenProportion = 0;
                double blueProportion = 1 - redProportion;

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
   public static void createVAOutputFile(Map<Integer,Map<Double, Map<String,List<Double>>>> datas, String fileName) {
       JsonObject resp = new JsonObject();
       Set<Integer> Ns = datas.keySet();
       JsonArray datasJson = new JsonArray();

       for (Integer n: Ns) {
           JsonObject data = new JsonObject();
           JsonArray etasJson = new JsonArray();

           Map<Double,Map<String,List<Double>>> etas = datas.get(n);
           for (Double eta: etas.keySet()) {
               JsonObject etaJson = new JsonObject();
               Map<String,List<Double>> etaData = etas.get(eta);
               JsonArray avgs = new JsonArray();
               JsonArray stds = new JsonArray();

               etaData.get("avgs").forEach(avgs::add);
               etaData.get("stds").forEach(stds::add);

               etaJson.addProperty("eta",eta);
               etaJson.add("avgs",avgs);
               etaJson.add("stds",stds);

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


    public static void createVAOutputFile(List<List<Double>> VAs, Integer maxIterations, List<Double> eta, String fileName) {
        JsonObject resp = new JsonObject();
        JsonArray totalData = new JsonArray();

        int VAsSize = VAs.size();
        for (int i = 0; i < VAsSize; i++) {
            JsonObject data = new JsonObject();
            JsonArray VAData = new JsonArray();

            data.addProperty("eta",eta.get(i));
            VAs.get(i).forEach(VAData::add);
            data.add("vas",VAData);
            totalData.add(data);
        }

        resp.addProperty("vasQuantity",VAsSize);
        resp.addProperty("iterations", maxIterations);
        resp.add("data", totalData);

        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
            fw.write(resp.getAsString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
