package gui;

import java.util.Random;

public class ColorRandomizer {
    Random r, g, b;
    public ColorRandomizer(){
        this.r=new Random();
        this.g=new Random();
        this.b=new Random();
    }
    public int[] getRandomColor(){
        int[] rgb=new int[3];
        rgb[0]= r.nextInt(255-0)+0;
        rgb[1]= g.nextInt(255-0)+0;
        rgb[2]= b.nextInt(255-0)+0;
        return rgb;
    }
}
