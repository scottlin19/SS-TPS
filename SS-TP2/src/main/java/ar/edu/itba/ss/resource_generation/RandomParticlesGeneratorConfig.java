package ar.edu.itba.ss.resource_generation;

import java.util.ArrayList;
import java.util.List;

public class RandomParticlesGeneratorConfig {
    private int N;
    private int L;
    private double RC;
    private boolean hasWalls;
    private double ETA;
    private int maxIter;
    private double initialVelocity;
    private double particleRadius;


    public RandomParticlesGeneratorConfig(int N, int L, double RC, boolean hasWalls, double ETA, int maxIter, double initialVelocity, double particleRadius){
        this.N = N;
        this.L = L;
        this.RC = RC;
        this.hasWalls = hasWalls;
        this.ETA = ETA;
        this.maxIter = maxIter;
        this.initialVelocity = initialVelocity;
        this.particleRadius = particleRadius;
    }

    public int getL() {
        return L;
    }

    public double getRC() {
        return RC;
    }

    public boolean getHasWalls() {
        return hasWalls;
    }

    public double getParticle_radius() {
        return particleRadius;
    }

    public int getN() {
        return N;
    }

    public double getETA() {
        return ETA;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }
}
