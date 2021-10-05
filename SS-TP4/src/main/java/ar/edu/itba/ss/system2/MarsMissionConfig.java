package ar.edu.itba.ss.system2;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.LocalDateTime;

public class MarsMissionConfig {

    private final int maxTime;
    private final String strategy;
    private final double deltaT;
    private final double takeOffTime;

    private final double takeOffSpeed;

    private final int step;


    public MarsMissionConfig(int maxTime, String strategy, double deltaT, double takeOffTime, double takeOffSpeed, int step){
        this.maxTime = maxTime;
        this.strategy = strategy;
        this.deltaT = deltaT;
        this.takeOffTime = takeOffTime;
        this.takeOffSpeed = takeOffSpeed;
        this.step = step;

    }


    public double getTakeOffTime(){
        return takeOffTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public String getStrategy() {
        return strategy;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getTakeOffSpeed() {
        return takeOffSpeed;
    }

    public int getStep() {
        return step;
    }
}
