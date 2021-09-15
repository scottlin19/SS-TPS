package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.Event;
import java.awt.geom.Point2D;

public class Particle{

    private final int id;
    private double posX, posY;
    private double velX,velY;
    private double radius;
    private final double mass;


    public Particle(int id,double posX,double posY,double radius, double mass, double velX, double velY){
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;

    }

    public static void updateParticleCollision(Particle p1, Particle p2){
        double dX = p2.getPosX() - p1.getPosX();
        double dY = p2.getPosY() - p1.getPosY();
        Point2D.Double dR = new Point2D.Double(dX,dY);
        Point2D.Double dV = new Point2D.Double(p2.getVelX()- p1.getVelX(),p2.getVelY() - p1.getVelY());
        double sigma = p1.getRadius() + p2.getRadius();
        double m1 = p1.getMass();
        double m2 = p2.getMass();
        double J = 2.0*m1*m2*(dV.x*dR.x + dV.y*dR.y)/(sigma*(m1+m2));

        Point2D.Double Jv = new Point2D.Double(J*dX/sigma,J*dY/sigma);

        p1.setVelX(p1.getVelX() + Jv.x/m1);
        p1.setVelY(p1.getVelY() + Jv.y/m1);

        p2.setVelX(p2.getVelX() - Jv.x/m2);
        p2.setVelY(p2.getVelY() - Jv.y/m2);


    }

    public static void updateWallCollision(Particle p, Event.Direction direction) {
       // System.out.printf("Prev velocities de %d:  vx: %f vy: %f\n",p.getId(),p.getVelX(),p.getVelY());
        if (direction == Event.Direction.X){
            p.setVelX(p.getVelX() * -1);
        }else{
            p.setVelY(p.getVelY() * -1);
        }
       // System.out.printf("Nuevas velocities de %d:  vx: %f vy: %f\n",p.getId(),p.getVelX(),p.getVelY());
    }
    public double getWallCollisionTime(double L, Event.Direction direction){
        double tc = 0;
        switch(direction){
            case X:
                if(velX > 0){
                   // System.out.println("choco con pared derecha");
                    tc = (L - radius - posX)/velX;
                }else if(velX < 0){
                    //System.out.println("choco con pared izq");
                    tc = (radius - posX)/velX;
                }else{
                    return Double.POSITIVE_INFINITY;
                }
                break;
            case Y:
                if(velY > 0){

                    //System.out.println("choco con pared abajo");
                    tc = (L - radius - posY)/velY;
                }else if(velY < 0){
                   // System.out.println("choco con pared arriba");
                    tc = (radius - posY)/velY;
                }else{
                    return Double.POSITIVE_INFINITY;
                }
                break;

        }


        return tc;
    }

    public double getParticleCollisionTime(Particle p){
        Point2D.Double dR = new Point2D.Double(p.getPosX()-posX, p.getPosY()-posY); //  ΔR
        Point2D.Double dV= new Point2D.Double(p.getVelX()-velX,p.getVelY()-velY); //  ΔV
        double VR = dV.x * dR.x + dV.y * dR.y;//   ΔV*ΔR = ΔVx*Δx +  ΔVy*Δy
        if(VR >= 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
        double VV = Math.pow(dV.x,2) + Math.pow(dV.y,2); //  ΔV*ΔV  =  ΔVx^2  +  ΔVy^2
        double RR = Math.pow(dR.x,2) + Math.pow(dR.y,2);//   ΔR*ΔR  =  Δx^2  +  Δy^2
        double sigma = radius + p.getRadius();
        double d = Math.pow(VR,2) - VV*(RR - Math.pow(sigma,2));

        if(d < 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
       // System.out.printf("dVx: %f, dVy: %f, dRx: %f, dRy: %f, VR: %f, VV: %f, RR: %f, sigma: %f, d: %f, output: %f\n",dV.x,dV.y, dR.x,dR.y,VR, VV,RR,sigma,d,- (VR + Math.sqrt(d))/VV);
        // raiz d > VR esta mal eso en modulo
       // System.out.printf("Colision(p1:%d,p2:%d): VR = %f , d = %f sqrt(d) = %f , VR +  SQRT(D) =  %f  \n",getId(),p.getId(),VR,d,Math.sqrt(d),VR + Math.sqrt(d));
        return - (VR + Math.sqrt(d))/VV;
    }

    public void update(double time){

      //  System.out.println("PREV POS p: "+id + ", X: "+posX + ", Y: "+posY + "radius: "+radius);
        posX += time * velX;
        posY += time * velY;
       // System.out.println("NUEVA POS p: "+id + ", X: "+posX + ", Y: "+posY+ "radius: "+radius);
    }


    /* ---------------- GETTERS, SETTERS, EQUALS, HASH, TOSTRING ---------------- */
    public int getId() {
        return id;
    }

    public double getVelX(){
        return velX;
    }

    public void setVelX(double velX){
        this.velX = velX;
    }

    public double getVelY(){
        return velY;
    }

    public void setVelY(double velY){
        this.velY = velY;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }



    public double getMass() {
        return mass;
    }


    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", posX=" + posX +
                ", posY=" + posY +
                ", velX=" + velX +
                ", velY=" + velY +
                ", radius=" + radius +
                ", mass=" + mass +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Particle)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Particle other = (Particle) o;

        // Compare the data members and return accordingly
        return this.id == other.id;
    }


}

