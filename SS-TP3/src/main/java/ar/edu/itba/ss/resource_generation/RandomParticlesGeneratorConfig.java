package ar.edu.itba.ss.resource_generation;

public class RandomParticlesGeneratorConfig {
    private final int N;
    private final double L;
    private final int maxIter;
    private final double maxVelocity;
    private final double smallParticleRadius;
    private final double bigMass;
    private final double smallMass;
    private final double bigParticleRadius;
    private final String outputFile;

    public RandomParticlesGeneratorConfig(int n, double l, int maxIter, double maxVelocity, double smallParticleRadius, double bigParticleRadius, double bigMass,double smallMass,String outputFile) {
        N = n;
        L = l;
        this.maxIter = maxIter;
        this.maxVelocity = maxVelocity;
        this.smallParticleRadius = smallParticleRadius;
        this.bigParticleRadius = bigParticleRadius;
        this.bigMass = bigMass;
        this.smallMass = smallMass;
        this.outputFile = outputFile;
    }

    public double getBigMass(){
        return bigMass;
    }

    public double getSmallMass(){
        return smallMass;
    }

    public int getN() {
        return N;
    }

    public double getL() {
        return L;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public double getSmallParticleRadius() {
        return smallParticleRadius;
    }

    public double getBigParticleRadius() {
        return bigParticleRadius;
    }

    public String getOutputFile() {
        return outputFile;
    }
}
