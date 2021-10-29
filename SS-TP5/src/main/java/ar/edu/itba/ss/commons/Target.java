package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;
import java.util.Random;

public class Target extends Wall{

    private double exitWidth;

    public Target(Point2D.Double p1, Point2D.Double p2) {
        super(p1, p2, Orientation.HORIZONTAL);
        this.exitWidth = p2.x - p1.x;
    }

    public Point2D.Double getClosestPoint(Pedestrian pedestrian){
        double pX = pedestrian.getPosX();

        double from = p1.x + 0.2*exitWidth;
        double to = p1.x + 0.8*exitWidth;
        if(pX < from || pX > to){
            Random r = new Random();
            double x = r.nextDouble()*(to-from) + from;
            return new Point2D.Double(x,p1.y);
        }else{
            return new Point2D.Double(pX,p1.y);
        }
    }
}
