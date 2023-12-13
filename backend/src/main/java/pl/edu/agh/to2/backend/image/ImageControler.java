package pl.edu.agh.to2.backend.image;

import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailService;

import java.util.List;


@RestController
@RequestMapping("/images")
public class ImageControler {
    private ImageService imageService;
    private ThumbnailService thumbnailService;

    public ImageControler(ImageService imageService, ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
        this.imageService = imageService;
    }

    @GetMapping(value = "/get_them/{size}")
    public ResponseEntity<GetResponse> getImages(@PathVariable String size) {
        System.out.println("Wysyłam obrazek");
        List<String> base64Images = thumbnailService.getThumbnailsBySize(size);
        GetResponse response = new GetResponse(base64Images);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/post_them")
    public ResponseEntity sendImage(@RequestBody String image) {
        System.out.println("Dostałem obrazek");
        imageService.addNewImage(image);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

