package ar.edu.itba.ss.commons;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Particle {

    protected final int id;
    protected Point2D.Double pos;
    protected double radius;

    public Particle(int id, Point2D.Double pos, double radius) {
        this.id = id;
        this.pos = pos;
        this.radius = radius;
    }


    /* ---------------- GETTERS, SETTERS, EQUALS, HASH, TOSTRING ---------------- */




    public int getId() {
        return id;
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

