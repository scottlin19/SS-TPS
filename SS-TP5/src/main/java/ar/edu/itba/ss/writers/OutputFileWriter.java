package ar.edu.itba.ss.writers;

public interface OutputFileWriter<T> {
     void createFile(T result,String outPath);
     String addExtension(String outPath);
}
