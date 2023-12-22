package pl.edu.agh.to2.backend.thumbnail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.image.ImageService;
import pl.edu.agh.to2.backend.rest.ThumbnailsRequest;

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
    public ResponseEntity getRequestedThumbnails(@RequestBody ThumbnailsRequest request){
        var imagesIds = request.imagesIds();
        var size = request.size();
        try{
            var thumbnails = thumbnailService.getThumbnailsByIdsAndSize(imagesIds, size);
            return ResponseEntity.ok().body(thumbnails);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Unknown size value passed: " + size.toString());
        }
    }
}