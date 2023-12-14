package pl.edu.agh.to2.backend.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailService;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageControler {
    private ImageService imageService;
    private ThumbnailService thumbnailService;

    public ImageControler(ImageService imageService, ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
        this.imageService = imageService;
    }

    @GetMapping(value = "/get_them/{size}")
    public ResponseEntity getImages(@PathVariable String size) {
        try {
            List<String> base64Images = thumbnailService.getThumbnailsBySize(size);
            GetResponse response = new GetResponse(base64Images);
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/post_them")
    public ResponseEntity sendImage(@RequestBody String image) {
        System.out.println("Dostałem obrazek");
        imageService.addNewImage(image);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}

