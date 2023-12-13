package pl.edu.agh.to2.backend.queue;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.Image;
import pl.edu.agh.to2.backend.thumbnail.ThumbnailSize;

import java.util.Arrays;
import java.util.List;


@Service
public class QueueService {
    private QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository){
        this.queueRepository = queueRepository;
    }

    public void queueImages(Image image){
        Queue queueImage = new Queue(image);
        queueRepository.save(queueImage);
    }
}
