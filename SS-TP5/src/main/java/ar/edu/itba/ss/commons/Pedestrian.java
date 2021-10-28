package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

public class Pedestrian extends Particle {

    private Point2D.Double Vd;
    private double Ve;
    private Particle desiredPosition;

    public Pedestrian(int id, Point2D.Double pos, double radius) {
        super(id, pos, radius);
        this.Vd = new Point2D.Double(0, 0);
        this.Ve = Ve;
    }




    private boolean intersects(Particle p1, Particle p2) {

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

    public Point2D.Double getEscapeVelocity(List<Particle> contacts) {
        List<Point2D.Double> contactDirs = contacts.stream().map(this::getDirectionVersor).collect(Collectors.toList());
        double sumX = contactDirs.stream().mapToDouble(Point2D.Double::getX).sum();
        double sumY = contactDirs.stream().mapToDouble(Point2D.Double::getY).sum();
        double mod = Math.sqrt(Math.pow(sumX, 2) + Math.pow(sumY, 2));
        return new Point2D.Double(Ve * sumX / mod, Ve * sumY / mod);
    }


    public void incrementRadius(double dR){
        radius += dR;
    }

    public Point2D.Double getDirectionVersor(Particle p){
        double diffX = p.getPosX() - pos.x;
        double diffY = p.getPosY() - pos.y;
        double mod = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
        return new Point2D.Double(diffX/mod,diffY/mod);
    }
}
