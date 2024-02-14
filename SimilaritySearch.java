import java.io.*;
import java.util.*;

public class SimilaritySearch {

    public static void main(String[] args) {
        
        if(args.length < 2){
            System.out.println("Usage: java SimilaritySearch <queryImage> <dataDirectory>");
            return;
        }
        String queryImageFile = args[0];
        String dataDirectory = args[0];

        ColorImage queryImage = new ColorImage(queryImageFile, 3);
        queryImage.reduceColor(3);
            
        ColorHistogram queryHistogram = new ColorHistogram(3);
        queryHistogram.setImage(queryImage);

        Map<Double, String> similarImagesMap = new HashMap<>();

        File directorySet = new File(dataDirectory);
        File[] directoryListing = directorySet.listFiles();

        if(directoryListing != null){
            for(File child : directoryListing){
                if(child.isFile() && child.getName().toLowerCase().endsWith(".txt")){
                    ColorHistogram datasetHistogram = new ColorHistogram(child.getAbsolutePath());

                    double score = queryHistogram.compare(datasetHistogram);

                    similarImagesMap.put(score, child.getName().replace(".txt", ""));
                    }
                }
            }

        similarImagesMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5) 
            .forEach(entry -> System.out.println(entry.getValue() + ": " + entry.getKey()));
    }
}

