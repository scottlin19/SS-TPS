package ar.edu.itba.ss.grid;

public class CimConfig {
    private final String toRun;
    private final String staticFile;
    private final String dynamicFile;
    private final int benchmarkIterations;
    private final String outputFile;

    CimConfig(String staticFile, String dynamicFile, String toRun, String outputFile,int benchmarkIterations){
        this.staticFile = staticFile;
        this.dynamicFile = dynamicFile;
        this.toRun = toRun;
        this.outputFile = outputFile;
        this.benchmarkIterations = benchmarkIterations;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getToRun() {
        return toRun;
    }

    public String getStaticFile() {
        return staticFile;
    }

    public String getDynamicFile() {
        return dynamicFile;
    }

    public int getBenchmarkIterations() {
        return benchmarkIterations;
    }
}
