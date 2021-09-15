package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.Particle;

public class BigParticleCutCondition extends CutCondition{

    private final Particle bigParticle;

    public BigParticleCutCondition(Particle bigParticle){
        super();
        this.bigParticle = bigParticle;
    }

    @Override
    public boolean cut(Event event) {
        if(event == null){
            return false;
        }
        return event.isWallCollision() && event.containsParticle(bigParticle);
    }
}
