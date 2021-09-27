package ar.edu.itba.ss.sistem1;

public class Config {

    private final double mass;
    private final int k;
    private final double gamma;
    private final int tf;
    private final double r0;
    private final double v0;
    private final double deltaT;

    public Config(double mass, int k, double gamma, int tf, double r0, double v0, double deltaT) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.tf = tf;
        this.r0 = r0;
        this.v0 = v0;
        this.deltaT = deltaT;
    }

    public double getMass() {
        return mass;
    }

    public int getK() {
        return k;
    }

    public double getGamma() {
        return gamma;
    }

    public int getTf() {
        return tf;
    }

    public double getR0() {
        return r0;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getV0() {
        return v0;
    }
}
