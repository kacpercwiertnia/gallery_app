package pl.edu.agh.to2.backend.image;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.directory.Directory;
import pl.edu.agh.to2.backend.directory.DirectoryRepository;
import pl.edu.agh.to2.backend.queue.QueueService;
import pl.edu.agh.to2.backend.rest.SendImagesPayload;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


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

    public void addNewImages(SendImagesPayload[] encodedImages) throws IllegalArgumentException {
        Map<String, List<String>> images = new HashMap<>();
        Directory dir;
        List<Image> checkedImages = new ArrayList<>();

        for (SendImagesPayload img:encodedImages){
            images.put(img.path(),List.of(img.images()));
        }

        for (Map.Entry<String, List<String>> entry:images.entrySet()){
            dir = directoryRepository.findByPath(entry.getKey());
            String path = entry.getKey().equals("/") ? "/" : StringUtils.chop(entry.getKey());
            String parent = getParent(path);

            if (dir == null){
                dir = new Directory(path,parent);
                directoryRepository.save(dir);
            }

            for (String encodedImage : entry.getValue()) {
                byte[] byteImage = Base64.getDecoder().decode(encodedImage);
                try (InputStream is = new BufferedInputStream(new ByteArrayInputStream(byteImage))) {
                    String mimetype = URLConnection.guessContentTypeFromStream(is);
                    if (mimetype == null){
                        continue;
                    }
                    if (!ACCEPTED_FORMATS.contains(mimetype)) {
                        throw new IllegalArgumentException("Not a valid image");
                    }

                    var img = new Image(byteImage);
                    img.setDirectory(dir);
                    checkedImages.add(img);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
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

    public String getImageById(int id) throws IllegalArgumentException {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isPresent()) {
            return Base64.getEncoder().encodeToString(image.get().getSource());
        }
        throw new IllegalArgumentException("No image with given ID");
    }

    private String getParent(String path){
        int slashes = StringUtils.countMatches(path,"/");

        return slashes <= 1 ? "/" : path.substring(0,path.lastIndexOf("/"));
    }
}
