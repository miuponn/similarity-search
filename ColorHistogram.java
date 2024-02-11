import java.io.*;
public class ColorHistogram {
    
    private double[] histogram;
    private int binCount;
    private int depth;

    public ColorHistogram(int d){
        this.depth = d;
        this.binCount = (int) Math.pow(2, depth * 3);
        this.histogram = new double[binCount]; 
    }

    public ColorHistogram(String filename) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line = reader.readLine();
            if(line == null){
                throw new IOException("Histogram file is empty.");
            }
            this.binCount = Integer.parseInt(line.trim());
            System.out.println("Reading histogram from " + filename + " with bin count: " + binCount); // Debugging
    
            this.histogram = new double[binCount];
            line = reader.readLine();
            if (line == null) {
                throw new IOException("Histogram values are missing.");
            }
                    
            String[] values = line.trim().split("\\s+");
            if(values.length != binCount){
                throw new IOException("Histogram data does not match bin count: expected " + binCount + ", got " + values.length);
            }
    
            for(int i = 0; i < binCount; i++){
                histogram[i] = Double.parseDouble(values[i]);
                // Print each histogram value as it's read
                System.out.println("Histogram[" + i + "]: " + histogram[i]);
            }
        } catch(NumberFormatException e) {
            throw new IOException("Invalid format in histogram file.", e);
        }
    }
    

    public void setImage(ColorImage image){
        int numBins = (int) Math.pow(2, depth * 3);
        this.histogram = new double[numBins];

        int width = image.getWidth();
        int height = image.getHeight();
        int pixels = width * height;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int[] pixel = image.getPixel(i, j);
                int binIndex = ((pixel[0] >> (8 - depth)) << (2 * depth)) +
                                ((pixel[1] >> (8 - depth)) << depth) +
                                (pixel[2] >> (8 - depth));
                histogram[binIndex]++;
            }
        }
        for(int i = 0; i < numBins; i++){ //normalization
            histogram[i] /= pixels;
        }

    }

    public double[] getHistogram(){
        return histogram;
    }

    public double compare(ColorHistogram hist){

        double[] hist1 = this.getHistogram();
        double[] hist2 = hist.getHistogram();

        double intersection = 0.0;
        for (int i = 0; i < hist1.length; i++) {
            intersection += Math.min(hist1[i], hist2[i]);
        }
        return intersection;
    }

    public void save(String filename) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(binCount + "\n");
            for(double bin: histogram){
                writer.write(bin +"\n");
            }
        }
    }
}