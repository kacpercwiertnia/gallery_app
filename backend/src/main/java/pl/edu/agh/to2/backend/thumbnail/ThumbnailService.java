package pl.edu.agh.to2.backend.thumbnail;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.ImageRepository;
import pl.edu.agh.to2.backend.queue.QueueRepository;
import pl.edu.agh.to2.backend.rest.ThumbnailDto;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ThumbnailService {
    private final ThumbnailRepository thumbnailRepository;
    private final QueueRepository queueRepository;
    private final ImageRepository imageRepository;
    private final ImageScaler imageScaler;
    private final Logger log = LoggerFactory.getLogger(Logger.class);
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
            log.info("No records in queue");
            return;
        }

        var queue = queuedImages.get(0);
        var image = queue.getImage();

        for (ThumbnailSize size : ThumbnailSize.values()) {
            try {
                var scaledImage = imageScaler.scaleImage(image.getSource(), size.getSize());
                var thumbnail = new Thumbnail(scaledImage, size, image, true);
                thumbnailRepository.save(thumbnail);
                image.addThumbnails(thumbnail);
            } catch (IOException ex) {
                log.warn("Operation failed for image with ID: " + image.getImageId() + " and size: " + size);
                var thumbnail = new Thumbnail(new byte[0], size, image, false);
                thumbnailRepository.save(thumbnail);
                image.addThumbnails(thumbnail);
            }
        }

        imageRepository.save(image);
        queueRepository.delete(queue);
    }

    //TODO: totalCount
    public List<ThumbnailDto> getThumbnailsBySizeForCurrentDirectory(String path, ThumbnailSize size, int page, int offset){
        List<Thumbnail> thumbnails = thumbnailRepository.findAllByImage_Directory_PathAndSize(path, size, PageRequest.of(page, offset));
        return thumbnails
                .stream()
                .map(thumbnail -> new ThumbnailDto(thumbnail.getImage().getImageId(), encodeToString(thumbnail.getSource()), thumbnail.getIsSuccesful()))
                .toList();
    }

    private String encodeToString(byte[] blob){ //TODO: Maybe make it a static method
        return Base64.getEncoder().encodeToString(blob);
    }
}
