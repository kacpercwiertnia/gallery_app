package pl.edu.agh.to2.backend.thumbnail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import pl.edu.agh.to2.backend.image.Image;

@Entity
public class Thumbnail {
    @Id
    @GeneratedValue
    private int thumbnailId;
    @Lob
    @Column(name = "source", length = 32768)
    private byte[] source;
    private ThumbnailSize size;

    private boolean isSuccesful;
    @ManyToOne
    private Image image;

    public Thumbnail(byte[] source, ThumbnailSize size, Image image, boolean isSuccesful) {
        this.source = source;
        this.size = size;
        this.image = image;
        this.isSuccesful = isSuccesful;
    }

    public Thumbnail() {
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public byte[] getSource() {
        return source;
    }

    public ThumbnailSize getSize() {
        return size;
    }

    public Image getImage() {
        return image;
    }

    public boolean getIsSuccesful(){
        return isSuccesful;
    }
}
