package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.StaticData;
import ar.edu.itba.ss.grid.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BrownianMotion {

//    private final StaticData sd;
    private final double L;
    private final List<SimulationSnapshot> snapshots;
    private static final String RESULTS_DIRECTORY = "results";
    private final List<Particle> particles;
    private Particle bigBoi;
    private Set<Event> events;


    // Contruct OffLattice from provided values (for benchmarking usage)
    public BrownianMotion(List<Particle> particles, double L, Particle bigBoi){
        this.particles = particles;

//        this.sd = new StaticData(L, maxIterations);
        this.L = L;
        System.out.println("L: "+ L);
        this.snapshots = new ArrayList<>();
        this.events = new TreeSet<>();
        this.bigBoi = bigBoi;
    }

    public void simulate(CutCondition cutCondition){
        Event e = null;
        for(int i = 0; i < 100; i++){
            update();
        }

//        while(!cutCondition.cut(e)){
//            e = update();
//        }
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
            // Throw something
        }
        double tc = e.getTime();

        Particle p1 = e.getP1();
        Particle p2 = e.getP2();
        double eTime = e.getTime();
        if(p2 == null){
            System.out.println("WALL COLLISION: EVENTO DE P1: " + p1.getId());
        }
        else{
            System.out.println("PARTICLES COLLISION: EVENTO DE P1: " + p1.getId() + " Y P2: " + p2.getId());
        }

        //PASO 3: Updeteamos posiciones de particulas
        System.out.println("");
        particles.forEach(p ->  p.update(eTime));
        System.out.println("Updated times: "+particles);
        // PASO 4: Sacar snapshot
        snapshot(e);
        // PASO 5: Aplicamos operador de colision con las particulas del evento
        e.update();

        //Eliminamos todos los eventos invalidos
        events =  events.stream().filter(ev -> !ev.containsParticle(p1) && !ev.containsParticle(p2)).collect(Collectors.toCollection(TreeSet::new));

        //Restamos los tiempos del evento a todos los otros eventos
        events.forEach(ev -> ev.updateTime(tc));

        // PASO 6: Calculamos los eventos para las particulas que participaron en la colision
        calculateEventForParticle(p1);
        if(p2 != null){
            calculateEventForParticle(p2);
        }

        return e;
    }


    public void calculateEvents(){

        for(Particle p1: particles){
            calculateEventForParticle(p1); //no cheuear con la que me acabo de colisionar re alpedo
        }
    }

    public void calculateEventForParticle(Particle p){
        System.out.println("CALCULO EVENT PARA "+p.getId());
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
            if(!p.equals(p2)){ // no me anda el cerebelo si dontCheck da null quiero que entre :sno entiendo
                double t = p.getParticleCollisionTime(p2);
                    if(t < 0){
                        System.out.printf("T da negativo: X1: %f Y1: %f X2: %f Y2: %f  \n",p.getPosX(),p.getPosY(),p2.getPosX(),p2.getPosY());
                    }
                    if(t < tcParticle){
                        tcParticle = t;
                        cParticle = p2;
                    }
            }
        }

        System.out.println("Particle "+p.getId()+" tcWall: "+tcWall + " tcParticle: "+tcParticle+" particle con la cual colisiono: "+ (cParticle != null ? cParticle.getId(): null));
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

    public void addParticle(Particle particle){
        particles.add(particle);
    }

    public void removeParticle(Particle particle){
        particles.remove(particle);

    }
}
