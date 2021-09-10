package ar.edu.itba.ss.grid;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ParticleSerializer implements JsonSerializer<Particle> {
    @Override
    public JsonElement serialize(Particle particle, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id",particle.getId());
        obj.addProperty("posX",particle.getPosX());
        obj.addProperty("posY",particle.getPosY());
        obj.addProperty("radius",particle.getRadius());

        JsonArray jarr = new JsonArray();
       // particle.getNeighbours().forEach( (n) -> jarr.add(n.getId()));
        obj.add("neighbours", jarr);

        return obj;
    }
}
