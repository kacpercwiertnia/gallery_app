package pl.edu.agh.to2.backend.queue;

import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.image.Image;

import java.util.List;

@Service
public class QueueService {
    private QueueRepository queueRepository;

    public QueueService(QueueRepository queueRepository) {
        this.queueRepository = queueRepository;
    }

    public void queueImages(List<Image> images) {
        var queuedImages =  images.stream().map(Queue::new).toList();
        queueRepository.saveAll(queuedImages);
    }
}
