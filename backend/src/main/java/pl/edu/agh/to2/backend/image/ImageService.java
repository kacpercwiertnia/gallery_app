package pl.edu.agh.to2.backend.image;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.queue.QueueService;

import java.util.Base64;
import java.util.List;


@Service
public class ImageService {
    private final QueueService queueService;
    private final ImageRepository imageRepository;


    public ImageService(QueueService queueService, ImageRepository imageRepository) {
        this.queueService = queueService;
        this.imageRepository = imageRepository;
    }

    public void addNewImage(String encodedImage) {
        byte[] byteImage = Base64.getDecoder().decode(encodedImage);
        System.out.println(byteImage.length);
        Image image = new Image(byteImage, 2137);
        imageRepository.save(image);
        queueService.queueImages(image);
    }

}
