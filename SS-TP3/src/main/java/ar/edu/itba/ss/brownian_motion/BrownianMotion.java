package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.Particle;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BrownianMotion {

//    private final StaticData sd;
    private final double L;
    private final List<SimulationSnapshot> snapshots;
    private static final String RESULTS_DIRECTORY = "results";
    private final List<Particle> particles;
    private Particle bigBoi;
    private Set<Event> events;
    private long totalCollisions;
    private double totalTime;
    private SimulationResult result;


    // Contruct OffLattice from provided values (for benchmarking usage)
    public BrownianMotion(List<Particle> particles, double L, Particle bigBoi){
        this.particles = particles;
        this.L = L;
        this.snapshots = new ArrayList<>();
        this.events = new TreeSet<>();
        this.bigBoi = bigBoi;
        this.totalCollisions = 0;
        this.totalTime = 0;
    }

    public void simulate(Predicate<Event> cutConditions){
        System.out.println("Starting simulation . . .");
        Event e = null;
        while(!cutConditions.test(e)){
            e = update();
        }
        System.out.println("Simulation terminated.");
        createResult();
    }

    private void createResult(){
        double totalTime = snapshots.stream().mapToDouble(s-> s.getEvent().getTime()).sum();
        result = new SimulationResult(totalCollisions,totalTime,snapshots);
    }

    public SimulationResult getResult() {
        return result;
    }

    private void snapshot(Event event){
        snapshots.add(new SimulationSnapshot(particles,event));
    }

    // For every particle --> get Tc
    public Event update(){

        if(events.isEmpty()){
            calculateEvents();
        }
        // PASO 2: Choose first event with lower Tc
        Event e = events.stream().findFirst().orElse(null);
        if(e == null){
            throw new IllegalStateException();
        }
        events.remove(e);

        double tc = e.getTime();
        totalCollisions++;
        totalTime+=tc;
        e.setRelativeTime(totalTime);

        Particle p1 = e.getP1();
        Particle p2 = e.getP2();

        //PASO 3: Updeteamos posiciones de particulas

        particles.forEach(p ->  p.update(tc));
       //System.out.println("Updated times: "+particles);
        // PASO 4: Sacar snapshot
        snapshot(e);
        // PASO 5: Aplicamos operador de colision con las particulas del evento
        e.update();

        //Eliminamos todos los eventos invalidos
        Set<Particle> invalidParticles = events.stream().filter(ev -> ev.containsParticle(p1) || ev.containsParticle(p2)).map(Event::getParticles).flatMap(Collection::stream).collect(Collectors.toCollection(HashSet::new));
        events = events.stream().filter(ev -> !ev.containsParticle(p1) && !ev.containsParticle(p2)).collect(Collectors.toCollection(TreeSet::new));

        //Restamos los tiempos del evento a todos los otros eventos
        events.forEach(ev -> ev.updateTime(tc));

        // PASO 6: Calculamos los eventos para las particulas que participaron en la colision y los que tienen un evento con alguna de las particulas qeu particuparon de la colision
        calculateEventForParticle(p1,p2);
        if(p2 != null){
            calculateEventForParticle(p2,p1);
        }
        invalidParticles.forEach(p ->calculateEventForParticle(p,null));
        //System.out.println("NEW EVENT LIST: "+events);

        return e;
    }


    public long getTotalCollisions() {
        return totalCollisions;
    }


    public void calculateEvents(){

        for(Particle p1: particles){
            calculateEventForParticle(p1,null);
        }
    }

    public void calculateEventForParticle(Particle p, Particle exclude){
       // System.out.println("CALCULO EVENT PARA " + p);
        double tcParticle = Double.POSITIVE_INFINITY;
        Event.Direction dir;

        double tcWallX = p.getWallCollisionTime(L, Event.Direction.X);
        double tcWallY = p.getWallCollisionTime(L,Event.Direction.Y);
       // System.out.println("tcWallX: "+tcWallX+" tcWallY:"+tcWallY);
        double tcWall;
        if(tcWallX < tcWallY){
            tcWall = tcWallX;
            dir = Event.Direction.X;
        }else{
            tcWall = tcWallY;
            dir = Event.Direction.Y;
        }
        Particle cParticle = null;
        for(Particle p2 : particles){
            if(!p.equals(p2) && !p2.equals(exclude)){
                double t = p.getParticleCollisionTime(p2);
                    if(t < tcParticle){
                        tcParticle = t;
                        cParticle = p2;
                    }
            }
        }

       // System.out.println("Particle "+p.getId()+" tcWall: "+tcWall + " tcParticle: "+tcParticle+" particle con la cual colisiono: "+ (cParticle != null ? cParticle.getId(): null));
        if(tcWall <= tcParticle){
            events.add(new Event(tcWall,p,dir));

        }else{
            if(cParticle == null){
                throw new IllegalStateException();
            }
            events.add(new Event(tcParticle,p,cParticle));
        }

    }


    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }

}
