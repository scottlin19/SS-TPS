package ar.edu.itba.ss.commons.writers;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum OutputTypeEnum {

    JSON,EXYZ
//
//    EXYZ{
//        @Override
//        public void createFile(SimulationResult result, String outPath)  {
//            List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
////            StringBuilder sb = new StringBuilder();
//            List<List<Particle>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList());
////            for(List<Particle> snapshot: snapshots){
////                sb.append(snapshots.get(0).size()).append("\n\n");
////                for(Particle p: snapshot){
////                    sb.append(p.getPosX()).append(" ").append(p.getPosY()).append(" ").append(p.getVelX()).append(" ").append(p.getVelY()).append(" ").append(p.getMass()).append(" ").append(p.getRadius()).append("\n");
////                }
////            }
//            try(final BufferedWriter writer = new BufferedWriter(new FileWriter(addExtension(outPath)))){
//                for(List<Particle> snapshot: snapshots){
//                    writer.write(snapshots.get(0).size()+"\n\n");
//                    for(Particle p: snapshot){
//
//                        writer.write(p.getPosX()+" "+
//                                        p.getPosY()+" "+
//                                        p.getVelX()+" "+
//                                        p.getVelY()+" "+
//                                        p.getMass()+" "+
//                                        p.getRadius()+" "+
//                                        p.getColor().getRed()+" "+
//                                        p.getColor().getGreen()+" "+
//                                        p.getColor().getBlue()+"\n");
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @Override
//        public String addExtension(String outPath) {
//            return outPath.contains(".exyz") ? outPath : outPath+".exyz";
//        }
//    },
//    JSON{
//        @Override
//        public void createFile(T result,String outPath) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.writeValue(new File(addExtension(outPath)), result);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        @Override
//        public String addExtension(String outPath) {
//            return outPath.contains(".json") ? outPath : outPath+".json";
//        }
//    };
//
//    public abstract void createFile(SimulationResult result,String outPath,Class<?> resultClass);
//    public abstract String addExtension(String outPath);

}
