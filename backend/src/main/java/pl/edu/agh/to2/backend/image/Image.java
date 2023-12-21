package pl.edu.agh.to2.backend.image;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import pl.edu.agh.to2.backend.queue.Queue;
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
    
    public void addThumbnails(Thumbnail thumbnail) {
        Thumbnails.add(thumbnail);
    }
}
