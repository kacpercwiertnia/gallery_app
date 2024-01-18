package pl.edu.agh.to2.backend.directory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.Image;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class DirectoryService {
    private final DirectoryRepository directoryRepository;
    private final Logger log = LoggerFactory.getLogger(Logger.class);

    public DirectoryService(DirectoryRepository directoryRepository){

        this.directoryRepository = directoryRepository;

        Directory dir = directoryRepository.findByPath("/");
        if (dir == null) {
            dir = new Directory("/", null);
            directoryRepository.save(dir);
        }
    }

    public List<String> getSubdirectoriesByParent(String parent){
        return  directoryRepository.findAllByParent(parent).stream().map(Directory::getPath).toList();
    }
}
