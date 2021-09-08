package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.cim.Particle;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DynamicFile {


    public static void initDynamicData(String dynamicDataPath,List<Particle> particles){
        URL resource = DynamicFile.class.getClassLoader().getResource(dynamicDataPath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {

            try {
                File file = new File(resource.toURI());

                try {

                    loadDynamicData(new Scanner(file).useLocale(Locale.US),particles);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadDynamicData(Scanner dynamicData,List<Particle> particles){
        if(dynamicData != null){

            int particleId = 0;

            while(dynamicData.hasNextLine()){

                if(dynamicData.hasNextDouble()){
                    String[] values = dynamicData.nextLine().split(" ");

                    Particle particle = particles.get(particleId);
                    particle.setPosX(Double.parseDouble(values[0]));
                    particle.setPosY(Double.parseDouble(values[1]));
                    particle.setVelocity(Double.parseDouble(values[2]));
                    particle.setDirection(Double.parseDouble(values[3]));
                    particleId++;

                }
                else{
                    dynamicData.nextLine();
//                    System.out.println(+" - Particles: "+particles);
                }
            }
        }else{
            throw new RuntimeException("Dynamic data scanner is null");
        }
    }
}
