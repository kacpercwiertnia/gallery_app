package pl.edu.agh.to2.backend.image;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import pl.edu.agh.to2.backend.queue.Queue;
import pl.edu.agh.to2.backend.thumbnail.Thumbnail;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Image {
    @Id
    @GeneratedValue
    private int image_id;
    private byte[] source;
    private int size;
    @OneToMany
    private List<Queue> Queue;
    @OneToMany
    private List<Thumbnail> Thumbnails;

    public Image(byte[] source, int size){
        this.source = source;
        this.size = size;
        this.Queue = new ArrayList<>();
        this.Thumbnails = new ArrayList<>();
    }

    public Image(){
    }

    public int getImage_id(){
        return image_id;
    }

    public byte[] getSource() {
        return source;
    }

    public int getSize(){
        return size;
    }

    public List<Queue> getQueue(){
        return Queue;
    }

    public List<Thumbnail> getThumbnails(){
        return Thumbnails;
    }

    public void addToQueue(Queue queueItem){
        Queue.add(queueItem);
    }

    public void addThumbnails(Thumbnail thumbnail){
        Thumbnails.add(thumbnail);
    }
}
