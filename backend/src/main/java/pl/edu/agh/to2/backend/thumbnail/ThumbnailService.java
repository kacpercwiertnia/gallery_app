package pl.edu.agh.to2.backend.thumbnail;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.ImageRepository;
import pl.edu.agh.to2.backend.queue.QueueRepository;
import pl.edu.agh.to2.backend.queue.QueueService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ThumbnailService {
    private ThumbnailRepository thumbnailRepository;
    private QueueRepository queueRepository;
    private ImageRepository imageRepository;

    public ThumbnailService(ThumbnailRepository thumbnailRepository, QueueRepository queueRepository, ImageRepository imageRepository){
        this.thumbnailRepository = thumbnailRepository;
        this.queueRepository = queueRepository;
        this.imageRepository = imageRepository;
    }

    //think about moving this all somewhere else, as this does not seem to belong here (the scaling part)
    @Transactional
    public void resizeImageFromQueue(){
        var queuedImages = queueRepository.findAll();
        if (queuedImages.size() == 0){
            System.out.println("No records"); //maybe just exception
            return;
        }

        var queue = queuedImages.get(0);
        var image = queue.getImage();

        for(ThumbnailSize size: ThumbnailSize.values()){ //make it multithread maybe
            try{
                var scaledImage = scaleImage(image.getSource(), size.getSize());
                var thumbnail = new Thumbnail(scaledImage, size, image);
                thumbnailRepository.save(thumbnail);
                image.removeFromQueue();
                imageRepository.save(image);
                queueRepository.delete(queue);
            }catch(IOException ex){
                System.out.println("Operation failed for image with ID: " + image.getImageId() + " and size: " + size);
            }
        }

    }
    private byte[] scaleImage(byte[] image, int size) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(image);
        BufferedImage imageFromByteArray = ImageIO.read(inStream);

        Image scaledImage = imageFromByteArray.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(scaledImage, 0, 0, null);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", outStream);
        return outStream.toByteArray();
    }
}
