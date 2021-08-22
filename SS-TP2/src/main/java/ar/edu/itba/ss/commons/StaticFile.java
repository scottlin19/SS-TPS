package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.cim.Particle;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class StaticFile {
    private Integer L;
    private Integer N;
    private Double RC;
    private int MAX_M;
    private double initialVelocity;
    private double dirNoise;
    private Boolean hasWalls;
    private Integer maxIterations;

    public StaticFile(String staticDataPath, List<Particle> particles){
        initStaticData(staticDataPath, particles);
    }

    public void initStaticData(String staticDataPath, List<Particle> particles){
        Scanner sc = null;
        // pass the path to the file as a parameter
        URL resource = StaticFile.class.getClassLoader().getResource(staticDataPath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {

            try {
                File file = new File(resource.toURI());

                try {
                    sc = new Scanner(file).useLocale(Locale.US);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if(sc != null){
                    int line = 0;
                    while (line < 7 && sc.hasNextLine()){

                        switch(line){
                            case 0:
                                N = sc.nextInt();
                                break;
                            case 1:
                                L = sc.nextInt();
                                break;
                            case 2:
                                RC = sc.nextDouble();
                                break;

                            case 3:
                                hasWalls = sc.nextInt() == 1 ? Boolean.TRUE : Boolean.FALSE;
                                break;
                            case 4:
                                double ETA = sc.nextDouble();
                                Random r = new Random();
                                dirNoise = r.nextDouble()*ETA - ETA/2;
                                System.out.println("dir Noise:" +dirNoise);
                                break;
                            case 5:
                                maxIterations = sc.nextInt();
                                break;
                            case 6:
                                initialVelocity = sc.nextDouble();
                                break;
                        }
                        line++;

                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if(L == null || RC == null || N == null || hasWalls == null){
            throw new IllegalArgumentException("Invalid particle static values");
        }

        double maxRadius = initParticles(sc,particles);
        MAX_M = (int) Math.floor((L/(RC + 2*maxRadius)));

        System.out.println("L = "+L+" M = "+MAX_M+" RC = "+RC+" N= "+N + " hasWalls= " + hasWalls);
//        this.grid = new Grid(L,M,RC,hasWalls);
    }

    public double initParticles(Scanner sc, List<Particle> particles){

        // pass the path to the file as a parameter
        double maxRadius = 0;
        if(sc != null) {
            int line = 0;
            while (sc.hasNextDouble()) {
                double radius = sc.nextDouble();
                if(maxRadius < radius){
                    maxRadius = radius;
                }
                particles.add(new Particle(line++,0.0,0.0,radius,dirNoise,initialVelocity));

            }
            System.out.println("Loaded "+line+" particles.");
            // System.out.println("Particles: "+particles);
        }else{
            throw new IllegalArgumentException("Scanner can't be null");
        }

        return maxRadius;
    }

    public Integer getL() {
        return L;
    }

    public Integer getN() {
        return N;
    }

    public Double getRC() {
        return RC;
    }

    public int getMaxM() {
        return MAX_M;
    }

    public Boolean getHasWalls() {
        return hasWalls;
    }

    public int getMAX_M() {
        return MAX_M;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public double getDirNoise() {
        return dirNoise;
    }
}
