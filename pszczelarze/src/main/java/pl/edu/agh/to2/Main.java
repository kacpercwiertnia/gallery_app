package pl.edu.agh.to2;

import javafx.application.Application;
import pl.edu.agh.to2.gallery.GalleryApp;

import java.util.logging.Logger;

public class Main {
	public static final Logger log = Logger.getLogger(Main.class.toString());
	
	public static void main(String[] args) {
		log.info("Hello world");
		Application.launch(GalleryApp.class);
	}
}
