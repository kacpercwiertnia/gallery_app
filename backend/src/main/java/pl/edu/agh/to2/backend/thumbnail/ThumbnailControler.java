package pl.edu.agh.to2.backend.thumbnail;

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

    public ThumbnailControler(ThumbnailService thumbnailService, ImageService imageService) {
        this.thumbnailService = thumbnailService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity getThumbnails(@RequestParam ThumbnailSize size) {
        try {
            int allImagesCount = imageService.getAllImagesCount();
            List<String> base64Images = thumbnailService.getThumbnailsBySize(size);
            GetThumbnailsResponse response = new GetThumbnailsResponse(base64Images, allImagesCount);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unknown size value passed: " + size.toString());
        }

    }
}
