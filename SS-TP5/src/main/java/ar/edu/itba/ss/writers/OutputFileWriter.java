package ar.edu.itba.ss.writers;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface OutputFileWriter<T> {
     void createFile(T result,String outPath);
     String addExtension(String outPath);

     default void createDirs(String outPath){
          List<String> path = Arrays.stream(outPath.split("/")).collect(Collectors.toList());
          // saco archivo
          path.remove(path.size() - 1);
          String current_path = "";
          for(String dir : path){
               current_path = current_path.concat(dir + "/");
               Path p = Paths.get(current_path);
               try {
                    Files.createDirectories(p);
               } catch (FileAlreadyExistsException e){
                    System.out.println(current_path + " exists");
               } catch (IOException e) {
                    e.printStackTrace();
               }
          }
     }
}
