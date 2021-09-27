package ar.edu.itba.ss.sistem1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import com.google.gson.Gson;

import ar.edu.itba.ss.commons.DampedOscillatorFunctions;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy;

public class FirstSystemRunner {

    public static void main(String[] args) {
        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            Config config = new Gson().fromJson(bufferedReader, Config.class);
            DampedOscillator dampedOscillator = new DampedOscillator()
                .withUpdateStrategy(new VerletOriginalStrategy(config.getDeltaT(), new DampedOscillatorFunctions(config.getR0(), config.getK(), config.getGamma())))
                .withConfig(config)
                ;
            dampedOscillator.simulate();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
