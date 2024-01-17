package pl.edu.agh.to2.backend.image;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.directory.Directory;
import pl.edu.agh.to2.backend.directory.DirectoryRepository;
import pl.edu.agh.to2.backend.queue.QueueService;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.*;

@Service
public class ImageService {
    private final QueueService queueService;
    private final ImageRepository imageRepository;

    private final DirectoryRepository directoryRepository; //TODO: possibly to remove
    private final Set<String> ACCEPTED_FORMATS;

    public ImageService(QueueService queueService, ImageRepository imageRepository, DirectoryRepository directoryRepository) {
        this.queueService = queueService;
        this.imageRepository = imageRepository;
        this.directoryRepository = directoryRepository;
        ACCEPTED_FORMATS = Set.of("image/jpeg", "image/png", "image/jpg");
    }

    public void addNewImages(String[] encodedImages) throws IllegalArgumentException {
        List<Image> checkedImages = new ArrayList<>();
        var dir = directoryRepository.findAll();
        Directory actualDir;
        if (dir.size() == 0){
            actualDir = new Directory("/", "/");
        }
        else{
            actualDir = dir.get(0);
        }
        //TODO: to be removed

        directoryRepository.save(actualDir);
        for (String encodedImage : encodedImages) {
            byte[] byteImage = Base64.getDecoder().decode(encodedImage);
            try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage))) {
                String mimetype = URLConnection.guessContentTypeFromStream(is);
                if (!ACCEPTED_FORMATS.contains(mimetype)) {
                    throw new IllegalArgumentException("Not a valid image");
                }

                var img = new Image(byteImage);
                img.setDirectory(actualDir);
                checkedImages.add(img);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        imageRepository.saveAll(checkedImages);
        queueService.queueImages(checkedImages);

    }

    public List<Integer> getImagesIds() {
        return imageRepository.findAll()
                .stream()
                .map(Image::getImageId)
                .toList();
    }

    public Integer getTotalInDirectory(String path){
        return imageRepository.countAllByDirectory_Path(path);
    }

    public String getImageById(int id) throws IllegalArgumentException {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return Base64.getEncoder().encodeToString(image.get().getSource());
        }
        throw new IllegalArgumentException("No image with given ID");


    }
}
