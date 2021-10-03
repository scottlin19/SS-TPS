package ar.edu.itba.ss.system2;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.LocalDateTime;

public class MarsMissionConfig {

    private final int maxTime;
    private final String strategy;
    private final double deltaT;
    private final double takeoffTime;


    private  LocalDateTime startDate;
    private final double takeOffSpeed;

    private final int step;


    public MarsMissionConfig(int maxTime, String strategy, double deltaT, LocalDateTime startDate, double takeoffTime, double takeOffSpeed, int step){
        this.maxTime = maxTime;
        this.strategy = strategy;
        this.deltaT = deltaT;
        this.startDate = LocalDateTime.of(2021,9, 24,0,0,0);
        this.takeoffTime = takeoffTime;
        this.takeOffSpeed = takeOffSpeed;
        this.step = step;

    }
    public LocalDateTime getStartDate(){
        return startDate;
    }

    public double getTakeoffTime(){
        return takeoffTime;
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
