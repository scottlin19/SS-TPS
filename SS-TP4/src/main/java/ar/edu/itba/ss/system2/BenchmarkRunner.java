package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.writers.JSONWriter;
import ar.edu.itba.ss.system1.FirstSystemRunner;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class BenchmarkRunner {

    private static final String RESULTS_DIRECTORY = "results/";
    private static final String EJ2_1_RESULTS_DIR = "ej2_1/";
    private static final String EJ2_2RESULTS_DIR = "ej2_2/";
    public static void main(String[] args) {
        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/mars_mission_config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            MarsMissionConfig config = new Gson().fromJson(bufferedReader, MarsMissionConfig.class);
            List<MarsMissionDistance> results = ej2_1(config);
            JSONWriter<List<MarsMissionDistance>> jsonWriter =new JSONWriter<>();
            MarsMissionDistance min = results.stream().min(Comparator.comparingInt(o -> (int) o.marsDistance)).get();
            System.out.printf("Min mars distance: %f with takeOff time = %f",min.getMarsDistance(),min.getTakeOffTime());

            jsonWriter.createFile(results, RESULTS_DIRECTORY+EJ2_1_RESULTS_DIR + "simulation_takeOffDate");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static List<MarsMissionDistance> ej2_1(MarsMissionConfig config){
        List<MarsMissionDistance> results = new ArrayList<>();

        int interval = (int) config.getDeltaT();
        List<Long> takeOffTimes = new ArrayList<>();
        for(long i = 0; i <= config.getMaxTime();i+=interval){
            takeOffTimes.add(i);
        }
        double deltaT = config.getDeltaT();

        for(Long takeOffTime: takeOffTimes){
            System.out.println("TakeOff Time: "+takeOffTime);
            MarsMission mm = new MarsMission(config);
            MarsMissionResult result = mm.simulate(deltaT,takeOffTime);
            results.add(new MarsMissionDistance(result.getTakeOffTime(),result.getMarsDistance()));
            if(result.isSuccessful()){
                System.out.println("MISSION SUCCESS: SPACESHIP LANDED ON MARS WITH TAKEOFF DATE "+mm.getStartDate().plusSeconds(takeOffTime).format(DateTimeFormatter.ISO_DATE));
                return results;

            }
        }
        System.out.println("MISSION FAILED: SPACESHIP DIDN'T LAND ON MARS");
        return results;
    }

    private static class MarsMissionDistance{
        private final double takeOffTime;
        private final double marsDistance;

        public MarsMissionDistance(double takeOffTime, double marsDistance) {
            this.takeOffTime = takeOffTime;
            this.marsDistance = marsDistance;
        }

        public double getTakeOffTime() {
            return takeOffTime;
        }

        public double getMarsDistance() {
            return marsDistance;
        }
    }


}
