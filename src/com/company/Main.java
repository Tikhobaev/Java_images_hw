package com.company;
import java.util.Scanner;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        ArrayList<Image> images = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            Scanner in = new Scanner(System.in);
            System.out.println("Enter a path for file");
            String path = in.next();

            try {
                Image im = new Image(path);
                System.out.println(im);
                images.add(im);
            } catch (ImageNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        images.sort(Image::compare);
        System.out.print(getDistance(images, images.get(0)));
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
