package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.Particle;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Event implements Comparable<Event>{

    private double time;
    private double relativeTime;
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

    public void setTime(double time) {
        this.time = time;
    }

    public double getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(double relativeTime) {
        this.relativeTime = relativeTime;
    }

    public void update(){
        if(isWallCollision()){ // Is wall collision
           // System.out.println("es wall collision");
            Particle.updateWallCollision(p1,direction);
        }else{
            //System.out.println("es particle collision");
            Particle.updateParticleCollision(p1,p2);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public List<Particle> getParticles(){
        return p2 == null ? List.of(p1) : Arrays.asList(p1,p2);
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

    public Particle getCollisionedWithParticle(Particle p){
        return p.equals(p1) ? p2 : (p.equals(p2) ? p1 : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;

        // if events are of different type
        if((p2 == null && event.getP2() != null) || (p2 != null && event.getP2() != null)){
            return false;
        }
        boolean ret = Double.compare(event.time, time) == 0;

        if(isWallCollision()){
            ret = ret && p1.equals(event.p1) && direction.equals(event.direction);
        }
        else{
            ret = ret &&  (p1.equals(event.p1) && p2.equals(event.p2)) || (p1.equals(event.p2) && p2.equals(event.p1));
        }

        return ret;
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

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", direction=" + direction +
                '}';
    }
}
