package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.Config;

public class GearPredictorStrategy implements UpdateStrategy{
    
    private final double K;
    private final double gamma;
    private static final double[] COEFS_5 = {3.0/16, 251.0/360, 1, 11.0/18, 1.0/6, 1.0/60};
    private double[] r;

    public GearPredictorStrategy(Config config){
        this.K = config.getK();
        this.gamma = config.getGamma();
        this.r = new double[]{0, 0, 0, 0, 0, 0};
    }
    
    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        r[0] = present.getPosX();
        r[1] = present.getVelX();
        r[2] = present.getAccX();
        //calculo r1 r2 r3 r4 r5 predichas
        double pr = pr(deltaT);
        double pr1 = pr1(deltaT);
        double pr2 = pr2(deltaT);
        double pr3 = pr3(deltaT);
        double pr4 = pr4(deltaT);
        double pr5 = pr5(deltaT);
        double mass = present.getMass();
        double newAcx = (-K*pr-gamma*pr1)/mass;
        // calculo correcto deltaR2
        double deltaR2 = (newAcx - pr2) * Math.pow(deltaT,2) / 2;
        Particle future = present.clone();

        //corrigo r1 r2 r3 r4 r5
        // actualizo las posiciones con las corregidas
        future.setPosX(pr + COEFS_5[0] * deltaR2);
        future.setVelX(pr1 + COEFS_5[1] * deltaR2 / deltaT);
        future.setAccX(pr2 + COEFS_5[2] * deltaR2 * 2 / Math.pow(deltaT,2));

        r[3] = pr3 + COEFS_5[3]*deltaR2*6/Math.pow(deltaT,3);
        r[4] = pr4 + COEFS_5[4]*deltaR2*24/Math.pow(deltaT,4);
        r[5] = pr5 + COEFS_5[5]*deltaR2*120/Math.pow(deltaT,5);


        return future;
    }

    private double pr(double deltaT){
        return r[0] + r[1] * deltaT + r[2] * Math.pow(deltaT,2)/2 + r[3] * Math.pow(deltaT,3)/6 + r[4] * Math.pow(deltaT,4)/24 + r[5] * Math.pow(deltaT,5)/120;
    }

    private double pr1(double deltaT){
        return r[1] + r[2] * deltaT + r[3] * Math.pow(deltaT,2)/2 + r[4] * Math.pow(deltaT,3)/6 + r[5] * Math.pow(deltaT,4)/24;
    }

    private double pr2(double deltaT){
        return r[2] + r[3]*deltaT + r[4]*Math.pow(deltaT,2)/2 + r[5]*Math.pow(deltaT,3)/6;
    }

    private double pr3(double deltaT){
        return r[3] + r[4] * deltaT + r[5] * Math.pow(deltaT,2) / 2;
    }

    private double pr4(double deltaT){
        return r[4] + r[5]*deltaT;
    }

    private double pr5(double deltaT){
        return r[5];
    }




}
