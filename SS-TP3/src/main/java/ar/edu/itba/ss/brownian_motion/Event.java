package ar.edu.itba.ss.brownian_motion;

import java.util.Objects;

public abstract class Event implements Comparable<Event>{

    private final double time;

    public Event(double time){
        this.time = time;
    }
    public abstract void update();

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
}
