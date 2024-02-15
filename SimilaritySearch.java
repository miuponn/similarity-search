/**
 * Student ID: 300245890, 300283847
 * Group Members: Kelly Gao, Sanika Sisodia
 */

import java.io.*;
import java.util.*;

public class SimilaritySearch {

    public static void main(String[] args) {
        
        if(args.length < 2){ //check if args are not correct format
            System.out.println("Usage: java SimilaritySearch <queryImage> <dataDirectory>");
            return; //print message and exit method
        }
        String queryImageFile = args[0]; //extract query image file
        String dataDirectory = args[1]; //extract data directory 

        ColorImage queryImage = new ColorImage(queryImageFile, 3); //create new color image for query image
        queryImage.reduceColor(3); //reduce depth of color image to 3
            
        ColorHistogram queryHistogram = new ColorHistogram(3); //create new histogram object for query image
        queryHistogram.setImage(queryImage); //generate pixel data for histogram from query image

        Map<Double, String> similarImagesMap = new HashMap<>(); //hashmap to store similarity scores, file name (key, value)

        File directorySet = new File(dataDirectory);  //create file object for directory set
        File[] directoryListing = directorySet.listFiles(); //retrieve list of files from directory

        if(directoryListing != null){ //check if director exists
            for(File child : directoryListing){ //iterate over each file in directory
                if(child.isFile() && child.getName().toLowerCase().endsWith(".txt")){ //check if txt file
                    ColorHistogram datasetHistogram = new ColorHistogram(child.getAbsolutePath()); //create new histogram object for each file

                    double score = queryHistogram.compare(datasetHistogram); //calculate similarity score between query file and directory file

                    similarImagesMap.put(score, child.getName().replace(".txt", "")); //add score and file name to hashmap
                    }
                }
            }

        similarImagesMap.entrySet().stream() //stream entries of hashmap 
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //sorts based on similarity score
            .limit(5) //set max files to 5
            .forEach(entry -> System.out.println(entry.getValue() + ": " + entry.getKey())); //print 5 most similar entries
    }
}

