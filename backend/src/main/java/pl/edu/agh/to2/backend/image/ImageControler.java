package pl.edu.agh.to2.backend.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import pl.edu.agh.to2.backend.rest.OriginalImageReponse;
import pl.edu.agh.to2.backend.rest.SendImagesRequest;
import pl.edu.agh.to2.backend.rest.TotalInDirectoryResponse;

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

