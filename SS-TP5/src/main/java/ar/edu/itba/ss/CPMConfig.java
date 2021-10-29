package ar.edu.itba.ss;

public class CPMConfig {

    private double L;
    private double VdMAX;
    private double rMin, rMax;
    private double tau;
    private long N;
    private int B;
    private double entranceLength;

    public CPMConfig(double L, double vdMAX, double rMin, double rMax, double tau, long N,int B, double entranceLength) {
        this.L = L;
        this.VdMAX = vdMAX;
        this.rMin = rMin;
        this.rMax = rMax;
        this.tau = tau;
        this.N = N;
        this.B = B;
        this.entranceLength = entranceLength;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public double getVdMAX() {
        return VdMAX;
    }

    public void setVdMAX(double vdMAX) {
        VdMAX = vdMAX;
    }

    public double getrMin() {
        return rMin;
    }

    public void setrMin(double rMin) {
        this.rMin = rMin;
    }

    public double getrMax() {
        return rMax;
    }

    public void setrMax(double rMax) {
        this.rMax = rMax;
    }

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public long getN() {
        return N;
    }

    public void setN(long n) {
        N = n;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getEntranceLength() {
        return entranceLength;
    }

    public void setEntranceLength(double entranceLength) {
        this.entranceLength = entranceLength;
    }
}
