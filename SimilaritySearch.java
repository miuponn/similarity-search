import java.io.*;
import java.util.*;

public class SimilaritySearch {
    public static void main(String[] args) {
        // Assuming args[0] is the query image filename and args[1] is the dataset directory
        String queryImageFilename = "/Users/kelly/Desktop/CSI2120/Project/queryImages/q04.ppm";
        String datasetDirectory = "/Users/kelly/Desktop/CSI2120/Project/imageDataset2_15_20";
        int depth = 3; // This could be passed as an argument or set in your main method

        try {
            // Read and preprocess the query PPM image
            ColorImage queryImage = new ColorImage(queryImageFilename);
            queryImage.reduceColor(depth);

            // Compute the histogram of the query image
            ColorHistogram queryHistogram = new ColorHistogram(depth);
            queryHistogram.setImage(queryImage);

            // Map to store similarity scores with corresponding image file names
            Map<Double, String> similarityScores = new TreeMap<>(Collections.reverseOrder());

            // Process the pre-computed histogram .txt files in the dataset directory
            File dir = new File(datasetDirectory);
            File[] histogramFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));

            if (histogramFiles != null) {
                for (File histFile : histogramFiles) {
                    // Read the histogram from the .txt file
                    ColorHistogram datasetHistogram = new ColorHistogram(histFile.getAbsolutePath());

                    // Compute the similarity score between histograms
                    double similarity = queryHistogram.compare(datasetHistogram);

                    // The .txt file is expected to correspond to a .jpg image in the same directory
                    String imageFileName = histFile.getName().replaceAll("\\.txt$", ".jpg");
                    similarityScores.put(similarity, imageFileName);
                }
            }

            // Print the top 5 most similar images
            System.out.println("Top 5 similar images to " + queryImageFilename + ":");
            int count = 0;
            for (Map.Entry<Double, String> entry : similarityScores.entrySet()) {
                if (count >= 5) break;
                System.out.println("Similarity: " + entry.getKey() + " - Image name: " + entry.getValue());
                count++;
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

