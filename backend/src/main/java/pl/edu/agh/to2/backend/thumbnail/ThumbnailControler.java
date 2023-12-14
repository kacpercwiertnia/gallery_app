package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.rest.GetRequest;
import pl.edu.agh.to2.backend.rest.GetResponse;

import java.util.List;

@RestController
@RequestMapping("/thumbnails")
public class ThumbnailControler {
    private ThumbnailService thumbnailService;

    public ThumbnailControler(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    @GetMapping(value = "/get_thumbnails")
    public ResponseEntity getImages(@RequestBody GetRequest size) {
        try {
            List<String> base64Images = thumbnailService.getThumbnailsBySize(size.getSize());
            GetResponse response = new GetResponse(base64Images);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
