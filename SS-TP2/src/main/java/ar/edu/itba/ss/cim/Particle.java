package ar.edu.itba.ss.cim;

import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Particle{

    private final int id;
    private double posX, posY;
    private double velocity;
    private double direction;
    private Map<Integer, Double> neighboursDirs;
    private double radius;
    private final double dirNoise;




    public Particle(int id,double posX,double posY,double radius,double dirNoise,double initialVelocity){
        this.id = id;
        this.radius = radius;
        this.dirNoise = dirNoise;
        this.velocity = initialVelocity;
        this.posX = posX;
        this.posY = posY;

        this.neighboursDirs = new HashMap<>();
    }


    public void update(int limits){
        double newPosX = posX + velocity * Math.cos(direction);
        if(newPosX < 0){
            newPosX += limits;
        }else if (newPosX > limits){
            newPosX -= limits;
        }

        double newPosY = posY + velocity * Math.sin(direction);
        if(newPosY < 0){
            newPosY += limits;
        }else if (newPosY > limits){
            newPosY -= limits;
        }
//        System.out.println("Particle " + id + " PREV POSITIONS: " + posX + ", "+posY +  " NEW POSITIONS: "+newPosX + ", "+newPosY);
        posX = newPosX;
        posY = newPosY;

        List<Double> neighbourDirections = new ArrayList<>(neighboursDirs.values());
        neighbourDirections.add(direction);
        double sinProm = neighbourDirections.stream().mapToDouble(Math::sin).average().orElse(Double.NaN);
        double cosProm = neighbourDirections.stream().mapToDouble(Math::cos).average().orElse(Double.NaN);
        double dirProm = Math.atan2(sinProm,cosProm);

        direction = dirProm + dirNoise;

        clearNeighbours();

    }
    public void clearNeighbours() {
        neighboursDirs = new HashMap<>();
    }

    /* ---------------- GETTERS, SETTERS, EQUALS, HASH, TOSTRING ---------------- */
    public int getId() {
        return id;
    }

    public double getVelocity(){
        return velocity;
    }

    public void setVelocity(double velocity){
        this.velocity = velocity;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Map<Integer, Double> getNeighbours() {
        return neighboursDirs;
    }

    public void setNeighbours(Map<Integer, Double> neighboursDirs) {
        this.neighboursDirs = neighboursDirs;
    }

    public boolean hasNeighbours(){
        return !this.neighboursDirs.isEmpty();
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Particle: " + id + "{" +
                "posX=" + posX +
                ", posY=" + posY+", radius= "+radius+", neighbours= [");
        for(Map.Entry<Integer, Double> entry: neighboursDirs.entrySet()){
            sb.append("Particle").append(entry.getKey()).append("{ posX= ").append(entry.getValue()).append("}, ");
        }
        return sb.append("]}").toString();
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Particle)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Particle other = (Particle) o;

        // Compare the data members and return accordingly
        return this.id == other.id;
    }



}

