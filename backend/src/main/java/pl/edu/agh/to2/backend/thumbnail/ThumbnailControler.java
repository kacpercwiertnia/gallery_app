package pl.edu.agh.to2.backend.thumbnail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.backend.image.ImageService;
import pl.edu.agh.to2.backend.rest.GetThumbnailsResponse;

import java.util.List;

@RestController
@RequestMapping("/thumbnails")
public class ThumbnailControler {
    private final ThumbnailService thumbnailService;
    private final ImageService imageService;
    private final Logger log = LoggerFactory.getLogger(Logger.class);

    public ThumbnailControler(ThumbnailService thumbnailService, ImageService imageService) {
        this.thumbnailService = thumbnailService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity getThumbnails(@RequestParam ThumbnailSize size) {
        try {
            log.info("Received request for thumbnails of " + size + " size");
            int allImagesCount = imageService.getAllImagesCount();
            List<String> base64Images = thumbnailService.getThumbnailsBySize(size);
            GetThumbnailsResponse response = new GetThumbnailsResponse(base64Images, allImagesCount);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            log.warn("Request failed due to bad parameter");
            return ResponseEntity.badRequest().body("Unknown size value passed: " + size.toString());
        }

    }
}
