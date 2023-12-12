package pl.edu.agh.to2.backend.image;

import jakarta.persistence.*;
import pl.edu.agh.to2.backend.queue.Queue;
import pl.edu.agh.to2.backend.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Image {
    @Id
    @GeneratedValue
    private int imageId;
    private byte[] source;
    private int size;
    @OneToOne
    private Queue queueItem;
    @OneToMany
    private List<Thumbnail> Thumbnails;

    public Image(byte[] source, int size){
        this.source = source;
        this.size = size;
        this.Thumbnails = new ArrayList<>();
    }

    public Image(){
    }

    public int getImageId(){
        return imageId;
    }

    public byte[] getSource() {
        return source;
    }

    public int getSize(){
        return size;
    }

    public Queue getQueueItem(){
        return queueItem;
    }

    public List<Thumbnail> getThumbnails(){
        return Thumbnails;
    }

    public void addToQueue(Queue queueItem){
        this.queueItem = queueItem;
    }

    public void removeFromQueue(){
        this.queueItem = null;
    }

    public void addThumbnails(Thumbnail thumbnail){
        Thumbnails.add(thumbnail);
    }
}
