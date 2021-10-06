package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.*;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy2;
import ar.edu.itba.ss.commons.writers.JSONWriter;
import ar.edu.itba.ss.commons.writers.XYZWriter;
import ar.edu.itba.ss.system1.FirstSystemRunner;
import ar.edu.itba.ss.system2.cut_conditions.CutCondition;
import ar.edu.itba.ss.system2.cut_conditions.LandedOnMarsCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MaxTimeCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MissedMarsCutCondition;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MarsMission {

    private final int SPACESHIP_ID = 1;
    private final int MARS_ID = 2;
    private final int EARTH_ID = 3;
    private final int SUN_ID = 4;
    private final double G = 6.693e-20;

    private Particle spaceship;
    private Particle mars;
    private Particle earth;
    private final Particle sun;
    private final LocalDateTime startDate;
    private final UpdateStrategy updateStrategy;

    private final MarsMissionConfig config;

    private final List<SimulationSnapshot> snapshots;
    private boolean takenOff;
    private double marsMinDistance;

    private final CutCondition missedMarsCC,maxTimeCC,landedOnMarsCC;
    public MarsMission(MarsMissionConfig config){
        this.snapshots = new ArrayList<>();
        this.config = config;
        this.updateStrategy = new VerletOriginalStrategy2();
        this.mars = new Particle(MARS_ID,-2.426617401833969e8,-3.578836154354768e7,3389.92,6.4171e23,4.435907910045917e0,-2.190044178514185e1,0,0, new Particle.Color(255,0,0));

        this.earth = new Particle(EARTH_ID,1.500619962348151e8,2.288499248197072e6,6371.01,5.97219e24,-9.322979134387409e-1,2.966365033636722e1,0,0,new Particle.Color(0,0,255));
        this.sun = new Particle(SUN_ID,0,0,10000,1.989e30,0,0,0,0,new Particle.Color(255,255,0));
        setAcc(this.mars, List.of(this.earth, this.sun));
        setAcc(this.earth, List.of(this.mars, this.sun));

        takenOff = false;
        this.missedMarsCC = new MissedMarsCutCondition(mars);
        this.maxTimeCC = new MaxTimeCutCondition(config.getMaxTime(),config.getDeltaT());
        this.landedOnMarsCC = new LandedOnMarsCutCondition();
        this.marsMinDistance = Double.MAX_VALUE;
        this.startDate = LocalDateTime.of(2021,9, 24,0,0,0);
    }

    private void setAcc(Particle particle, List<Particle> particles){
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

    private double enX(Particle pi, Particle pj,double dist){
        return (pj.getPosX() - pi.getPosX())/dist;
    }
    private double enY(Particle pi, Particle pj,double dist){
        return (pj.getPosY() - pi.getPosY())/dist;
    }
    private double etX(Particle pi, Particle pj,double dist){
        return -enY(pi, pj, dist);
    }
    private double etY(Particle pi, Particle pj,double dist){
        return enX(pi, pj, dist);
    } //capaz cambia el signo tambien c:

    private double calculateGravityForce(Particle pi, Particle pj,double dist){
        return G*pi.getMass()*pj.getMass()/Math.pow(dist,2);
    }

    private void createSpaceship(){
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
        this.spaceship.setVelX(earth.getVelX() + Math.abs((7.12 + config.getTakeOffSpeed()))*etX(spaceship,earth,shipEarthDist));
        this.spaceship.setVelY(earth.getVelY() + Math.abs(7.12 + config.getTakeOffSpeed())*etY(spaceship,earth,shipEarthDist));
        setAcc(this.spaceship, List.of(this.earth, this.sun, this.mars));

    }


    public MarsMissionResult simulate(double deltaT,double takeOffTime){
        System.out.println("Starting mars mission with takeOff Time: "+takeOffTime);
        double currentTime = 0;
        Particle pastMars = null;
        Particle pastEarth = null;
        Particle pastSpaceship = null;
        Particle futureMars;
        Particle futureEarth;
        Particle futureSpaceship;
        int step = config.getStep();
        int i = 0;


        while(!landedOnMarsCC.cut(spaceship,mars) && !missedMarsCC.cut(spaceship,mars) && !maxTimeCC.cut(spaceship,mars) ){
            if (!takenOff && currentTime >= takeOffTime){
                System.out.printf("Taking off... (%.0fs)\n",currentTime);
                createSpaceship();
                takenOff = true;
                marsMinDistance = Double.min(marsMinDistance,Particle.dist(mars,spaceship));
            }

            futureEarth = updateStrategy.update(pastEarth, earth, deltaT, currentTime);
            futureMars = updateStrategy.update(pastMars, mars, deltaT, currentTime);
            setAcc(futureEarth, List.of(futureMars, this.sun));
            setAcc(futureMars, List.of(futureEarth, this.sun));
            pastEarth = earth;
            pastMars = mars;
            earth = futureEarth;
            mars = futureMars;
            if(takenOff){
                futureSpaceship = updateStrategy.update(pastSpaceship, spaceship, deltaT, currentTime);
                setAcc(futureSpaceship, List.of(futureEarth,futureMars,this.sun));
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
                marsMinDistance = Double.min(marsMinDistance,Particle.dist(spaceship,mars));
            }

            if(i % step == 0){
                if (takenOff){
                    snapshots.add(new SimulationSnapshot(List.of(spaceship,mars,earth,sun), currentTime));
                }else{
                    snapshots.add(new SimulationSnapshot(List.of(earth,mars,sun), currentTime));
                }
            }
            currentTime += deltaT;
            i++;
        }
        if (takenOff){
            snapshots.add(new SimulationSnapshot(List.of(spaceship,mars,earth,sun), currentTime));
        }else{
            snapshots.add(new SimulationSnapshot(List.of(earth,mars,sun), currentTime));
        }
        System.out.printf("Simulation finished at time %.0fs with minimum distance to mars = %fkm\n",currentTime,marsMinDistance-mars.getRadius());
        return new MarsMissionResult(currentTime,takeOffTime,marsMinDistance - mars.getRadius(),isSuccessful(),config.getTakeOffSpeed(),snapshots);

    }

    public boolean isSuccessful(){
        if(!takenOff){
            return false;
        }
        return Particle.dist(mars,spaceship) <= mars.getRadius();
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public List<SimulationSnapshot> getSnapshots(){
        return snapshots;
    }
    public static void main(String[] args) {

        URL config_url = FirstSystemRunner.class.getClassLoader().getResource("config/mars_mission_config.json");
        if (config_url == null) {
            System.out.println("Config file not found, exiting...");
            return;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_url.getFile()));

            MarsMissionConfig config = new Gson().fromJson(bufferedReader, MarsMissionConfig.class);
            MarsMission mm = new MarsMission(config);

            MarsMissionResult result =  mm.simulate(config.getDeltaT(),config.getTakeOffTime());
            if(result.isSuccessful()){
                System.out.printf("MISSION SUCCESS: SPACESHIP LANDED ON MARS AT %s WITH TAKEOFF DATE %s\n",mm.getStartDate().plusSeconds((long) result.getTotalTime()).format(DateTimeFormatter.ISO_DATE),mm.getStartDate().plusSeconds((long) result.getTakeOffTime()).format(DateTimeFormatter.ISO_DATE));
            }else{
                System.out.println("MISSION FAILED: SPACESHIP DIDN'T LAND ON MARS");
            }
            XYZWriter xyzWriter = new XYZWriter();
            xyzWriter.createFile(result,  "results/ej2_1b/mars_mission");
            JSONWriter<MarsMissionResult> jsonWriter = new JSONWriter<>();
            jsonWriter.createFile(result,"results/ej2_1b/mars_mission");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
