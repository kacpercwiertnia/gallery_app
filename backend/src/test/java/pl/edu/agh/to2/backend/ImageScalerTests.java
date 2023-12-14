package pl.edu.agh.to2.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.edu.agh.to2.backend.thumbnail.ImageScaler;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class ImageScalerTests {
    static Path resourceDirectory = Paths.get("src","test","resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();
    static byte[] image;
    static ImageScaler imageScaler;

    @BeforeAll
    static void setup(){
        System.out.println("startup");

        try {
            image = Files.readAllBytes(Paths.get(absolutePath+"/image1.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageScaler = new ImageScaler();
    }

    @Test
    public void checkScalingToSmall(){
        //given
        ThumbnailSize size = ThumbnailSize.SMALL;

        //when
        int height;
        int width;

        try {
            byte[] outputImage = imageScaler.scaleImage(image, size.getSize());

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputImage);
            BufferedImage imageFromByteArray = ImageIO.read(inStream);

            height = imageFromByteArray.getHeight();
            width = imageFromByteArray.getWidth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //then
        Assertions.assertEquals(height,size.getSize());
        Assertions.assertEquals(width,size.getSize());
    }

    @Test
    public void checkScalingToMedium(){
        //given
        ThumbnailSize size = ThumbnailSize.MEDIUM;

        //when
        int height;
        int width;

        try {
            byte[] outputImage = imageScaler.scaleImage(image, size.getSize());

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputImage);
            BufferedImage imageFromByteArray = ImageIO.read(inStream);

            height = imageFromByteArray.getHeight();
            width = imageFromByteArray.getWidth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //then
        Assertions.assertEquals(height,size.getSize());
        Assertions.assertEquals(width,size.getSize());
    }

    @Test
    public void checkScalingToLarge(){
        //given
        ThumbnailSize size = ThumbnailSize.LARGE;

        //when
        int height;
        int width;

        try {
            byte[] outputImage = imageScaler.scaleImage(image, size.getSize());

            ByteArrayInputStream inStream = new ByteArrayInputStream(outputImage);
            BufferedImage imageFromByteArray = ImageIO.read(inStream);

            height = imageFromByteArray.getHeight();
            width = imageFromByteArray.getWidth();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //then
        Assertions.assertEquals(height,size.getSize());
        Assertions.assertEquals(width,size.getSize());
    }
}
