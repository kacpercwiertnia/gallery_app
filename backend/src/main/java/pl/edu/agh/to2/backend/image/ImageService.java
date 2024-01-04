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
import java.util.Optional;
import java.util.Set;

@Service
public class ImageService {
    private final QueueService queueService;
    private final ImageRepository imageRepository;
    private final Set<String> ACCEPTED_FORMATS;

    public ImageService(QueueService queueService, ImageRepository imageRepository) {
        this.queueService = queueService;
        this.imageRepository = imageRepository;
        ACCEPTED_FORMATS = Set.of("image/jpeg", "image/png", "image/jpg");
    }

    public void addNewImages(String[] encodedImages) throws IllegalArgumentException {
        List<Image> checkedImages = new ArrayList<>();
        for (String encodedImage : encodedImages) {
            byte[] byteImage = Base64.getDecoder().decode(encodedImage);
            try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage))) {
                String mimetype = URLConnection.guessContentTypeFromStream(is);
                if (!ACCEPTED_FORMATS.contains(mimetype)) {
                    throw new IllegalArgumentException("Not a valid image");
                }
                checkedImages.add(new Image(byteImage));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        imageRepository.saveAll(checkedImages);
        queueService.queueImages(checkedImages);

    }

    public List<Integer> getImagesIds() {
        return imageRepository.findAll()
                .stream()
                .map(Image::getImageId)
                .toList();
    }

    public String getImageById(int id) throws IllegalArgumentException {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return Base64.getEncoder().encodeToString(image.get().getSource());
        }
        throw new IllegalArgumentException("No image with given ID");


    }
}
