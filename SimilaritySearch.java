import java.io.*;
import java.util.*;

public class SimilaritySearch {

    public static void main(String[] args) {
        int depth = 3;
        String queryImageFile = "C:\\Users\\KELLY\\OneDrive\\Documents\\GitHub\\similarity-search\\q02.ppm";
        String dataDirectory = "C:\\Users\\KELLY\\OneDrive\\Documents\\GitHub\\similarity-search\\imageDataset2_15_20";
        

            ColorImage queryImage = new ColorImage(queryImageFile, depth);
            queryImage.reduceColor(depth);
            
            ColorHistogram queryHistogram = new ColorHistogram(depth);
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

