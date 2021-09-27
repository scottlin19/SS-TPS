package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class Particle{

    private final int id;
    private double posX, posY;
    private double velX,velY;
    private double radius;
    private final double mass;
    private final SystemFunctions updatedFunctions;

    public Particle(int id,double posX,double posY,double radius, double mass, double velX, double velY, SystemFunctions updatedFunctions){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.updatedFunctions = updatedFunctions;
    }

    public double getForceX(){
        return 0;
    }

    public void update(double time){

        //  System.out.println("PREV POS p: "+id + ", X: "+posX + ", Y: "+posY + "radius: "+radius);
        posX += time * velX;
        posY += time * velY;
        // System.out.println("NUEVA POS p: "+id + ", X: "+posX + ", Y: "+posY+ "radius: "+radius);
    }


    /* ---------------- GETTERS, SETTERS, EQUALS, HASH, TOSTRING ---------------- */
    public int getId() {
        return id;
    }

    public double getVelX(){
        return velX;
    }



    public void setVelX(double velX){
        this.velX = velX;
    }

    public double getVelY(){
        return velY;
    }

    public void setVelY(double velY){
        this.velY = velY;
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



    public double getMass() {
        return mass;
    }


    @Override
    public String toString() {
        return "Particle{" +
            "id=" + id +
            ", posX=" + posX +
            ", posY=" + posY +
            ", velX=" + velX +
            ", velY=" + velY +
            ", radius=" + radius +
            ", mass=" + mass +
            '}';
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
