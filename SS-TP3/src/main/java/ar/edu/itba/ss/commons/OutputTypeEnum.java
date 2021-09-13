package ar.edu.itba.ss.commons;
import ar.edu.itba.ss.brownian_motion.SimulationResult;
import ar.edu.itba.ss.grid.Particle;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum OutputTypeEnum {

    EXYZ{
        @Override
        public void createFile(SimulationResult result,String outPath)  {
            List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
            StringBuilder sb = new StringBuilder();
            List<List<Particle>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList());
            for(List<Particle> snapshot: snapshots){
                sb.append(snapshots.get(0).size()).append("\n\n");
                for(Particle p: snapshot){
                    sb.append(p.getPosX()).append(" ").append(p.getPosY()).append(" ").append(p.getVelX()).append(" ").append(p.getVelY()).append(" ").append(p.getMass()).append(" ").append(p.getRadius()).append("\n");
                }
            }
            try(final BufferedWriter writer = new BufferedWriter(new FileWriter(addExtension(outPath)))){

                for(List<Particle> snapshot: snapshots){
                    writer.write(snapshots.get(0).size()+"\n\n");
                    for(Particle p: snapshot){
                        writer.write(p.getPosX()+" "+
                                        p.getPosY()+" "+
                                        p.getVelX()+" "+
                                        p.getVelY()+" "+
                                        p.getMass()+" "+
                                        p.getRadius()+"\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        @Override
        public String addExtension(String outPath) {
            return outPath+".exyz";
        }
    },
    JSON{
        @Override
        public void createFile(SimulationResult result,String outPath) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(new File(addExtension(outPath)), result);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public String addExtension(String outPath) {
            return outPath+".json";
        }


    };

    public abstract void createFile(SimulationResult result,String outPath);
    public abstract String addExtension(String outPath);

}
