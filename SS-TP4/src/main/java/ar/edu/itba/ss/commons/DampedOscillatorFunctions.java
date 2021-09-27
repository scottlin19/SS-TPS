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
    public double f(Particle p, double t) {
        return -k * r(p,t) - gamma * r1(p,t);
    }

    @Override
    public double r(Particle p, double t) {
        return amplitude * Math.exp(-(gamma/(2*p.getMass()))*t) * Math.cos(Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * t);
    }

    @Override
    public double r1(Particle p, double t) {
        return amplitude*(-(gamma/(2*p.getMass())) * Math.exp(-(gamma/(2*p.getMass()))*t) - Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * Math.exp(-(gamma/(2*p.getMass()))*t) * Math.sin(Math.pow(((k/p.getMass())-(Math.pow(gamma, 2) / (4* Math.pow(p.getMass(), 2)))),0.5) * t));
    }

    @Override
    public double r2(Particle p, double t) {
        return -(k/p.getMass()) * r(p,t); // r0 = 0
    }

    @Override
    public double r3(Particle p, double t) {
        return -(k/p.getMass()) * r1(p,t);
    }

    @Override
    public double r4(Particle p, double t) {
        return -(k/p.getMass()) * r2(p,t);
    }

    @Override
    public double r5(Particle p, double t) {
        return -(k/p.getMass()) * r3(p,t);
    }
}
