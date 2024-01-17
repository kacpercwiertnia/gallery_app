package pl.edu.agh.to2.backend.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import pl.edu.agh.to2.backend.directory.Directory;
import pl.edu.agh.to2.backend.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Image {
    @Id
    @GeneratedValue
    private int imageId;
    @Lob
    @Column(name = "source", length = 32768)
    private byte[] source;
    @OneToMany
    private List<Thumbnail> Thumbnails;

    @ManyToOne
    private Directory directory;

    public Image(byte[] source) {
        this.source = source;
        this.Thumbnails = new ArrayList<>();
    }

    public Image() {
    }

    public int getImageId() {
        return imageId;
    }

    public byte[] getSource() {
        return source;
    }

    public List<Thumbnail> getThumbnails() {
        return Thumbnails;
    }

    public Directory getDirectory(){
        return directory;
    }

    public void setDirectory(Directory directory){
        this.directory = directory;
    }
    
    public void addThumbnails(Thumbnail thumbnail) {
        Thumbnails.add(thumbnail);
    }
}
