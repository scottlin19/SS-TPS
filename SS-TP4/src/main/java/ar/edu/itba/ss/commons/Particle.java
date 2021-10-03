package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class Particle implements Cloneable{

    private final int       id;
    private double          posX, posY;
    private double          velX,velY;
    private double          accX,accY;
    private final double    radius;
    private final double    mass;

    public Particle(int id,double posX,double posY,double radius, double mass, double velX, double velY, double accX, double accY){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.accX = accX;
        this.accY = accY;
    }

    public double getForceX(){
        return mass * accX;
    }

    public double getForceY(){
        return mass * accY;
    }

    public void update(double time){

        //  System.out.println("PREV POS p: "+id + ", X: "+posX + ", Y: "+posY + "radius: "+radius);
        posX += time * velX;
        posY += time * velY;
        // System.out.println("NUEVA POS p: "+id + ", X: "+posX + ", Y: "+posY+ "radius: "+radius);
    }

    public static double dist(Particle p1, Particle p2){
        return Math.sqrt(Math.pow(p1.posX - p2.posX,2) + Math.pow(p1.posY - p2.posY,2));
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

    public double getMass() {
        return mass;
    }

    public double getAccX() {
        return accX;
    }

    public double getAccY() {
        return accY;
    }

    public void setAccX(double accX) {
        this.accX = accX;
    }

    public void setAccY(double accY) {
        this.accY = accY;
    }

    @Override
    public String toString() {
        return "Particle{" +
            "id=" + id +
            ", posX=" + posX +
            ", posY=" + posY +
            ", velX=" + velX +
            ", velY=" + velY +
            ", accX=" + accX +
            ", accY=" + accY +
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


    @Override
    public Particle clone() {
        try {
            Particle clone = (Particle) super.clone();
            // No tiene objetos mutables dentro, por lo tanto no es necesario cambiar el metodo
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
