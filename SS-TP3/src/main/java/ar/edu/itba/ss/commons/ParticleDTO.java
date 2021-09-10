package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.grid.Particle;

public class ParticleDTO {
    private double posX;
    private double posY;
    private double velX;
    private double velY;

    public ParticleDTO(double posX, double posY, double velX, double velY) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
    }

    public static ParticleDTO fromParticle(Particle particle){
        return new ParticleDTO(particle.getPosX(),particle.getPosY(), particle.getVelX(), particle.getVelY() );
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


    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }


    @Override
    public String toString() {
        return "ParticleDTO{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY + "}";
    }
}
