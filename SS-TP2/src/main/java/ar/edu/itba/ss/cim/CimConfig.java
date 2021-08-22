package ar.edu.itba.ss.cim;

public class CimConfig {
    private final String to_run;
    private final String static_file;
    private final String dynamic_file;
    private final int benchmark_iterations;

    CimConfig(String static_file, String dynamic_file, String to_run, int benchmark_iterations){
        this.static_file = static_file;
        this.dynamic_file = dynamic_file;
        this.to_run = to_run;
        this.benchmark_iterations = benchmark_iterations;
    }

    public String getTo_run() {
        return to_run;
    }

    public String getStatic_file() {
        return static_file;
    }

    public String getDynamic_file() {
        return dynamic_file;
    }

    public int getBenchmark_iterations() {
        return benchmark_iterations;
    }
}
