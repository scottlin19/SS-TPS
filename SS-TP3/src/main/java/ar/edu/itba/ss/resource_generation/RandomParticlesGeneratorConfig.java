package ar.edu.itba.ss.resource_generation;

public class RandomParticlesGeneratorConfig {
    private final int N;
    private final int L;
    private final double RC;
    private final double ETA;
    private final int maxIter;
    private final double initialVelocity;
    private final double particleRadius;
    private final String outputFile;


    public RandomParticlesGeneratorConfig(int N, int L, double RC, double ETA, int maxIter, double initialVelocity, double particleRadius, String outputFile){
        this.N = N;
        this.L = L;
        this.RC = RC;
        this.ETA = ETA;
        this.maxIter = maxIter;
        this.initialVelocity = initialVelocity;
        this.particleRadius = particleRadius;
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public int getL() {
        return L;
    }

    public double getRC() {
        return RC;
    }

    public double getParticleRadius() {
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
