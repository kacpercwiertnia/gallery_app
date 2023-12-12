package pl.edu.agh.to2.backend.thumbnail;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to2.backend.queue.QueueRepository;
import pl.edu.agh.to2.backend.queue.QueueService;

@Service
public class ThumbnailService {
    private ThumbnailRepository thumbnailRepository;
    private QueueRepository queueRepository;

    public ThumbnailService(ThumbnailRepository repository){
        thumbnailRepository = repository;
    }

    @Transactional
    public void resizeImageFromQueue(){

    }

    public void test(){
        System.out.println("łysy zamknij dupe");
    }
}
