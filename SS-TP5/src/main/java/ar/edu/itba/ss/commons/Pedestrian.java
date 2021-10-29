package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Pedestrian {

    private final int                   id;
    private Point2D.Double              pos;
    private Point2D.Double              Vd;
    private double                      Ve;
    private Point2D.Double              desiredPosition;
    private double                      radius;
    private List<Point2D.Double>        collisions;

    public Pedestrian(int id, Point2D.Double pos, double radius,double Ve) {

        this.id = id;
        this.pos = pos;
        this.radius = radius;
        this.Vd = new Point2D.Double(0, 0);
        this.Ve = Ve;
    }

    public void update(double deltaT) {
        pos.setLocation(pos.x + deltaT * Vd.x, pos.y + deltaT * Vd.y);
    }

    public void addCollision(Pedestrian p) {
        collisions.add(p.pos);
    }

    public static void addNeighbour(Pedestrian p1, Pedestrian p2) {
        if(intersects(p1,p2)){
            p1.addCollision(p2);
            p2.addCollision(p1);
        }
    }

    public void updateCollisions(double deltaT, double VdMax, double rMin, double rMax, int B, double tau) {
        Point2D.Double p = null;
        if(collisions.isEmpty()){
            p = getDesiredVelocity(VdMax, rMin, rMax, B);
            Vd.setLocation(p);
            incrementRadius(rMax / (tau/deltaT), rMax);
        }
        else {
            // TODO: check walls collisions
            p = getEscapeVelocity(collisions);
            Vd.setLocation(p);
            radius = rMin;
        }

        // Limpio lista para la proxima iteracion
        collisions.clear();
    }

    private static boolean intersects(Pedestrian p1, Pedestrian p2) {

        double dist = Math.sqrt(Math.pow((p1.getPosX() - p2.getPosX()), 2) + Math.pow((p1.getPosY() - p2.getPosY()), 2));
        return dist <= (p1.getRadius() + p2.getRadius());
    }

    public Point2D.Double getDesiredVelocity(double VdMax, double rMin, double rMax, int B) {
        double velMod = VdMax * Math.pow((radius - rMin) / (rMax - rMin), B);
        Point2D.Double desiredDirection = getDirectionVersor(desiredPosition);
        desiredDirection.x *= velMod;
        desiredDirection.y *= velMod;
        return desiredDirection;
    }

    public Point2D.Double getEscapeVelocity(List<Point2D.Double> contacts) {
        List<Point2D.Double> contactDirs = contacts.stream().map(this::getDirectionVersor).collect(Collectors.toList());
        double sumX = contactDirs.stream().mapToDouble(Point2D.Double::getX).sum();
        double sumY = contactDirs.stream().mapToDouble(Point2D.Double::getY).sum();
        double mod = Math.sqrt(Math.pow(sumX, 2) + Math.pow(sumY, 2));
        return new Point2D.Double(Ve * sumX / mod, Ve * sumY / mod);
    }


    public void incrementRadius(double dR, double rMax){

        double nextRadius = radius +dR;
        if(nextRadius > rMax){
            radius = rMax;
        }else{
            radius = nextRadius;
        }
    }

    public Point2D.Double getDirectionVersor(Point2D.Double other){
        double diffX = other.x - pos.x;
        double diffY = other.y - pos.y;
        double mod = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
        return new Point2D.Double(diffX/mod,diffY/mod);
    }

    /* ---------------- GETTERS, SETTERS, EQUALS, HASH, TOSTRING ---------------- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedestrian that = (Pedestrian) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public Point2D.Double getPos(){
        return pos;
    }

    public double getPosX() {
        return pos.x;
    }

    public double getPosY() {
        return pos.y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public List<Point2D.Double> getCollisions() {
        return collisions;
    }
}
