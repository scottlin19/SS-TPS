package ar.edu.itba.ss.commons.writers;

public interface OutputFileWriter<T> {
     void createFile(T result,String outPath);
     String addExtension(String outPath);
}
