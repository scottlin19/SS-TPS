package ar.edu.itba.ss.sistem1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import com.google.gson.Gson;

import ar.edu.itba.ss.commons.OutputFile;
import ar.edu.itba.ss.commons.OutputTypeEnum;
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

            dampedOscillator.simulate();
            OutputFile.createOutputFile(new SimulationResult(config.getTf(), dampedOscillator.getSnapshots(), config.getStrategy()),  "simulation_" + config.getStrategy(), OutputTypeEnum.JSON);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
