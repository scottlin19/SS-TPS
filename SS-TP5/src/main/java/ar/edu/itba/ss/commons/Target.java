package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class Target {
    private final Point2D.Double p1;
    private final Point2D.Double p2;


    public Target(Point2D.Double p1, Point2D.Double p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point2D.Double getP1() {
        return p1;
    }

    public Point2D.Double getP2() {
        return p2;
    }
}
