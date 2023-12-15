package pl.edu.agh.to2.backend.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.rest.SendImageRequest;


import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageControler {
    private ImageService imageService;


    public ImageControler(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/post_image")
    public ResponseEntity sendImage(@RequestBody SendImageRequest request) {
        try {
            imageService.addNewImage(request.image());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Not a valid encoded image sent for at least one item.");
        }

        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}

