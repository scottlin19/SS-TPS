package ar.edu.itba.ss.cim;

import ar.edu.itba.ss.brownian_motion.WallCollisionEvent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Particle{

    private final int id;
    private double posX, posY;
    private double velX,velY;
    private double radius;
    private final double mass;


    public Particle(int id,double posX,double posY,double radius, double mass, double velX, double velY){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;

    }

    public static void updateParticleCollision(Particle p1, Particle p2){
        double dX = p1.getPosX() - p2.getPosX();
        double dY = p1.getPosY() - p2.getPosY();
        Point2D.Double dR = new Point2D.Double(dX,dY);
        Point2D.Double dV= new Point2D.Double(p1.getVelX()- p2.getVelX(),p1.getPosX() - p2.getVelX());
        double theta = p1.getRadius() + p2.getRadius();
        double J = 2*p1.getMass()*p2.getMass()*(dV.x*dR.x + dV.y*dR.y)/theta*(p1.getMass()+p2.getMass());
        Point2D.Double Jv = new Point2D.Double(J*dX/theta,J*dY/theta);

        p1.setVelX(p1.getVelX() + Jv.x/p1.getMass());
        p1.setVelY(p1.getVelY() + Jv.y/p1.getMass());

        p2.setVelX(p2.getVelX() - Jv.x/p2.getMass());
        p2.setVelY(p2.getVelY() - Jv.y/p2.getMass());

    }

    public static void updateWallCollision(Particle p, WallCollisionEvent.Direction direction) {
        if (direction == WallCollisionEvent.Direction.X){
            p.setVelX(p.getVelX() * -1);
        }else{
            p.setVelY(p.getVelY() * -1);
        }
    }

    public double getCollisionTime(Particle p){
        Point2D.Double dR = new Point2D.Double(posX - p.getPosX(),posY - p.getPosY());
        Point2D.Double dV= new Point2D.Double(velX- p.getVelX(),velY - p.getVelX());
        double VR = dV.x * dR.x + dV.y * dR.y;
        if(VR >= 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
        double VV = dV.x * dV.x + dV.y * dV.y;
        double RR = dR.x * dR.x + dR.y * dV.y;
        double theta = radius + p.getRadius();
        double d = Math.pow(VR,2) - VV*(RR - Math.pow(theta,2));

        if(d < 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
        return - (VR + Math.sqrt(d))/VV;
    }

    public void update(double time){
        posX += time * velX;
        posY += time * velY;
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
        StringBuilder sb = new StringBuilder("Particle: " + id + "{" +
                "posX=" + posX +
                ", posY=" + posY+", radius= "+radius+", neighbours= [");

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

