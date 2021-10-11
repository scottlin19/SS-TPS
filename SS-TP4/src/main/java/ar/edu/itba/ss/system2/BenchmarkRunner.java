package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationSnapshot;
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
import java.util.stream.Collectors;


public class BenchmarkRunner {

    private static final String RESULTS_DIRECTORY = "results/";
    private static final String EJ2_1A_RESULTS_DIR = "ej2_1a/";
    private static final String EJ2_1B_RESULTS_DIR = "ej2_1b/";
    private static final String EJ2_2_RESULTS_DIR = "ej2_2/";
    private static final String EJ3_RESULTS_DIR = "ej3/";
    private static final String EJ3_1A_RESULTS_DIR = "ej3_1a/";

    public static void main(String[] args) {
        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/space_mission_config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));
            SpaceMissionConfig config = new Gson().fromJson(bufferedReader, SpaceMissionConfig.class);

            // Calculate DT
//           JSONWriter<List<MarsMissionEnergy>> mmEnergyWriter =new JSONWriter<>();
//           List<MarsMissionEnergy> energies = calculateDt(config);
//           mmEnergyWriter.createFile(energies, RESULTS_DIRECTORY+EJ2_1A_RESULTS_DIR + "simulation_energy");

            //Ej2_1a
            List<SpaceMissionDistance> results2_1 = calculateTakeoffTime(config);
            JSONWriter<List<SpaceMissionDistance>> mmDistanceWriter = new JSONWriter<>();
            SpaceMissionDistance min = results2_1.stream().min(Comparator.comparingInt(o -> (int) o.targetDistance)).get();
            System.out.printf("Min %s distance: %f with takeOff time = %f", config.getTarget(), min.getTargetDistance(), min.getTakeOffTime());
            String dir = "jupiter".equals(config.getTarget()) ? EJ3_1A_RESULTS_DIR : EJ2_1A_RESULTS_DIR;
            mmDistanceWriter.createFile(results2_1, RESULTS_DIRECTORY + dir + "simulation_takeOffDate");

            //Ej2_2
