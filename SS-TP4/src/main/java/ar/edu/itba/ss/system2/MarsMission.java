package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.*;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy2;

import ar.edu.itba.ss.system1.FirstSystemRunner;
import com.google.gson.Gson;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;

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
    private Particle sun;

    private final UpdateStrategy updateStrategy;

    private final MarsMissionConfig config;

    private final List<SimulationSnapshot> snapshots;

    private boolean takenOff;
    public MarsMission(MarsMissionConfig config){
        this.snapshots = new ArrayList<>();
        this.config = config;
        this.updateStrategy = new VerletOriginalStrategy2();
        this.mars = new Particle(MARS_ID,-2.426617401833969e8,-3.578836154354768e7,3389.92,6.4171e23,4.435907910045917e0,-2.190044178514185e1,0,0, Color.red);

        this.earth = new Particle(EARTH_ID,1.500619962348151e8,2.288499248197072e6,6371.01,5.97219e24,-9.322979134387409e-1,2.966365033636722e1,0,0,Color.blue);
        this.sun = new Particle(SUN_ID,0,0,10000,1.989e30,0,0,0,0,Color.yellow);
      //  System.out.println("ANGULO ENTRE TIERRA Y SOL: "+ Math.atan2(earth.getPosY(), earth.getPosX()));
        setAcc(this.mars, List.of(this.earth, this.sun));//TODO VOLVER
        setAcc(this.earth, List.of(this.mars, this.sun));//TODO VOLVER

        takenOff = false;
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
        System.out.println("CREATE SPACESHIP");
        double sunEarthDist = Particle.dist(earth,sun);
        this.spaceship = new Particle(SPACESHIP_ID,
                this.earth.getPosX() + (1500 + earth.getRadius()) *enX(earth,sun,sunEarthDist),
                earth.getPosY() + (1500 + earth.getRadius())*enY(earth,sun,sunEarthDist),
                2000,
                2e5,
                0,
                0,
                0,0,Color.white);

        double shipEarthDist = Particle.dist(spaceship,earth);
        this.spaceship.setVelX(earth.getVelX() + Math.abs((7.12 + config.getTakeOffSpeed()))*etX(spaceship,earth,shipEarthDist));
        this.spaceship.setVelY(earth.getVelY() + Math.abs(7.12 + config.getTakeOffSpeed())*etY(spaceship,earth,shipEarthDist));
        setAcc(this.spaceship, List.of(this.earth, this.sun, this.mars)); //TODO VOLVER

    }


    public double simulate(double deltaT){
        int maxTime = config.getMaxTime();
        double currentTime = 0;

        Particle pastMars = null;
        Particle pastEarth = null;
        Particle pastSpaceship = null;

        Particle futureMars;
        Particle futureEarth;
        Particle futureSpaceship;
        int step = config.getStep();
        int i = 0;

        while(currentTime <= maxTime){

            if (!takenOff && currentTime >= config.getTakeoffTime()){
                createSpaceship();
                takenOff = true;
            }

            futureEarth = updateStrategy.update(pastEarth, earth, deltaT, currentTime);
            futureMars = updateStrategy.update(pastMars, mars, deltaT, currentTime);
            System.out.println("####### "+"iteration = "+i+" #######");
            System.out.println("currentTime: "+currentTime + " maxTime: "+maxTime);
            System.out.println("SPACESHIP: "+spaceship+"\nEARTH: "+earth+"\nMARS: "+mars+"\nSUN: "+sun+"\n#####################");
           // setAcc(futureMars, List.of(futureEarth, this.sun));
            setAcc(futureEarth, List.of(futureMars, this.sun));//TODO VOLVER
            setAcc(futureMars, List.of(futureEarth, this.sun));
            if(takenOff){
                futureSpaceship = updateStrategy.update(pastSpaceship, spaceship, deltaT, currentTime);
                setAcc(futureSpaceship, List.of(futureEarth,futureMars,this.sun));//TODO VOLVER
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
            }

            if(i % step == 0 ){
                if (takenOff){
                    snapshots.add(new SimulationSnapshot(List.of(spaceship,mars,earth,sun), currentTime));//TODO VOLVER
                }else{
                    snapshots.add(new SimulationSnapshot(List.of(earth,mars,sun), currentTime));
                }

            }
           // System.out.println("ANGULO ENTRE TIERRA Y SOL: "+ Math.atan2(earth.getPosY(), earth.getPosX()));
            pastEarth = earth;
            pastMars = mars;//TODO VOLVER
            earth = futureEarth;
            mars = futureMars;//TODO VOLVER

            currentTime += deltaT;

            i++;
        }
        return currentTime - deltaT;

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

            double totalTime = mm.simulate(config.getDeltaT());
            OutputFile.createOutputFile(new SimulationResult(totalTime, mm.getSnapshots(), config.getStrategy()),  "mars_mission", OutputTypeEnum.EXYZ);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
