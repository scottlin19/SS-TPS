package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.writers.JSONWriter;

public class BenchmarkRunner {

    private static final String RESULTS_DIRECTORY       = "results/";
    private static final String EJ1_DIRECTORY           = "ej1/";
    private static final String EJ2_DIRECTORY           = "ej2/";

    private static final String EJ3_DIRECTORY           = "ej3/";

    private static final CPMConfig PAPER_CONFIG_1 = new CPMConfig(
        1.55,
        20,
        1.55,
        0.15,
        0.32,
        0.5,
        200,
        0.9,
        1.2,
        1,
        3600
    );

    private static final CPMConfig PAPER_CONFIG_2 = new CPMConfig(
        0.95,
        20,
        0.95,
        0.10,
        0.37,
        0.5,
        200,
        0.9,
        1.2,
        1,
        3600
    );

    private static final CPMConfig CUSTOM_CONFIG = new CPMConfig(
        2,
        20,
        2,
        0.15,
        0.32,
        0.5,
        200,
        0.9,
        1.2,
        1,
        3600
    );

    public static List<List<ExitedAndTime>> ej1(final int iterations, boolean filter){
        final List<List<ExitedAndTime>> srs = new ArrayList<>();
        CPMConfig config = CUSTOM_CONFIG;
        double deltaT = config.getrMin()/(2*Math.max(config.getVdMax(),config.getVe()));
        CPM cpm;
        int step = 1;
        for(int i = 0 ; i < iterations ; i++){
            System.out.println("Iteration " + i);
            cpm = new CPM(config);
            SimulationResult sr = cpm.simulate(deltaT,step);
            int acum = 0;
            List<ExitedAndTime> iteration = new ArrayList<>();
            for(SimulationSnapshot snapshot : sr.getSnapshots()){
                if(!filter || !snapshot.getExited().isEmpty()) {
                    acum += snapshot.getExited().size();
                    iteration.add(new ExitedAndTime(
                        acum,
                        snapshot.getExited().size(),
                        snapshot.getTime()
                    ));
                }
            }
            System.out.println("exited amount: " + acum);
            srs.add(iteration);
        }
        return srs;


    }

    public static class ExitedAndTime{
        private final int acumExited;
        private final int exited;
        private final double time;

        public ExitedAndTime(int acumExited, int exited, double time){
            this.acumExited = acumExited;
            this.exited = exited;
            this.time = time;
        }

        public int getExited() {
            return exited;
        }

        public int getAcumExited() {
            return acumExited;
        }

        public double getTime() {
            return time;
        }
    }



    public static List<Ej3Result> ej3(final int iterations){
        final List<Ej3Result> ej3Results = new ArrayList<>();
        double VdMax = 2.0;
        int L = 20;
        double Ve = 2.0;
        double rMin = 0.15;
        double rMax = 0.32;
        double tau = 0.5;
        List<Long> Ns = List.of(200L,260L,320L,380L);
        double B = 0.8;
        List<Double> Ds = List.of(1.2,1.8,2.4,3.0);
        int step = 1;
        for(int i = 0; i < Ns.size();i++){
            double D = Ds.get(i);
            long N = Ns.get(i);
            CPMConfig config = new CPMConfig(VdMax,L,Ve,rMin,rMax,tau,N,B,D,step, 60);
            double deltaT = config.getrMin()/(2*Math.max(config.getVdMax(),config.getVe()));
            CPM cpm;
            List<SimulationResult> results = new ArrayList<>();
            for(int j = 0 ; j < iterations ; j++){
                System.out.println("Iteration " + i);
                cpm = new CPM(config);
                SimulationResult sr = cpm.simulate(deltaT,step);
                results.add(sr);
            }
            ej3Results.add(new Ej3Result(D,N,results));
        }
        return ej3Results;
    }


    private static class Ej3Result{

        private final double d;
        private final long N;
        private final List<SimulationResult> results;

        public Ej3Result(double d, long n, List<SimulationResult> results) {
            this.d = d;
            this.N = n;
            this.results = results;
        }

        public double getD() {
            return d;
        }

        public long getN() {
            return N;
        }

        public List<SimulationResult> getResults() {
            return results;
        }
    }




    public static void main(String[] args) {
        //Ej 1/2
        List<List<ExitedAndTime>> res = ej1(20, true);
        JSONWriter<List<List<ExitedAndTime>>> jsonWriter = new JSONWriter<>();
        jsonWriter.createFile(res, RESULTS_DIRECTORY + EJ1_DIRECTORY + "results");

        //Ej 3
//        List<Ej3Result> ej3res = ej3(3);
//        JSONWriter<List<Ej3Result>> ej3JsonWriter = new JSONWriter<>();
//        ej3JsonWriter.createFile(ej3res, RESULTS_DIRECTORY + EJ3_DIRECTORY + "results");

   }
}
