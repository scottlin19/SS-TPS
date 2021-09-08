package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.cim.Particle;

import java.util.Objects;

public class ParticleCollisionEvent extends Event{

    private final Particle p1;
    private final Particle p2;

    public ParticleCollisionEvent(double time, Particle p1, Particle p2){
        super(time);
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void update(){
        Particle.updateParticleCollision(p1,p2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleCollisionEvent that = (ParticleCollisionEvent) o;
        return Objects.equals(this.getTime(), that.getTime()) && ((p1.equals(that.p1) && p2.equals(that.p2)) || (p1.equals(that.p2) && p2.equals(that.p1)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2);
    }
}
