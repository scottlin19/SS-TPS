package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.BrownianMotion;
import ar.edu.itba.ss.brownian_motion.Event;
import ar.edu.itba.ss.grid.Particle;

import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum OutputTypeEnum {
    EXYZ{
        @Override
        public String formatOutput(BrownianMotion bm) {
            List<SimulationSnapshot> simulationSnapshots = bm.getSnapshots();
            StringBuilder sb = new StringBuilder();
            List<List<Particle>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList());
            for(List<Particle> snapshot: snapshots){
                sb.append(snapshots.get(0).size()).append("\n\n");
                for(Particle p: snapshot){
                    sb.append(p.getPosX()).append(" ").append(p.getPosY()).append(" ").append(p.getVelX()).append(" ").append(p.getVelY()).append(" ").append(p.getMass()).append(" ").append(p.getRadius()).append("\n");
                }
            }
            return sb.toString();
        }

        @Override
        public String addExtension(String outPath) {
            return outPath+".exyz";
        }
    },
    JSON{
        @Override
        public String formatOutput(BrownianMotion bm) {
            List<SimulationSnapshot> simulationSnapshots = bm.getSnapshots();
            JsonObject resp = new JsonObject();
            resp.addProperty("totalCollisions", bm.getCollisions());
            resp.addProperty("totalTime", bm.getTimeElapsed().toMillis());
            JsonArray data = new JsonArray();
            for(SimulationSnapshot snapshot: simulationSnapshots){
                JsonObject iteration = new JsonObject();
                List<Particle> particles = snapshot.getParticles();
                Event event = snapshot.getEvent();
                JsonObject eventData = new JsonObject();
                eventData.addProperty("time",event.getTime());
                List<Particle> eventParticles = event.getParticles();
                JsonArray eventParticlesData = new JsonArray();
                eventParticles.forEach(ep -> eventParticlesData.add(particleAsJson(ep)));
                eventData.add("collisionedParticles",eventParticlesData);
                iteration.add("event", eventData);
                JsonArray particlesData = new JsonArray();
                particles.forEach(p -> particlesData.add(particleAsJson(p)));
                iteration.add("particles", particlesData);
                data.add(iteration);
            }
            resp.add("data",data);
            return resp.toString();
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

    public abstract String formatOutput(BrownianMotion bm);
    public abstract String addExtension(String outPath);
}
