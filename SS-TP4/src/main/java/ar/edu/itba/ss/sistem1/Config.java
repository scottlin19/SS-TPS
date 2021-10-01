package ar.edu.itba.ss.sistem1;

import ar.edu.itba.ss.commons.strategies.AnalyticStrategy;
import ar.edu.itba.ss.commons.strategies.EulerStrategy;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy;

public class Config {

    private final double mass;
    private final int k;
    private final double gamma;
    private final int tf;
    private final double r0;
    private final String strategy;
    private final int step;
    private final double deltaT;

    public Config(double mass, int k, double gamma, int tf, double r0,double deltaT,String strategy, int step) {
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.tf = tf;
        this.r0 = r0;
        this.deltaT = deltaT;
        this.strategy = strategy;
        this.step = step;
    }

    public String getStrategy(){
        return strategy;
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

    public double getDeltaT(){
        return deltaT;
    }
    public int getStep() {
        return step;
    }
}
