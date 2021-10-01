package ar.edu.itba.ss.commons;

public class DampedOscillatorFunctions extends SystemFunctions {

    private final double amplitude;
    private final int k;
    private final double gamma;

    public DampedOscillatorFunctions(double amplitude, int k, double gamma) {
        this.amplitude = amplitude;
        this.k = k;
        this.gamma = gamma;
    }

    @Override
    public double fx(Particle p, double t) {
        return -k * rx(p,t) - gamma * r1x(p,t);
    }

    @Override
    public double fy(Particle p, double t) {
        return 0;
    }

    @Override
    public double rx(Particle p, double t) {
        return amplitude * Math.exp(-(gamma/(2*p.getMass()))*t) * Math.cos(Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * t);
    }

    @Override
    public double ry(Particle p, double t) {
        return 0;
    }

    @Override
    public double r1x(Particle p, double t) {
        return amplitude*(-(gamma/(2*p.getMass())) * Math.exp(-(gamma/(2*p.getMass()))*t) - Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * Math.exp(-(gamma/(2*p.getMass()))*t) * Math.sin(Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * t));
    }

    @Override
    public double r1y(Particle p, double t) {
        return 0;
    }

    @Override
    public double r2x(Particle p, double t) {
        return -(k/p.getMass()) * rx(p,t); // r0 = 0
    }

    @Override
    public double r2y(Particle p, double t) {
        return 0;
    }

    @Override
    public double r3x(Particle p, double t) {
        return -(k/p.getMass()) * r1x(p,t);
    }

    @Override
    public double r3y(Particle p, double t) {
        return 0;
    }

    @Override
    public double r4x(Particle p, double t) {
        return -(k/p.getMass()) * r2x(p,t);
    }

    @Override
    public double r4y(Particle p, double t) {
        return 0;
    }

    @Override
    public double r5x(Particle p, double t) {
        return -(k/p.getMass()) * r3x(p,t);
    }

    @Override
    public double r5y(Particle p, double t) {
        return 0;
    }
}
