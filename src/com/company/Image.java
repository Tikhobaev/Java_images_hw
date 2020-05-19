package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static java.lang.Math.abs;

public class Image {
    private String path;
    private FileTime creationTime;
    private FileTime lastAccessTime;
    private FileTime lastModifiedTime;
    private int height;
    private int width;
    private double meanRed;
    private double meanGreen;
    private double meanBlue;
    private double sizeKB;
    private double eps;  // to compare double values

    public Image(String path) throws ImageNotFoundException{
        eps = .0001;
        this.path = path;
        File imageFile = new File(path);
        sizeKB = imageFile.length() / 1024.;
        Path file = Paths.get(path);
        BufferedImage image;
        BasicFileAttributes attr;
        try {
            image = ImageIO.read(imageFile);
            attr = Files.readAttributes(file, BasicFileAttributes.class);
        } catch (IOException e) {
            throw new ImageNotFoundException("Image " + path + " not found");
        }
        height = image.getHeight();
        width = image.getWidth();
        long red = 0;
        long green = 0;
        long blue = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                red += c.getRed();
                green += c.getGreen();
                blue += c.getBlue();

            }
        }
        meanRed = red * 1.0 / (height*width);
        meanBlue = blue * 1.0 / (height*width);
        meanGreen = green * 1.0 / (height*width);
        creationTime = attr.creationTime();
        lastAccessTime = attr.lastAccessTime();
        lastModifiedTime = attr.lastModifiedTime();
    }

    public int compare(Image im){
        double colorDiff = meanRed + meanBlue + meanGreen - im.meanRed - im.meanBlue - im.meanGreen;
        double sizeKBDiff = sizeKB - im.sizeKB;
        double sizeDiff = height*width - im.height*im.width;
        int createdEarlier = Integer.compare(creationTime.compareTo(im.creationTime), 0);
        int lastAccessEarlier = Integer.compare(lastAccessTime.compareTo(im.lastAccessTime), 0);
        int modifiedEarlier = Integer.compare(lastModifiedTime.compareTo(im.lastModifiedTime), 0);

        if (abs(colorDiff) > eps){
            return (int)colorDiff;
        }
        if (abs(sizeKBDiff) > eps){
            return (int)(sizeKBDiff / (255 * 3 * 0.5));  // prioritize the value of color difference
        }
        if (abs(sizeDiff) > eps){
            return (int)(sizeDiff / (255 * 3 * 0.25));
        }
        return (createdEarlier + lastAccessEarlier + modifiedEarlier);
    }

    @Override
    public boolean equals(Object im) {
        // self check
        if (this == im)
            return true;
        // null check
        if (im == null)
            return false;
        // type check and cast
        if (getClass() != im.getClass())
            return false;
        Image image = (Image) im;
        return (meanRed + meanBlue + meanGreen - image.meanRed - image.meanBlue - image.meanGreen) < eps &&
                (sizeKB - image.sizeKB) < eps && (height*width - image.height*image.width) < eps &&
                creationTime.compareTo(image.creationTime) == 0 &&
                lastAccessTime.compareTo(image.lastAccessTime) == 0 &&
                lastModifiedTime.compareTo(image.lastModifiedTime) == 0;

    }

    @Override
    public String toString() {
        return "\nImage" +
               "\nfilename: " + path +
               "\nsize: " + sizeKB + "kB" +
               "\nresolution: " + height + "x" + width +
               "\nmean red component value(RGB): " + meanRed +
               "\nmean green component value(RGB): " + meanGreen +
               "\nmean blue component value(RGB): " + meanBlue +
               "\ncreation time: " + creationTime +
               "\nlast access time: " + lastAccessTime +
               "\nlast modified time: " + lastModifiedTime + "\n";
    }

}
