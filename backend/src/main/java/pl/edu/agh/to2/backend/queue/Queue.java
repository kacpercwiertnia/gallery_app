package pl.edu.agh.to2.backend.queue;

import jakarta.persistence.*;
import pl.edu.agh.to2.backend.image.Image;

@Entity
public class Queue {
    @Id
    @GeneratedValue
    private int itemId;
    @OneToOne
    private Image image;

    public Queue(Image image){
        this.image = image;
    }

    public Queue(){
    }

    public int getItemId(){
        return itemId;
    }

    public Image getImage(){
        return image;
    }
}
