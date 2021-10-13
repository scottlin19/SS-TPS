//package ar.edu.itba.ss.system2.cut_conditions;
//
//import ar.edu.itba.ss.commons.Particle;
//
//public class LandedOnTargetCutCondition extends CutCondition{
//
//    private final int LIMIT;
//
//
//    public LandedOnTargetCutCondition(int limit){
//        this.LIMIT = limit;
//    }
//    @Override
//    public boolean cut(Particle spaceship, Particle target) {
//        if(spaceship == null){
//            return false;
//        }
//        //System.out.println("spceship: "+spaceship + "\nmars: "+mars);
//       // System.out.println("LANDED ON MARS CUT: "+Math.sqrt(Math.pow(mars.getPosX()-spaceship.getPosX(),2)  + Math.pow(mars.getPosY() - spaceship.getPosY(),2)) + "mars radius: "+mars.getRadius());
//        boolean cut = Particle.dist(spaceship,target) <= target.getRadius() + LIMIT;
//        if(cut){
//            System.out.println("SPACESHIP LANDED!");
//        }
//        return cut;
//    }
//
//
//
//
//}
