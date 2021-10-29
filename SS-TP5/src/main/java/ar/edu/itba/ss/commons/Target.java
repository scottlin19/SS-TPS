package ar.edu.itba.ss.commons;

import static ar.edu.itba.ss.commons.Wall.collidesWith;

import java.awt.geom.Point2D;
import java.util.Objects;
import java.util.Random;

public class Target extends Wall{

    private double exitWidth;
    private int id;

    public Target(Point2D.Double p1, Point2D.Double p2, int id) {
        super(p1, p2, Orientation.HORIZONTAL);
        this.exitWidth = p2.x - p1.x;
        this.id = id;
    }

    public Point2D.Double getClosestPoint(Point2D.Double pos){
        double pX = pos.x;

        double from = p1.x + 0.2*exitWidth;
        double to = p1.x + 0.8*exitWidth;
        if(pX < from || pX > to){
            Random r = new Random();
            double x = r.nextDouble()*(to-from) + from;
//            System.out.println("CLOSETS POINT: "+new Point2D.Double(x,p1.y));
            return new Point2D.Double(x,p1.y);
        }else{
//            System.out.println("CLOSETS POINT:"+ new Point2D.Double(pX,p1.y));
            return new Point2D.Double(pX,p1.y);
        }
    }

    public boolean checkCollision(Pedestrian p){
        return collidesWith(this, p) != null;
    }

    public double getExitWidth() {
        return exitWidth;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return id == target.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
