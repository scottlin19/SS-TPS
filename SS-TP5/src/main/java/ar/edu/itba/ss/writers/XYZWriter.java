package ar.edu.itba.ss.writers;

import ar.edu.itba.ss.commons.PedestrianDTO;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.Wall;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class XYZWriter implements OutputFileWriter<SimulationResult> {

    @Override
    public void createFile(SimulationResult result,String outPath)  {
        createDirs(outPath);
        List<SimulationSnapshot> simulationSnapshots = result.getSnapshots();
        List<List<PedestrianDTO>> snapshots = simulationSnapshots.stream().map(SimulationSnapshot::getPedestrians).collect(Collectors.toList());
        try(final BufferedWriter pedestrian_writer = new BufferedWriter(new FileWriter(addExtension(outPath)));
            final BufferedWriter walls_writer = new BufferedWriter(new FileWriter(addExtension(wallsExtension(outPath))))){
            for(List<PedestrianDTO> snapshot: snapshots){
                pedestrian_writer.write(snapshot.size()+"\n\n");
                for(PedestrianDTO p: snapshot){

                    pedestrian_writer.write(p.getX()+" "+
                            p.getY()+" "+
                            p.getRadius() + "\n");

                }
            }

            List<Point2D.Double> firstPoints = result.getWalls().stream().map(Wall::getP1).collect(Collectors.toList());
            List<Point2D.Double> secondPoints = result.getWalls().stream().map(Wall::getP2).collect(Collectors.toList());
            walls_writer.write(firstPoints.size()+"\n\n");

            for(Point2D.Double point : firstPoints){

                walls_writer.write(
                    point.x + " " + point.y + "\n"
                );
            }
            walls_writer.write(secondPoints.size()+"\n\n");
            for(Point2D.Double point : secondPoints){
                walls_writer.write(
                    point.x + " " + point.y + "\n"
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String addExtension(String outPath) {
        return outPath.contains(".exyz") ? outPath : outPath + ".exyz";
    }

    public String wallsExtension(String outPath){
        return outPath + "Walls";
    }
}
