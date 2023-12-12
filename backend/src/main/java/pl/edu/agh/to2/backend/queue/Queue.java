package pl.edu.agh.to2.backend.queue;

import jakarta.persistence.*;
import pl.edu.agh.to2.backend.image.Image;
import pl.edu.agh.to2.backend.thumbnail.Thumbnail;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.List;

@Entity
public class Queue {
    @Id
    @GeneratedValue
    private int itemId;
    private ThumbnailSize size;
    @OneToOne
    private Image image;

    public Queue(ThumbnailSize size, Image image){
        this.size = size;
        this.image = image;
    }

    public Queue(){
    }

    public int getItemId(){
        return itemId;
    }

    public ThumbnailSize getSize() {
        return size;
    }

    public Image getImage(){
        return image;
    }
}
