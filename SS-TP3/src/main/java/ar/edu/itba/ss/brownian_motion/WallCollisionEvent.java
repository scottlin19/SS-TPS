package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.cim.Particle;

import java.util.Objects;

public class WallCollisionEvent extends Event{

    private final Particle particle;
    private final Direction direction;

    public WallCollisionEvent(double time, Particle particle, Direction direction) {
        super(time);
        this.particle = particle;
        this.direction = direction;
    }

    @Override
    public void update() {
        Particle.updateWallCollision(particle,direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WallCollisionEvent that = (WallCollisionEvent) o;
        return Objects.equals(particle, that.particle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), particle);
    }

    public enum Direction{
        X,
        Y
    }
}
