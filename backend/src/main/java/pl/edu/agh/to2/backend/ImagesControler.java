package pl.edu.agh.to2.backend;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/images")
public class ImagesControler {

    @GetMapping(path="/get_them")
    public byte[] getImages()  {
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            System.out.println("Current absolute path is: " + s);
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            URL input = getClass().getResource("src/main/resources/plik.jpg");
            System.out.println(input);
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/post_them")
    public String sendImage(){
        return "Twoj stary";
    }
}
