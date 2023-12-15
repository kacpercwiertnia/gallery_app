package pl.edu.agh.to2.backend.image;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.queue.QueueService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
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

    public void addNewImage(String[] encodedImages) throws IllegalArgumentException {

        try {
            List<Image> checkedImages = new ArrayList<>();
            for (String encodedImage: encodedImages){
                byte[] byteImage = Base64.getDecoder().decode(encodedImage);
                InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage));
                String mimetype = URLConnection.guessContentTypeFromStream(is);
                if (mimetype != "image/jpeg" && mimetype != "image/png" && mimetype != "image/jpg") {
                    throw new IllegalArgumentException("Not a valid image");
                }
                checkedImages.add(new Image(byteImage, 2137));
            }
            imageRepository.saveAll(checkedImages);
            queueService.queueImages(checkedImages);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

}
