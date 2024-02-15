/**
 * Student ID: 300245890, 300283847
 * Group Members: Kelly Gao, Sanika Sisodia
 */

import java.io.*;
public class ColorHistogram {
    
    private double[] histogram; //double to store histogram
    private int depth; //depth

    public ColorHistogram(int d){ //constructor with depth
        this.depth = d;
        int binCount = (int) Math.pow(2, depth * 3); //bin count of histogram with depth
        this.histogram = new double[binCount]; //initialize histogram double with bincount
    }

    public ColorHistogram(String filename){ //constructor with filename
        int binCount = 0; //initalize bin count of histogram

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){ //open file for reading
            String line = reader.readLine(); //read first line 
            binCount = Integer.parseInt(line.trim()); //parses line for # of bins and assigns to bin count
            this.histogram = new double[binCount]; //initalize histogram double with bincount
            this.depth = (int)(Math.log(binCount) / Math.log(2) / 3); //calculate and set depth based on bin count

            int index = 0; //index counter
            while ((line = reader.readLine()) != null) { //loop through remaining lines of file
                String[] numbers = line.split("\\s+"); //splits strings into arrays without whitespace
                for (String number : numbers) { //iterate over each string in array
                    if (!number.trim().isEmpty()) {  //check if string is empty
                        try {
                            histogram[index++] = Double.parseDouble(number.trim()); //attempt to parse string as double
                        } catch (NumberFormatException e) { //number format exception if parsing failed
                            System.err.println("Error parsing number from string: \"" + number + "\"");
                            throw e; // Rethrow the exception 
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) { //more exception handling
            System.err.println("Error: File not found - " + filename);
        } catch (IOException e) {
            System.err.println("Error: IO exception while reading file - " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error: Number format exception while parsing histogram data");
        }
    }
    
    public void setImage(ColorImage image){
        int binCount = (int) Math.pow(2, depth * 3); //recalculate bin count based on depth
        this.histogram = new double[binCount]; //initalize histogram with bin count

        int width = image.getWidth(); //retrieve image dimensions
        int height = image.getHeight();
        int pixels = width * height; //calculates total # of pixels

        for (int i = 0; i < height; i++) { //iterate over each pixel in image
            for (int j = 0; j < width; j++) {
                int[] pixel = image.getPixel(i, j); //retrieve RGB of current pixel
                int binIndex = ((pixel[0] >> (8 - depth)) << (2 * depth)) + //calculates index of pixel using RGB and depth
                                ((pixel[1] >> (8 - depth)) << depth) +
                                (pixel[2] >> (8 - depth));
                histogram[binIndex]++; //increments value of histogram at calcualted binIndex
            }
        }
        for(int i = 0; i < binCount; i++){ //normalization
            histogram[i] /= pixels; 
        }

    }

    public double[] getHistogram(){
        return histogram;
    }

    public double compare(ColorHistogram hist){
        double[] hist1 = this.getHistogram(); //retrive current histogram
        double[] hist2 = hist.getHistogram(); //retrive parameter histogram
    
        double intersection = 0.0; //initialize intersection accumulator double 
        for (int i = 0; i < hist1.length; i++) { //iterate over each bin in hist1
            double binIntersection = Math.min(hist1[i], hist2[i]); //compute min value between bin in hist1 and bin in hist2
            intersection += binIntersection; //add current intersection to accumulator
        }
        return intersection; //return final intersection
    }

    public void save(String filename) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){ //try-with-resources
            writer.write(String.valueOf(depth)); //write depth to file 
            writer.newLine(); //newline to file
            for(double binValue : histogram){ //iterate over each value in histogram
                writer.write(String.valueOf(binValue)); //write each value to file
                writer.newLine(); //newline to file
                }
            } catch (IOException e) { //catch block for io
                e.printStackTrace();
            }
        }
    }