package ar.edu.itba.ss.commons;

import static ar.edu.itba.ss.commons.Wall.collidesWith;

import java.awt.geom.Point2D;
import java.util.ArrayList;
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
    private Target                      target;

    public Pedestrian(int id, Point2D.Double pos, double radius,double Ve,Target target) {
        this.id = id;
        this.pos = pos;
        this.radius = radius;
        this.Vd = new Point2D.Double(0, 0);
        this.Ve = Ve;
        this.target = target;
        this.desiredPosition = target.getClosestPoint(pos);
        this.collisions = new ArrayList<>();
    }

    public void update(double deltaT) {
        pos.setLocation(pos.x + deltaT * Vd.x, pos.y + deltaT * Vd.y);
        desiredPosition = target.getClosestPoint(pos);
    }

    public void addCollision(Point2D.Double pos) {
        collisions.add(pos);
    }

    public static void addNeighbour(Pedestrian p1, Pedestrian p2) {
        if(intersects(p1,p2)){
            //System.out.println("Pedestrians (" + p1.id + "," + p2.id + ") collide");
            p1.addCollision(p2.pos);
            p2.addCollision(p1.pos);
        }
    }

    public static void addWall(Pedestrian p, Wall w) {
        Point2D.Double pos = collidesWith(w,p);
        if(pos != null){
            p.addCollision(pos);
        }
    }

    public boolean hasCollisions(){
        return !collisions.isEmpty();
    }

    public void clearCollisions(){
        this.collisions.clear();
    }

    public void updateCollisions(double deltaT, double VdMax, double rMin, double rMax, int B, double tau) {
       // System.out.println("For pedestrian: " + id + " - Collisions are: " + collisions);
        Point2D.Double p;
        if(collisions.isEmpty()){

            p = getDesiredVelocity(VdMax, rMin, rMax, B);
           // System.out.println("DESIRED VEL: "+p);
            Vd.setLocation(p);
            incrementRadius(rMax / (tau/deltaT), rMax);
        }
        else {
            p = getEscapeVelocity(collisions);
            //System.out.println("ESCAPE VEL: "+p);
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

    // Vector
    public Point2D.Double getDesiredVelocity(double VdMax, double rMin, double rMax, int B) {
        double velMod = VdMax * Math.pow((radius - rMin) / (rMax - rMin), B);
        Point2D.Double desiredDirection = getDirectionVersor(pos,desiredPosition);
        desiredDirection.x *= velMod;
        desiredDirection.y *= velMod;
        return desiredDirection;
    }

    // Vector
    public Point2D.Double getEscapeVelocity(List<Point2D.Double> contacts) {
        List<Point2D.Double> contactDirs = contacts.stream().map(c-> getDirectionVersor(c,pos)).collect(Collectors.toList());
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

    public Point2D.Double getDirectionVersor(Point2D.Double  from,Point2D.Double other){
        double diffX = other.x - from.x;
        double diffY = other.y - from.y;
        double mod = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
        return new Point2D.Double(diffX/mod,diffY/mod);
    }


    public void updateTarget(Target target) {
        this.target = target;
        this.desiredPosition = target.getClosestPoint(pos);
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

    public Point2D.Double getVd() {
        return Vd;
    }

    public double getVe() {
        return Ve;
    }

    public Point2D.Double getDesiredPosition() {
        return desiredPosition;
    }

    public Target getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "Pedestrian{" +
            "id=" + id +
            ", pos=" + pos +
            ", Vd=" + Vd +
            ", Ve=" + Ve +
            ", desiredPosition=" + desiredPosition +
            ", radius=" + radius +
            ", collisions=" + collisions +
            ", target=" + target +
            '}';
    }
}
