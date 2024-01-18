package pl.edu.agh.to2.backend.thumbnail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.to2.backend.rest.ThumbnailsRequest;
import pl.edu.agh.to2.backend.rest.ThumbnailsResponse;

@RestController
@RequestMapping("/thumbnails")
public class ThumbnailControler {
    private final ThumbnailService thumbnailService;
    private final Logger log = LoggerFactory.getLogger(Logger.class);

    public ThumbnailControler(ThumbnailService thumbnailService) {
        this.thumbnailService = thumbnailService;
    }

    @PostMapping
    public ResponseEntity<ThumbnailsResponse> getThumbnails(@RequestBody ThumbnailsRequest request){
        try{
            var thumbnails = thumbnailService.getThumbnailsBySizeForCurrentDirectory(request.path()
                    , request.size(), request.page(), request.offset());
            return ResponseEntity.ok(new ThumbnailsResponse(thumbnails, "Success"));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ThumbnailsResponse(null,
                    "Unknown size value passed: " + request.size().toString()));
        }
    }
}