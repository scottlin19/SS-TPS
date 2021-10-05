//package ar.edu.itba.ss.commons.writers;
//
//import java.io.*;
//
//public class OutputFile<T> {
//    public static final int DEFAULT_BUFFER_SIZE = 4096*2;
//
//    private static final String RESULTS_DIRECTORY = "results/";
//
//    private XYZWriter XYZwriter;
//    private JSONWriter<T> jsonWriter;
//
//    public OutputFile(){
//        this.XYZwriter = new XYZWriter();
//        this.jsonWriter  =new JSONWriter<>();
//    }
//
//    public static void createOutputFile(T result, String outPath, OutputTypeEnum outputType) {
//        File directory = new File(RESULTS_DIRECTORY);
//        if (!directory.exists()) {
//            if (!directory.mkdir()) {
//                System.out.println("Couldn't create directory results, exiting...");
//                System.exit(-1);
//            }
//        }
//        switch(outputType){
//            case JSON:
//
//                break;
//            case EXYZ:
//
//                break;
//        }
//            outputType.createFile(result,RESULTS_DIRECTORY +outPath);
//
//    }
//
//}
