package pl.edu.agh.to2.gallery;

import javafx.application.Platform;
import pl.edu.agh.to2.Main;

public class PollingScheduler extends Thread{
    private final GalleryControler galleryControler;
    public PollingScheduler(GalleryControler galleryControler){
        this.galleryControler = galleryControler;
    }
    @Override
    public void run(){
        try{
            while(galleryControler.isRunning()){
                Main.log.info("Requesting for thumbnails update...");
                Platform.runLater(galleryControler::refreshThumbnailsLists);
                Thread.sleep(3000);
            }
        } catch(InterruptedException exception){
            Main.log.warning("Exception caught in Scheduler: "+exception.getMessage());
        }
    }
}