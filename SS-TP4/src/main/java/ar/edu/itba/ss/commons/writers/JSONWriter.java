package ar.edu.itba.ss.commons.writers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JSONWriter<T> implements OutputFileWriter<T>{
    @Override
    public void createFile(T result,String outPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(addExtension(outPath)), result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String addExtension(String outPath) {
        return outPath.contains(".json") ? outPath : outPath+".json";
    }
}
