package pl.edu.agh.to2.backend.thumbnail;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.ImageRepository;
import pl.edu.agh.to2.backend.queue.QueueRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ThumbnailService {
    private final ThumbnailRepository thumbnailRepository;
    private final QueueRepository queueRepository;
    private final ImageRepository imageRepository;
    private final ImageScaler imageScaler;

    public ThumbnailService(ThumbnailRepository thumbnailRepository, QueueRepository queueRepository, ImageRepository imageRepository, ImageScaler imageScaler) {
        this.thumbnailRepository = thumbnailRepository;
        this.queueRepository = queueRepository;
        this.imageRepository = imageRepository;
        this.imageScaler = imageScaler;
    }

    @Transactional
    public void resizeImageFromQueue() {
        var queuedImages = queueRepository.findAll();
        if (queuedImages.size() == 0) {
            System.out.println("No records");
            return;
        }

        var queue = queuedImages.get(0);
        var image = queue.getImage();

        for (ThumbnailSize size : ThumbnailSize.values()) {
            try {
                var scaledImage = imageScaler.scaleImage(image.getSource(), size.getSize());
                var thumbnail = new Thumbnail(scaledImage, size, image);
                thumbnailRepository.save(thumbnail);
                image.removeFromQueue();
                imageRepository.save(image);
                queueRepository.delete(queue);
            } catch (IOException ex) {
                System.out.println("Operation failed for image with ID: " + image.getImageId() + " and size: " + size);
            }
        }
    }

    public List<String> getThumbnailsBySize(ThumbnailSize size) throws IllegalArgumentException {
        if (size == null) {
            throw new IllegalArgumentException("Unrecognizable size value");
        }
        List<Thumbnail> thumbnails = thumbnailRepository.findThumbnailsBySize(size);
        List<String> encodedImages = new ArrayList<>();
        for (Thumbnail thumbnail : thumbnails) {
            encodedImages.add(Base64.getEncoder().encodeToString(thumbnail.getSource()));
        }
        return encodedImages;
    }
}
