import java.io.*;
public class ColorHistogram {
    
    private double[] histogram;
    private int depth;

    public ColorHistogram(int d){
        this.depth = d;
        int binCount = (int) Math.pow(2, depth * 3);
        this.histogram = new double[binCount]; 
    }

    public ColorHistogram(String filename){
        int binCount = 0;

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line = reader.readLine();
            binCount = Integer.parseInt(line.trim());
            this.histogram = new double[binCount];
            this.depth = (int)(Math.log(binCount) / Math.log(2) / 3);

            int index = 0;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split("\\s+");
                for (String number : numbers) {
                    if (!number.trim().isEmpty()) {
                        try {
                            histogram[index++] = Double.parseDouble(number.trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number from string: \"" + number + "\"");
                            throw e; // Rethrow the exception 
                        }
                    }
                }
            }

        }catch (IOException d){
            d.printStackTrace();
        }
    }
    
    public void setImage(ColorImage image){
        int binCount = (int) Math.pow(2, depth * 3);
        this.histogram = new double[binCount];

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
        for(int i = 0; i < binCount; i++){ //normalization
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
            double binIntersection = Math.min(hist1[i], hist2[i]);
            intersection += binIntersection;
        }
        return intersection;
    }

    public void save(String filename) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(String.valueOf(depth));
            writer.newLine();
            for(double binValue : histogram){
                writer.write(String.valueOf(binValue));
                writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }