package ar.edu.itba.ss.grid;

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
        double dX = p1.getPosX() - p2.getPosX();
        double dY = p1.getPosY() - p2.getPosY();
        Point2D.Double dR = new Point2D.Double(dX,dY);
        Point2D.Double dV = new Point2D.Double(p1.getVelX()- p2.getVelX(),p1.getPosX() - p2.getVelX());
        double sigma = p1.getRadius() + p2.getRadius();
        double J = 2*p1.getMass()*p2.getMass()*(dV.x*dR.x + dV.y*dR.y)/sigma*(p1.getMass()+p2.getMass());
        Point2D.Double Jv = new Point2D.Double(J*dX/sigma,J*dY/sigma);

        System.out.printf("Prev velocities de %d:  vx: %f vy: %f\n",p1.getId(),p1.getVelX(),p1.getVelY());
        p1.setVelX(p1.getVelX() + Jv.x/p1.getMass());
        p1.setVelY(p1.getVelY() + Jv.y/p1.getMass());
        System.out.printf("Nuevas velocities de %d:  vx: %f vy: %f\n",p1.getId(),p1.getVelX(),p1.getVelY());
        System.out.printf("Prev velocities de %d:  vx: %f vy: %f\n",p2.getId(),p2.getVelX(),p2.getVelY());
        p2.setVelX(p2.getVelX() - Jv.x/p2.getMass());
        p2.setVelY(p2.getVelY() - Jv.y/p2.getMass());
        System.out.printf("Nuevas velocities de %d:  vx: %f vy: %f\n",p2.getId(),p2.getVelX(),p2.getVelY());

    }

    public static void updateWallCollision(Particle p, Event.Direction direction) {
        if (direction == Event.Direction.X){
            p.setVelX(p.getVelX() * -1);
        }else{
            p.setVelY(p.getVelY() * -1);
        }
    }
    public double getWallCollisionTime(double L, Event.Direction direction){
        double tc = 0;


        switch(direction){
            case X:
                if(velX > 0){
                    tc = (L - radius - posX)/velX;
                }else if(velX < 0){
                    tc = (radius - posX)/velX;
                }else{
                    return Double.POSITIVE_INFINITY;
                }
                break;
            case Y:
                if(velY > 0){
                    tc = (L - radius - posY)/velY;
                }else if(velY < 0){
                    tc = (radius - posY)/velY;
                }else{
                    return Double.POSITIVE_INFINITY;
                }
                break;

        }


        return tc;
    }

    public double getParticleCollisionTime(Particle p){
        Point2D.Double dR = new Point2D.Double(p.getPosX()-posX, p.getPosY()-posY);
        Point2D.Double dV= new Point2D.Double(p.getVelX()-velX,p.getVelY()-velY);
        double VR = dV.x * dR.x + dV.y * dR.y;
        if(VR >= 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
        double VV = Math.pow(dV.x,2) + Math.pow(dV.y,2);
        double RR = Math.pow(dR.x,2) + Math.pow(dR.y,2);
        double sigma = radius + p.getRadius();
        double d = Math.pow(VR,2) - VV*(RR - Math.pow(sigma,2));

        if(d < 0){
            return Double.POSITIVE_INFINITY; //INFINITE
        }
        //System.out.printf("dVx: %f, dVy: %f, dRx: %f, dRy: %f, VR: %f, VV: %f, RR: %f, sigma: %f, d: %f, output: %f\n",dV.x,dV.y, dR.x,dR.y,VR, VV,RR,sigma,d,- (VR + Math.sqrt(d))/VV);
        // raiz d > VR esta mal eso en modulo
        //System.out.printf("VR = %f , d = %f sqrt(d) = %f , VR +  SQRT(D) =  %f  \n",VR,d,Math.sqrt(d),VR + Math.sqrt(d));
        return - (VR + Math.sqrt(d))/VV;
    }

    public void update(double time){
        posX += time * velX;
        posY += time * velY;
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
        StringBuilder sb = new StringBuilder("Particle: " + id + "{" +
                "posX=" + posX +
                ", posY=" + posY+", radius= "+radius+", neighbours= [");

        return sb.append("]}").toString();
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

