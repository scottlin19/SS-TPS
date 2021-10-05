package ar.edu.itba.ss.system1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.itba.ss.commons.writers.JSONWriter;
import com.google.gson.Gson;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

public class BenchmarkRunner {

    private static final String EJ1_2_RESULTS_DIR = "ej1_2/";
    private static final String EJ1_3RESULTS_DIR = "ej1_3/";
    public static void main(String[] args) {
        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            Config config = new Gson().fromJson(bufferedReader, Config.class);
            List<SimulationResult> results = ej1_2(config);
            JSONWriter<SimulationResult> ej1_2Writer = new JSONWriter<>();
            results.forEach(res -> ej1_2Writer.createFile(res, EJ1_2_RESULTS_DIR + "/simulation_" + res.getMethod()));

            Map<String,DCMResult> dcmResults = ej1_3(config);
            JSONWriter<DCMResult> ej1_3Writer = new JSONWriter<>();
            for(Map.Entry<String,DCMResult> entry: dcmResults.entrySet()){
                ej1_3Writer.createFile(entry.getValue(),"results/" + EJ1_3RESULTS_DIR + "/dcms_"+ entry.getKey());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static List<SimulationResult> ej1_2(Config config){
        List<SimulationResult> results = new ArrayList<>();
        List<String> strategies = new ArrayList<>(){{
            add("analytic");
            add("verlet");
            add("gear");
            add("beeman");
        }};
        strategies.forEach(strategy -> {
            DampedOscillator oscillator = new DampedOscillator(config,strategy);
            SimulationResult result  = oscillator.simulate(config.getDeltaT());
            results.add(result);
        });
        return results;
    }

    public static Map<String,DCMResult> ej1_3(Config config){
        List<String> strategies = new ArrayList<>(){{
            add("analytic");
            add("verlet");
            add("gear");
            add("beeman");
        }};
        Map<String,DCMResult> dcmResults = new HashMap<>();
        for (double deltaT=0.01 ; deltaT > 0.0001 ; deltaT-=0.0001 ){
            System.out.println("Delta: "+deltaT);
            List<SimulationResult> results = new ArrayList<>();

            for (String strategy : strategies) {
                DampedOscillator oscillator = new DampedOscillator(config, strategy);
                SimulationResult result = oscillator.simulate(deltaT);
                results.add(result);
            }

            SimulationResult analytic = results.get(0);
            for(int j = 1; j<  results.size();j++){
                SimulationResult result = results.get(j);
                dcmResults.computeIfAbsent(result.getMethod(), k -> new DCMResult(result.getMethod()));
                dcmResults.get(result.getMethod()).getDCMs().add(new DCMResult.DCM(deltaT,getDCM(analytic,result)));
            }
        }

        return dcmResults;
    }

    private static double getDCM(SimulationResult analytic, SimulationResult other){

        List<SimulationSnapshot> analyticSS = analytic.getSnapshots();
        List<SimulationSnapshot> otherSS = other.getSnapshots();
        long steps = analyticSS.size();
        double dcm = 0;
        for(int i = 0; i<  steps; i++) {
            dcm += Math.pow(analyticSS.get(i).getParticles().get(0).getPosX() - otherSS.get(i).getParticles().get(0).getPosX(), 2);
        }
        return  dcm/steps;
    }

    static class DCMResult{
        private final String method;
        private final List<DCM> DCMs;


        public DCMResult(String method){
            this.method = method;

            this.DCMs = new ArrayList<>();
        }

       public String getMethod(){
            return method;
       }

        public List<DCM> getDCMs(){
            return DCMs;
        }

        public static class DCM{
            private final double deltaT;
            private final double DCM;

            public DCM(double deltaT, double DCM){
                this.deltaT = deltaT;
                this.DCM = DCM;
            }

            public double getDeltaT(){

                return deltaT;
            }

            public double getDCM(){
                return DCM;
            }
        }

    }
}
