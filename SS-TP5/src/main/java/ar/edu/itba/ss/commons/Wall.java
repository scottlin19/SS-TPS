package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class Wall {
    protected final Point2D.Double        p1;
    protected final Point2D.Double        p2;
    protected final Orientation           orientation;

    public Wall(Point2D.Double p1, Point2D.Double p2,Orientation orientation) {
        this.p1 = p1;
        this.p2 = p2;
        this.orientation = orientation;
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public static Point2D.Double collidesWith(Wall w, Pedestrian p){
        double pX = p.getPosX();
        double pY = p.getPosY();
        if(w.orientation == Orientation.VERTICAL){

            if(pY >= w.p1.y && pY <= w.p2.y){
                if(Math.abs(pX - w.p1.x) <= p.getRadius()){
                    //Collision
                    return new Point2D.Double(w.p1.x,pY);
                }else{
                    return null;
                }

            }else{
                return null;
            }
        }else{
            if(pX >= w.p1.x && pX <= w.p2.x){
                if(Math.abs(pY - w.p1.y) <= p.getRadius()){
                    //Collision
                    return new Point2D.Double(pX,w.p1.y);
                }else{
                    return null;
                }

            }else{
                return null;
            }
        }
    }

    public Point2D.Double getP1() {
        return p1;
    }

    public Point2D.Double getP2() {
        return p2;
    }




}
