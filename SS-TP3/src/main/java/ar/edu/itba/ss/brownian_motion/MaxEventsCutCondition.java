package ar.edu.itba.ss.brownian_motion;

public class MaxEventsCutCondition extends CutCondition{

    private final long MAX_EVENTS;

    private long eventAmount;

    public MaxEventsCutCondition(long maxEvents){
        super();
        this.MAX_EVENTS = maxEvents;
        this.eventAmount = 0;

    }
    @Override
    public boolean cut(Event event) {
        if(event == null){
            return false;
        }
        eventAmount++;
        return MAX_EVENTS == eventAmount;
    }
}
