package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.SimulationResult;
import java.io.*;

public class OutputFile {
    public static final int DEFAULT_BUFFER_SIZE = 4096*2;

    private static final String RESULTS_DIRECTORY = "results/";

    public static void createOutputFile(SimulationResult result, String outPath, OutputTypeEnum outputType) {
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
            outputType.createFile(result,RESULTS_DIRECTORY +outPath);

    }





}
