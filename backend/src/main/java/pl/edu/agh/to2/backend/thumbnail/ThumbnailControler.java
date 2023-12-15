package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.backend.rest.GetThumbnailsResponse;

import java.util.List;

@RestController
@RequestMapping("/thumbnails")
public class ThumbnailControler {
    private ThumbnailService thumbnailService;

    public ThumbnailControler(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    @GetMapping(value = "/get_thumbnails")
    public ResponseEntity getThumbnails(@RequestParam String size) {
        try {
            int allImagesCount = thumbnailService.getAllImagesCount();
            List<String> base64Images = thumbnailService.getThumbnailsBySize(size);
            GetThumbnailsResponse response = new GetThumbnailsResponse(base64Images, allImagesCount);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unknown size value passed");
        }

    }
}
