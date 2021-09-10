package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.grid.Particle;

import java.util.Objects;

public class Event implements Comparable<Event>{

    private double time;
    private final Particle p1;
    private final Particle p2;
    private final Direction direction;

    public Event(double time, Particle p1, Particle p2){
        this.time = time;
        this.p1 = p1;
        this.p2 = p2;
        this.direction = null;
    }

    public Event(double time, Particle p1, Direction direction){
        this.time = time;
        this.p1 = p1;
        this.p2 = null;
        this.direction = direction;
    }

    public void update(){
        if(isWallCollision()){ // Is wall collision
            Particle.updateWallCollision(p1,direction);
        }else{
            Particle.updateParticleCollision(p1,p2);
        }
    }

    public void updateTime(double time){
        this.time -= time;
    }

    public boolean containsParticle(Particle p){
        return p2 != null ? (p1.equals(p) || p2.equals(p)) : p1.equals(p);
    }

    public boolean isWallCollision(){
        return p2 == null;
    }

    public boolean isParticleCollision(){
        return p2 != null && direction == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.time, time) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event other){
        return Double.compare(this.time, other.time);
    }

    public Particle getP1() {
        return p1;
    }

    public Particle getP2() {
        return p2;
    }

    public enum Direction{
        X,
        Y
    }
}
