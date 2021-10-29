package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class Wall {
    private final Point2D.Double        p1;
    private final Point2D.Double        p2;
    private final Orientation           orientation;

    public Wall(Point2D.Double p1, Point2D.Double p2,Orientation orientation) {
        this.p1 = p1;
        this.p2 = p2;
        this.orientation = orientation;
    }

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public Point2D.Double collidesWith(Pedestrian p){
        double pX = p.getPosX();
        double pY = p.getPosY();
        if(orientation == Orientation.VERTICAL){

            if(pY >= p1.y && pY <= p2.y){
                if(Math.abs(pX - p1.x) <= p.getRadius()){
                    //Collision
                    return new Point2D.Double(p1.x,pY);
                }else{
                    return null;
                }

            }else{
                return null;
            }
        }else{
            if(pX >= p1.x && pX <= p2.x){
                if(Math.abs(pY - p1.y) <= p.getRadius()){
                    //Collision
                    return new Point2D.Double(pX,p1.y);
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
