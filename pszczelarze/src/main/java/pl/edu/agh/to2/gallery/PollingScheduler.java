package pl.edu.agh.to2.gallery;

import javafx.application.Platform;
import pl.edu.agh.to2.Main;

import java.util.Timer;
import java.util.TimerTask;

public class PollingScheduler extends Thread{
    private final GalleryControler galleryControler;
    private final Timer timer = new Timer("Scheduler");
    public PollingScheduler(GalleryControler galleryControler){
        this.galleryControler = galleryControler;
    }
    @Override
    public void run(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Main.log.info("Requesting for thumbnails update...");
                Platform.runLater(galleryControler::refreshThumbnailsLists);
            }
        };
        timer.scheduleAtFixedRate(task, 0,500);
    }
}