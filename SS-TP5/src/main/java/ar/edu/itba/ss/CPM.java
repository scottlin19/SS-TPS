package ar.edu.itba.ss;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import ar.edu.itba.ss.commons.Grid;
import ar.edu.itba.ss.commons.Pedestrian;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.writers.JSONWriter;
import ar.edu.itba.ss.writers.XYZWriter;

public class CPM {

    public static final String                  RESULTS_DIRECTORY = "results/";

    private final Grid                          grid;
    private final List<SimulationSnapshot>      snapshots;
    private final double                        maxTime;

    public CPM(CPMConfig config) {

        this.grid = new Grid(config);
        this.snapshots = new ArrayList<>();
        this.maxTime = config.getMaxTime();
    }

    public SimulationResult simulate(double deltaT,int step) {
        double time = 0;
        int i = 0;
        while(!grid.allPedestriansLeft() && time < maxTime){

            Grid.GridStatus status = grid.updatePedestrians(deltaT);
            if(i % step == 0){
                snapshots.add(new SimulationSnapshot(status.getCurrent(), status.getExited(), time));
            }
            grid.updateGrid();
            grid.updateCollisions(deltaT);
            time += deltaT;
            i++;
        }
        return new SimulationResult(snapshots, grid.getWalls(),time - deltaT);
    }

    public static void main(String[] args) throws IOException {
        URL config_url = CPM.class.getClassLoader().getResource("config/config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        CPMConfig config = objectMapper.readValue(config_url, CPMConfig.class);
        double deltaT = config.getrMin()/(2*Math.max(config.getVdMax(),config.getVe()));
        CPM cpm = new CPM(config);
        SimulationResult result = cpm.simulate(deltaT, config.getK());

        JSONWriter<SimulationResult> jsonWriter = new JSONWriter<>();
        jsonWriter.createFile(result, RESULTS_DIRECTORY + "simulation.json");

        XYZWriter xyzWriter = new XYZWriter();
        xyzWriter.createFile(result,RESULTS_DIRECTORY + "simulation");
    }
}
