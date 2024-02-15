/**
 * Student ID: 300245890, 300283847
 * Group Members: Kelly Gao, Sanika Sisodia
 */

 import java.io.*;

public class ColorImage{

    private int depth;
    private int width, height;
    private int [][][] pixels; //3d array of pixels(RGB)

    public ColorImage(String filename, int depth){
        this.depth = depth; //depth
        try (BufferedReader readPPM = new BufferedReader(new FileReader(filename))){ //open file
            
            readPPM.readLine(); //skip 'P3' 
            String line; //variable to hold line
            
            boolean foundDimensions = false; //check for dimensions boolean
            while((line = readPPM.readLine()) != null){ 
                line = line.trim();
                if(!line.startsWith("#") && !line.isEmpty()){ //skip comments
                    if(!foundDimensions){ //read lines until dimensions are found
                        String[] dimensions = line.split("\\s+");
                        width = Integer.parseInt(dimensions[0]);
                        height = Integer.parseInt(dimensions[1]);
                        foundDimensions = true;
                        readPPM.readLine(); //skip max color value 
                        break;}
                }
            }
            
            if(!foundDimensions){ //throw exception if not found
                throw new IOException("Dimensions not found in PPM file.");
            }

            pixels = new int[height][width][3]; //initialize pixels array
            
            int pixelCount = 0; //pixel counter

            while((line = readPPM.readLine()) != null && pixelCount < width * height){ //read rgb values from lines
                String[] rgb = line.trim().split("\\s+");
                for(int k = 0; k < rgb.length; k += 3){
                    int i = pixelCount / width;
                    int j = pixelCount % width;

                    pixels[i][j][0] = Integer.parseInt(rgb[k]); // R
                    pixels[i][j][1] = Integer.parseInt(rgb[k + 1]); // G
                    pixels[i][j][2] = Integer.parseInt(rgb[k + 2]); // B
                    pixelCount++;

                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found - " + filename);
            } catch (IOException e) {
                System.err.println("Error: IO exception while reading file - " + filename);
            } catch (NumberFormatException e) {
                System.err.println("Error: Number format exception while parsing image data");
            }
        }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public int getDepth(){
        return this.depth;
    }

    public int[] getPixel(int i, int j){
        return pixels[i][j]; 
    }

    public void reduceColor(int d){
        int rightShift = depth - d; //right shift 
        this.depth = d;

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                pixels[i][j][0] >>= rightShift; //reduce R
                pixels[i][j][1] >>= rightShift; //reduge G
                pixels[i][j][2] >>= rightShift; //reduce B
            }
        }
    }
}

