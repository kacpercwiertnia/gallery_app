package pl.edu.agh.to2.backend.thumbnail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.rest.ThumbnailsPagedRequest;
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

    @PostMapping //TODO: think why it is post and why not get (consult with others)
                    //TODO: remove
    public ResponseEntity<ThumbnailsResponse> getRequestedThumbnails(@RequestBody ThumbnailsRequest request){
        var imagesIds = request.imagesIds();
        var size = request.size();
        try{
            var thumbnails = thumbnailService.getThumbnailsByIdsAndSize(imagesIds, size);
            return ResponseEntity.ok().body(new ThumbnailsResponse(thumbnails, "Success"));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ThumbnailsResponse(null,
                    "Unknown size value passed: " + size.toString()));
        }
    }

    @PostMapping(path = "/paged")
    public ResponseEntity<ThumbnailsResponse> getThumbnails(@RequestBody ThumbnailsPagedRequest request){
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