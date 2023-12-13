package pl.edu.agh.to2.backend.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/images")
public class ImageControler {
    private ImageService imageService;

    public ImageControler(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/get_them")
    public ResponseEntity getImages() {
        System.out.println("Wysyłam obrazek");
        String base64Image = imageService.getImages();
        return ResponseEntity.ok().body(base64Image);
    }

    @PostMapping("/post_them")
    public ResponseEntity sendImage(@RequestBody String image) {
        System.out.println("Dostałem obrazek");
        imageService.addNewImage(image);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

