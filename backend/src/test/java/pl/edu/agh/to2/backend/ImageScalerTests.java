package pl.edu.agh.to2.backend;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.edu.agh.to2.backend.thumbnail.ImageScaler;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@TestPropertySource(properties = "app.scheduling.enable=false")
public class ImageScalerTests {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);
    static Path resourceDirectory = Paths.get("src","test","resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();
    static byte[] image;
    static ImageScaler imageScaler;

    @BeforeAll
    static void setup(){
        log.info("Starting tests");

        try {
            image = Files.readAllBytes(Paths.get(absolutePath+"/image1.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        imageScaler = new ImageScaler();
    }

    @Test
    public void checkScalingToSmallGivenImage(){
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
    public void checkScalingToMediumGivenImage(){
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
    public void checkScalingToLargeGivenImage(){
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
