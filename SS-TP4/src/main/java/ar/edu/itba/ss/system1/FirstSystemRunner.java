package ar.edu.itba.ss.system1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import ar.edu.itba.ss.commons.writers.JSONWriter;
import com.google.gson.Gson;
import ar.edu.itba.ss.commons.SimulationResult;

public class FirstSystemRunner {

    private static final String RESULTS_DIRECTORY = "results/";

    public static void main(String[] args) {
        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            Config config = new Gson().fromJson(bufferedReader, Config.class);
            DampedOscillator dampedOscillator = new DampedOscillator(config);

            SimulationResult result = dampedOscillator.simulate(config.getDeltaT());
            JSONWriter<SimulationResult> jsonWriter = new JSONWriter<>();
            jsonWriter.createFile(result,  "simulation_" + config.getStrategy());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
