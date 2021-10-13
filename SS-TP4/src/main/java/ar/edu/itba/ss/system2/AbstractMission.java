package ar.edu.itba.ss.system2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy2;
import ar.edu.itba.ss.commons.writers.JSONWriter;
import ar.edu.itba.ss.commons.writers.XYZWriter;
import ar.edu.itba.ss.system1.FirstSystemRunner;
import ar.edu.itba.ss.system2.cut_conditions.CutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MaxTimeCutCondition;

public abstract class AbstractMission {
    protected final int SPACESHIP_ID = 1;
    protected final int MARS_ID = 2;
    protected final int EARTH_ID = 3;
    protected final int SUN_ID = 4;
    protected final int JUPITER_ID = 5;
    protected final double G = 6.693e-20;

    protected Particle spaceship;
    protected Particle mars;
    protected Particle earth;
    protected final Particle sun;
    protected LocalDateTime startDate;
    protected final UpdateStrategy updateStrategy;

    protected final SpaceMissionConfig config;

    protected final List<SimulationSnapshot> snapshots;
    protected boolean takenOff;
    protected double targetMinDistance;

    protected CutCondition missedTargetCC,maxTimeCC,enteredOrbitCC;

    public AbstractMission(SpaceMissionConfig config){
        this.config = config;
        this.snapshots = new ArrayList<>();
        this.updateStrategy = new VerletOriginalStrategy2();
        this.targetMinDistance = Double.MAX_VALUE;
        this.startDate = LocalDateTime.of(2021,9, 24,0,0,0);
        this.mars = new Particle(MARS_ID,-2.426617401833969e8,-3.578836154354768e7,3389.92,6.4171e23,4.435907910045917e0,-2.190044178514185e1,0,0, new Particle.Color(255,0,0));
        this.earth = new Particle(EARTH_ID,1.500619962348151e8,2.288499248197072e6,6371.01,5.97219e24,-9.322979134387409e-1,2.966365033636722e1,0,0,new Particle.Color(0,0,255));
        this.sun = new Particle(SUN_ID,0,0,15000,1.989e30,0,0,0,0,new Particle.Color(255,255,0));
        setAcc(this.mars, List.of(this.earth, this.sun));
        setAcc(this.earth, List.of(this.mars, this.sun));
        this.maxTimeCC = new MaxTimeCutCondition(config.getMaxTime(),config.getDeltaT());
        this.takenOff = false;



    }

    protected void setAcc(Particle particle, List<Particle> particles){
        double fx = 0;
        double fy = 0;
        for(Particle p : particles){
            double dist = Particle.dist(particle,p);
            double fn = calculateGravityForce(particle,p,dist);
            // System.out.println("ENx = "+enX(particle,p, dist));
            fx += fn * enX(particle,p, dist);
            // System.out.println("ENy = "+enY(particle,p, dist));
            fy += fn * enY(particle,p, dist);
        }
        //  System.out.println("new AccX = "+fx/particle.getMass());
        particle.setAccX(fx/particle.getMass());
        // System.out.println("new AccY = "+fy/particle.getMass());
        particle.setAccY(fy/particle.getMass());
    }

    protected double enX(Particle pi, Particle pj,double dist){
        return (pj.getPosX() - pi.getPosX())/dist;
    }
    protected double enY(Particle pi, Particle pj,double dist){
        return (pj.getPosY() - pi.getPosY())/dist;
    }
    protected double etX(Particle pi, Particle pj,double dist){
        return -enY(pi, pj, dist);
    }
    protected double etY(Particle pi, Particle pj,double dist){
        return enX(pi, pj, dist);
    } //capaz cambia el signo tambien c:

    protected double calculateGravityForce(Particle pi, Particle pj,double dist){
        return G*pi.getMass()*pj.getMass()/Math.pow(dist,2);
    }

    protected void createSpaceship(double initialVelocity, List<Particle> planets){
        System.out.println("LAUNCHING SPACESHIP");
        double sunEarthDist = Particle.dist(earth,sun);
        this.spaceship = new Particle(SPACESHIP_ID,
            this.earth.getPosX() + (1500 + earth.getRadius()) *enX(earth,sun,sunEarthDist),
            earth.getPosY() + (1500 + earth.getRadius())*enY(earth,sun,sunEarthDist),
            2000,
            2e5,
            0,
            0,
            0,0,new Particle.Color(255,255,255));
        double shipEarthDist = Particle.dist(spaceship,earth);
        this.spaceship.setVelX(earth.getVelX() + Math.abs((7.12 + initialVelocity))*etX(spaceship,earth,shipEarthDist));
        this.spaceship.setVelY(earth.getVelY() + Math.abs(7.12 + initialVelocity)*etY(spaceship,earth,shipEarthDist));
        setAcc(this.spaceship, planets);
    }

    protected boolean isSuccessful(){
        if(!takenOff){
            return false;
        }
        return enteredOrbitCC.getState() != CutCondition.State.MISS;
    }

    public abstract SpaceMissionResult simulate(double deltaT,double takeOffTime,double initialVelocity);

    public static void main(String[] args) {

        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/space_mission_config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));

            SpaceMissionConfig config = new Gson().fromJson(bufferedReader, SpaceMissionConfig.class);
            AbstractMission mission;
            switch (config.getTarget()){
                case "mars":
                    mission = new MarsMission(config);
                    break;
                case "jupiter":
                    mission = new JupiterMission(config);
                    break;
                default:
                    throw new RuntimeException("Invalid target");
            }
//            MarsMission mm = new MarsMission(config);

            SpaceMissionResult result = mission.simulate(config.getDeltaT(),config.getTakeOffTime(),config.getTakeOffSpeed());
            if(result.isSuccessful()){
                System.out.printf(mission.getName() + " SUCCESS: SPACESHIP LANDED AT %s WITH TAKEOFF DATE %s\n",mission.getStartDate().plusSeconds((long) result.getTotalTime()).format(DateTimeFormatter.ISO_DATE),mission.getStartDate().plusSeconds((long) result.getTakeOffTime()).format(DateTimeFormatter.ISO_DATE));
            }else{
                System.out.println(mission.getName() + " FAILED: SPACESHIP DIDN'T LAND");
            }
            XYZWriter xyzWriter = new XYZWriter();
            xyzWriter.createFile(result,  "results/ej2_1b/" + mission.getName() + "_V" + config.getTakeOffSpeed());
            JSONWriter<SpaceMissionResult> jsonWriter = new JSONWriter<>();
            jsonWriter.createFile(result,"results/ej2_1b/" + mission.getName() + "_V" + config.getTakeOffSpeed());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public List<SimulationSnapshot> getSnapshots(){
        return snapshots;
    }

    public abstract String getName();
}