//            List<MarsMissionVelocity> results2_2 = ej2_2(config);
//            JSONWriter<List<MarsMissionVelocity>> mmVelocityWriter =new JSONWriter<>();
//            MarsMissionVelocity minVelocity = results2_2.stream().min(Comparator.comparingInt(o -> (int) o.totalTime)).get();
//            System.out.printf("Min total time: %f with takeOff velocity = %f",minVelocity.getTotalTime(),minVelocity.getVelocity());
//            mmVelocityWriter.createFile(results2_2, RESULTS_DIRECTORY+EJ2_2_RESULTS_DIR + "simulation_takeOffVel");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static AbstractMission instantiateMission(SpaceMissionConfig config){
        switch (config.getTarget()){
            case "mars":
                return new MarsMission(config);
            case "jupiter":
                return new JupiterMission(config);
            default:
                throw new RuntimeException("Invalid target");
        }
    }

    private static List<Double> calculateEnergy(List<SimulationSnapshot> snapshots){
        List<Double> energies = new ArrayList<>();
        double G = 6.693e-20;
        for(List<Particle> particles : snapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList())){
            double K = 0;
            double P = 0;
            for(Particle p1: particles){
                K += 0.5*p1.getMass()*(Math.pow(p1.getVelX(),2) + Math.pow(p1.getVelY(),2));
                for(Particle p2: particles){
                    if(!p1.equals(p2)){
                        P += -G*p1.getMass()*p2.getMass()/Particle.dist(p1,p2);
                    }
                }
            }
            energies.add(K+P);
        }
        return energies;
    }

    public static List<MarsMissionEnergy> calculateDt(SpaceMissionConfig config){
        List<MarsMissionEnergy> results = new ArrayList<>();

        double maxDeltaT = 60*60;
        double step= 60;
        List<Double> deltaTs = new ArrayList<>();
        for(double i = maxDeltaT; i >= 60;i-= step){
            deltaTs.add(i);
        }
        double takeOffTime = config.getTakeOffTime();

        for(Double deltaT: deltaTs){
            System.out.println("dT: "+deltaT);
            AbstractMission mission = instantiateMission(config);
//            MarsMission mm = new MarsMission(config);
            SpaceMissionResult result = (SpaceMissionResult) mission.simulate(deltaT,takeOffTime,config.getTakeOffSpeed());
            results.add(new MarsMissionEnergy(deltaT,calculateEnergy(result.getSnapshots())));
            if(result.isSuccessful()){
                System.out.println("MISSION SUCCESS: SPACESHIP LANDED ON MARS WITH TAKEOFF DATE " + mission.getStartDate().plusSeconds((long) takeOffTime).format(DateTimeFormatter.ISO_DATE));
                return results;
            }
        }
        System.out.println("MISSION FAILED: SPACESHIP DIDN'T LAND ON MARS");
        return results;
    }

    public static List<SpaceMissionDistance> calculateTakeoffTime(SpaceMissionConfig config){
        List<SpaceMissionDistance> results = new ArrayList<>();

        int interval = (int) config.getDeltaT();
        List<Long> takeOffTimes = new ArrayList<>();
        for(long i = 0; i <= config.getMaxTime();i+=interval){
            takeOffTimes.add(i);
        }
        double deltaT = config.getDeltaT();

        for(Long takeOffTime: takeOffTimes){
            System.out.println("TakeOff Time: "+takeOffTime);
            AbstractMission mission = instantiateMission(config);
//            MarsMission mm = new MarsMission(config);
            SpaceMissionResult result = mission.simulate(deltaT,takeOffTime,config.getTakeOffSpeed());
            results.add(new SpaceMissionDistance(result.getTakeOffTime(),result.getTargetDistance()));
            if(result.isSuccessful()){
                System.out.println(mission.getName() + " SUCCESS: SPACESHIP LANDED WITH TAKEOFF DATE " + mission.getStartDate().plusSeconds(takeOffTime).format(DateTimeFormatter.ISO_DATE) + " (" + takeOffTime + ")");
                return results;

            }
        }
        System.out.println("MISSION FAILED: SPACESHIP DIDN'T LAND");
        return results;
    }

    public static List<MarsMissionVelocity> ej2_2(SpaceMissionConfig config){
        List<MarsMissionVelocity> results = new ArrayList<>();

        double takeOffTime  = config.getTakeOffTime();
        double deltaT = config.getDeltaT();
        List<Double> velocities= new ArrayList<>();
        double maxVelocity = 9;
        double velocityStep = 0.001;
        for(double vel = config.getTakeOffSpeed(); vel <= maxVelocity;vel+=velocityStep){
            velocities.add(vel);
        }

        for(Double velocity: velocities){
            System.out.printf("Velocity: %s km/s\n",velocity);
            AbstractMission mission = instantiateMission(config);
//            MarsMission mm = new MarsMission(config);
            SpaceMissionResult result = mission.simulate(deltaT,takeOffTime,velocity);
            results.add(new MarsMissionVelocity(result.getTakeOffSpeed(),result.getTotalTime(),result.isSuccessful()));
            if(result.isSuccessful()){
                System.out.println(mission.getName() + " SUCCESS: SPACESHIP LANDED ON WITH TAKEOFF DATE " + mission.getStartDate().plusSeconds((long) takeOffTime).format(DateTimeFormatter.ISO_DATE) + " (" + takeOffTime + ")");

            }else{
                System.out.println("MISSION FAILED: SPACESHIP DIDN'T LAND\n");
            }
        }

        return results;
    }

    private static class MarsMissionVelocity{
        private final double velocity;
        private final double totalTime;
        private final boolean successful;

        public MarsMissionVelocity(double velocity, double totalTime,boolean successful) {
            this.velocity = velocity;
            this.totalTime = totalTime;
            this.successful = successful;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public double getVelocity() {
            return velocity;
        }

        public double getTotalTime() {
            return totalTime;
        }
    }

    private static class MarsMissionEnergy{
        private final double deltaT;
        private final List<Double> energies;

        public MarsMissionEnergy(double deltaT, List<Double> energies) {
            this.deltaT = deltaT;
            this.energies = energies;
        }

        public double getDeltaT() {
            return deltaT;
        }

        public List<Double> getEnergies() {
            return energies;
        }
    }

    private static class SpaceMissionDistance{
        private final double takeOffTime;
        private final double targetDistance;

        public SpaceMissionDistance(double takeOffTime, double targetDistance) {
            this.takeOffTime = takeOffTime;
            this.targetDistance = targetDistance;
        }

        public double getTakeOffTime() {
            return takeOffTime;
        }

        public double getTargetDistance() {
            return targetDistance;
        }
    }


}
