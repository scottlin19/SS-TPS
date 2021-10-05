package ar.edu.itba.ss.commons.writers;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class XYZWriter implements OutputFileWriter<SimulationResult>{

    @Override
    public void createFile(SimulationResult result,String outPath)  {
        List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
        List<List<Particle>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getParticles).collect(Collectors.toList());
        try(final BufferedWriter writer = new BufferedWriter(new FileWriter(addExtension(outPath)))){
            for(List<Particle> snapshot: snapshots){
                writer.write(snapshots.get(0).size()+"\n\n");
                for(Particle p: snapshot){

                    writer.write(p.getPosX()+" "+
                            p.getPosY()+" "+
                            p.getVelX()+" "+
                            p.getVelY()+" "+
                            p.getMass()+" "+
                            p.getRadius()+" "+
                            p.getColor().getRed()+" "+
                            p.getColor().getGreen()+" "+
                            p.getColor().getBlue()+"\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String addExtension(String outPath) {
        return outPath.contains(".exyz") ? outPath : outPath+".exyz";
    }
}
