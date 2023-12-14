package pl.edu.agh.to2.backend.thumbnail;

import javax.imageio.ImageIO;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageScaler {
    public byte[] scaleImage(byte[] image, int size) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(image);
        BufferedImage imageFromByteArray = ImageIO.read(inStream);
        int xStart = 0;
        int yStart = 0;
        double ratio = 1.0;
        var height = imageFromByteArray.getHeight();
        var width = imageFromByteArray.getWidth();

        if (height > width) {
            ratio = (double) size / height;
            xStart = (int) (size - ratio * width) / 2;
        } else if (width > height) {
            ratio = (double) size / width;
            yStart = (int) (size - ratio * height) / 2;
        }

        Image scaledImage = imageFromByteArray.getScaledInstance((int) (width * ratio), (int) (height * ratio), Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, xStart, yStart, null);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", outStream);
        return outStream.toByteArray();
    }
}
