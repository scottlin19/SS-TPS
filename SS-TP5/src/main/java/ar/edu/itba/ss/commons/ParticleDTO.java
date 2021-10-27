package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.commons.Particle;

public class ParticleDTO {
    private double posX;
    private double posY;
    private double direction;

    public ParticleDTO(double posX, double posY, double direction) {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
    }

    public static ParticleDTO fromParticle(Particle particle){
        return new ParticleDTO(particle.getPosX(),particle.getPosY(), particle.getDirection());
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "ParticleDTO{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", direction=" + direction +
                '}';
    }
}
