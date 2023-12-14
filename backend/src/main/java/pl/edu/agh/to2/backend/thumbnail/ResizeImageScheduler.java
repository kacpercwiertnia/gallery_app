package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ResizeImageScheduler {
    private ThumbnailService thumbnailService;

    public ResizeImageScheduler(ThumbnailService service) {
        thumbnailService = service;
    }

    @Scheduled(fixedDelay = 2000L)
    public void resize() {
        thumbnailService.resizeImageFromQueue();
    }
}
