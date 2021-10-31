package ar.edu.itba.ss;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CPMConfig {

    @JsonProperty("L")
    private double L;

    @JsonProperty("VdMax")
    private double VdMax;

    @JsonProperty("Ve")
    private double Ve;

    @JsonProperty("rMin")
    private double rMin;

    @JsonProperty("rMax")
    private double rMax;

    @JsonProperty("tau")
    private double tau;

    @JsonProperty("N")
    private long N;

    @JsonProperty("B")
    private double B;

    @JsonProperty("entranceLength")
    private double entranceLength;

    @JsonProperty("K")
    private int K;

    @JsonProperty("maxTime")
    private double maxTime;

    public CPMConfig(){

    }

    public CPMConfig(double VdMax,double L, double Ve, double rMin, double rMax, double tau , long N, double B, double entranceLength,int K, double maxTime) {
        this.VdMax = VdMax;
        this.L = L;
        this.Ve = Ve;
        this.rMin = rMin;
        this.rMax = rMax;
        this.tau = tau;
        this.N = N;
        this.B = B;
        this.entranceLength = entranceLength;
        this.K = K;
        this.maxTime = maxTime;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getVdMax() {
        return VdMax;
    }

    public void setVdMax(double VdMax) {
        this.VdMax = VdMax;
    }

    public double getVe() {
        return Ve;
    }

    public void setVe(double ve) {
        Ve = ve;
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

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getEntranceLength() {
        return entranceLength;
    }

    public void setEntranceLength(double entranceLength) {
        this.entranceLength = entranceLength;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public double getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }
}
