package com.company;
import java.util.Scanner;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        ArrayList<Image> images = new ArrayList<>();
        String path = "";
        while (!path.equals("q")){
            Scanner in = new Scanner(System.in);
            System.out.println("Enter a path for file (enter q for exit)");
            path = in.next();
            if (path.equals("q"))
                break;
            try {
                Image im = new Image(path);
                System.out.println(im);
                images.add(im);
            } catch (ImageNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        images.sort(Image::compare);
        if (images.size() > 0){
            System.out.print(getDistance(images, images.get(0)));  // demo of the getDistance method usage
        }
    }

    public static int getDistance(ArrayList<Image> images, Image im){
        // calculate the mean distance between image and others images in ArrayList
        if (images.size() < 2){
            return 0;
        }
        int diff = 0;
        for(Image image : images){
            diff += im.compare(image);
        }
        return diff / (images.size() - 1);
    }
}
