import java.io.IOException;
public class HistogramTester {
    public static void main(String[] args) {

            // Assuming the depth has been reduced already within the ColorImage constructor
            int depth = 3; // This should match the depth used in the ColorImage class

            // Load two PPM images as ColorImage objects
            ColorImage image1 = new ColorImage("q01.ppm",3);
            ColorImage image2 = new ColorImage("q02.ppm",3);

            // Create histograms for both images
            ColorHistogram histogram1 = new ColorHistogram(depth);
            histogram1.setImage(image1);
            ColorHistogram histogram2 = new ColorHistogram(depth);
            histogram2.setImage(image2);

            // Print the histogram values for the first image
            System.out.println("Histogram for Image 1:");
            double[] histValues1 = histogram1.getHistogram();
            for (int i = 0; i < histValues1.length; i++) {
                System.out.printf("Bin %d: %f\n", i, histValues1[i]);
            }

            // Print the histogram values for the second image
            System.out.println("\nHistogram for Image 2:");
            double[] histValues2 = histogram2.getHistogram();
            for (int i = 0; i < histValues2.length; i++) {
                System.out.printf("Bin %d: %f\n", i, histValues2[i]);
            }

            // Compare the two histograms and print the intersection
            double intersection = histogram1.compare(histogram2);
            System.out.println("\nIntersection between Image 1 and Image 2: " + intersection);

            try {
                histogram1.save("/Users/kelly/Desktop/CSI2120/Project/similarity-search/histogram1.txt");
                System.out.println("Histogram 1 saved to file.");
                histogram2.save("/Users/kelly/Desktop/CSI2120/Project/similarity-search/histogram2.txt");
                System.out.println("Histogram 2 saved to file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
