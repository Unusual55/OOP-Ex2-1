package gui;

import java.util.Random;

/**
 * This class is a color generator, it support the Color Frenzy Mode.
 */
public class ColorRandomizer {
    Random r, g, b;
    public ColorRandomizer(){
        this.r=new Random();
        this.g=new Random();
        this.b=new Random();
    }

    /**
     * This function create 3 random number between 0-255 and return an array with 3 cells which containg the
     * randomized values as rgb.
     * @return
     */
    public int[] getRandomColor(){
        int[] rgb=new int[3];
        rgb[0]= r.nextInt(255-0)+0;
        rgb[1]= g.nextInt(255-0)+0;
        rgb[2]= b.nextInt(255-0)+0;
        return rgb;
    }
}
