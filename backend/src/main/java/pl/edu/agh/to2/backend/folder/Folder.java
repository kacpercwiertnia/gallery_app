package pl.edu.agh.to2.backend.folder;

import jakarta.persistence.*;
import pl.edu.agh.to2.backend.image.Image;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(columnList = "path", unique = true))
public class Folder {
    @Id
    @GeneratedValue
    private int folderId;
    private String path;
    @OneToMany
    private List<Image> images;

    public Folder(String path){
        this.path = path;
        this.images = new ArrayList<>();
    }

    public Folder(){}

    public void addImage(Image image){
        images.add(image);
    }

    public void setImages(List<Image> images){
        this.images = images;
    }

    public String getPath() {
        return path;
    }

    public List<Image> getImages() {
        return images;
    }
}
