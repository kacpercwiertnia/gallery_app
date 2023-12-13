package pl.edu.agh.to2.backend.image;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.queue.QueueService;

import java.util.Base64;
import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

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

    public String getImages() {
        List<Image> images = imageRepository.findAll();
        String encoded = Base64.getEncoder().encodeToString(images.get(0).getSource());
        return encoded;
    }
}
