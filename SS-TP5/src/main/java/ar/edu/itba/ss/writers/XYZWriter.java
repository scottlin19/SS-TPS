package ar.edu.itba.ss.writers;

import ar.edu.itba.ss.commons.Pedestrian;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class XYZWriter implements OutputFileWriter<SimulationResult> {

    @Override
    public void createFile(SimulationResult result,String outPath)  {
        List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
        List<List<Pedestrian>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getPedestrians).collect(Collectors.toList());
        try(final BufferedWriter writer = new BufferedWriter(new FileWriter(addExtension(outPath)))){
            for(List<Pedestrian> snapshot: snapshots){
                writer.write(snapshot.size()+"\n\n");
                for(Pedestrian p: snapshot){

                    writer.write(p.getPosX()+" "+
                            p.getPosY()+" "+
                            p.getRadius());

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
