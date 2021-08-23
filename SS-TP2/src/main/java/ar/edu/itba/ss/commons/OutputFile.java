package ar.edu.itba.ss.commons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputFile {
    private static final int MAX_RGB_VAL = 255;
    private static final String RESULTS_DIRECTORY = "results/";
    public static void createOutputFile(List<List<ParticleDTO>> snapshots,String fileName){
        File directory = new File(RESULTS_DIRECTORY);
        if (!directory.exists()){
            if(!directory.mkdir()){
                System.out.println("Couldn't create directory results, exiting...");
                System.exit(-1);
            }
        }
        StringBuilder sb = new StringBuilder();

        for(List<ParticleDTO> snapshot: snapshots){
            sb.append(snapshots.get(0).size()).append("\n\n");
            for(ParticleDTO dto: snapshot){
                double direction = dto.getDirection() < 0 ?  2*Math.PI - Math.abs(dto.getDirection()) : dto.getDirection();
                double colorProportion = direction < Math.PI ? direction / Math.PI : (direction-Math.PI) / Math.PI;

                double redProportion = direction < Math.PI ? 1 - colorProportion : colorProportion;
                double greenProportion = 0;
                double blueProportion = 1 - redProportion;

                sb.append(dto.getPosX()).append(" ").append(dto.getPosY()).append(" ").append(direction).append(" ").append(redProportion).append(" ").append(greenProportion).append(" ").append(blueProportion).append("\n");
            }
        }
        try {
            FileWriter fw = new FileWriter(RESULTS_DIRECTORY + fileName);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
