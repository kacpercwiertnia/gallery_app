package pl.edu.agh.to2.backend.directory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.to2.backend.rest.SubdirectoriesRequest;
import pl.edu.agh.to2.backend.rest.SubdirectoriesResponse;

@RestController
@RequestMapping("/directories")
public class DirectoryController {
    private final DirectoryService directoryService;
    private final Logger log = LoggerFactory.getLogger(Logger.class);

    public DirectoryController(DirectoryService directoryService){
        this.directoryService = directoryService;
    }

    @PostMapping
    public ResponseEntity<SubdirectoriesResponse> getSubdirectories(@RequestBody SubdirectoriesRequest request){
        var subDirectories = directoryService.getSubdirectoriesByParent(request.parent());
        return ResponseEntity.ok(new SubdirectoriesResponse(subDirectories));
    }
}
