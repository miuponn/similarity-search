import java.io.*;

public class ColorImage{

    private int depth;
    private int width, height;
    private int [][][] pixels; //3d array of pixels(RGB)

    public ColorImage(String filename, int depth){
        this.depth = depth;
        try (BufferedReader readPPM = new BufferedReader(new FileReader(filename))){
            
            readPPM.readLine(); //skip P3
            String line;
            
            boolean foundDimensions = false;
            while((line = readPPM.readLine()) != null){
                line = line.trim();
                if(!line.startsWith("#") && !line.isEmpty()){ //skip comments
                    if(!foundDimensions){
                        String[] dimensions = line.split("\\s+");
                        width = Integer.parseInt(dimensions[0]);
                        height = Integer.parseInt(dimensions[1]);
                        foundDimensions = true;
                        readPPM.readLine(); //skip max color value 
                        break;}
                }
            }
            
            if(!foundDimensions){
                throw new IOException("Dimensions not found in PPM file.");
            }

            pixels = new int[height][width][3]; //initialize pixels array
            
            int pixelCount = 0;

            while((line = readPPM.readLine()) != null && pixelCount < width * height){
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
            }catch (IOException e){
                e.printStackTrace();
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
        int rightShift = depth - d;
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

