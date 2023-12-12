package pl.edu.agh.to2.backend.thumbnail;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import pl.edu.agh.to2.backend.image.Image;

@Entity
public class Thumbnail {
    @Id
    @GeneratedValue
    private int item_id;
    private byte[] source;
    private ThumbnailSize size;
    @ManyToOne
    private Image image;

    public Thumbnail(byte[] source, ThumbnailSize size, Image image){
        this.source = source;
        this.size = size;
        this.image = image;
    }

    public Thumbnail(){
    }

    public int getItem_id(){
        return item_id;
    }

    public byte[] getSource() {
        return source;
    }

    public ThumbnailSize getSize() {
        return size;
    }

    public Image getImage(){
        return image;
    }
}
