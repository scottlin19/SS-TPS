package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.BrownianMotion;
import ar.edu.itba.ss.brownian_motion.Event;
import ar.edu.itba.ss.brownian_motion.SimulationResult;
import ar.edu.itba.ss.grid.Particle;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum OutputTypeEnum {

    EXYZ{
        @Override
        public void createFile(SimulationResult result,String outPath)  {
            List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
            StringBuilder sb = new StringBuilder();
            List<List<Particle>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList());
            for(List<Particle> snapshot: snapshots){
                sb.append(snapshots.get(0).size()).append("\n\n");
                for(Particle p: snapshot){
                    sb.append(p.getPosX()).append(" ").append(p.getPosY()).append(" ").append(p.getVelX()).append(" ").append(p.getVelY()).append(" ").append(p.getMass()).append(" ").append(p.getRadius()).append("\n");
                }
            }
            File f = new File(addExtension(outPath));
            InputStream targetStream = new ByteArrayInputStream(sb.toString().getBytes());
            try {
                copyInputStreamToFile(targetStream,f);
                targetStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        public String addExtension(String outPath) {
            return outPath+".exyz";
        }
    },
    JSON{
        @Override
        public void createFile(SimulationResult result,String outPath) {
            try {

                ObjectMapper mapper = new ObjectMapper();
                // convert book object to JSON file
                mapper.writeValue(Paths.get(addExtension(outPath)).toFile(), result);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public String addExtension(String outPath) {
            return outPath+".json";
        }

        private JsonObject particleAsJson(Particle p){
            JsonObject particleData = new JsonObject();
            particleData.addProperty("id",p.getId());
            particleData.addProperty("posX",p.getPosX());
            particleData.addProperty("posY",p.getPosY());
            particleData.addProperty("velX",p.getVelX());
            particleData.addProperty("velY",p.getVelY());
            particleData.addProperty("mass",p.getMass());
            particleData.addProperty("radius",p.getRadius());
            return particleData;
        }
    };
    public final int DEFAULT_BUFFER_SIZE = 4096*2;
    public abstract void createFile(SimulationResult result,String outPath);
    public abstract String addExtension(String outPath);
    void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
