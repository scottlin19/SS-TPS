package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;

public class PedestrianDTO {
    private int id;
    private double x;
    private double y;
    private double radius;

    public PedestrianDTO(){

    }

    public static PedestrianDTO fromPedestrian(Pedestrian pedestrian){
        PedestrianDTO dto = new PedestrianDTO();
        dto.id = pedestrian.getId();
        dto.x = pedestrian.getPos().getX();
        dto.y = pedestrian.getPos().getY();
        dto.radius = pedestrian.getRadius();
        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
