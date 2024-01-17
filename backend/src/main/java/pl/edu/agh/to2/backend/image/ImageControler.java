package pl.edu.agh.to2.backend.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.backend.rest.ImagesIdsResponse;
import pl.edu.agh.to2.backend.rest.OriginalImageReponse;
import pl.edu.agh.to2.backend.rest.SendImagesRequest;
import pl.edu.agh.to2.backend.rest.TotalInDirectoryResponse;

import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageControler {
    private final ImageService imageService;

    public ImageControler(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<String> sendImages(@RequestBody SendImagesRequest request) {
        try {
            imageService.addNewImages(request.images());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Not a valid encoded image sent for at least one item.");
        }
        return ResponseEntity.ok().body("Success");
    }

    @GetMapping
    public ResponseEntity<ImagesIdsResponse> getImagesIds() { //refactor/remove
        var imagesIds = imageService.getImagesIds();
        return ResponseEntity.ok(new ImagesIdsResponse(imagesIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OriginalImageReponse> getOriginalImage(@PathVariable int id) {
        try {
            String image = imageService.getImageById(id);
            return ResponseEntity.ok(new OriginalImageReponse(image, null));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/total")
    public ResponseEntity<TotalInDirectoryResponse> getTotalInDirectory(@RequestParam String path){
        var total = imageService.getTotalInDirectory(path);
        return ResponseEntity.ok(new TotalInDirectoryResponse(total));
    }
}

