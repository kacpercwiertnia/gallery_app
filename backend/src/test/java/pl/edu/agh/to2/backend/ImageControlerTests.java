package pl.edu.agh.to2.backend;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.agh.to2.backend.image.ImageControler;
import pl.edu.agh.to2.backend.image.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ImageControler.class)
public class ImageControlerTests {
    private byte[] content;
    private String[] encodedContent = new String[2];
    private Path resourceDirectory = Paths.get("src","test","resources");
    private String absolutePath = resourceDirectory.toFile().getAbsolutePath();
    @MockBean
    ImageService imageService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturn202GivenSingleImage(){
        //given
        try {
            content = Files.readAllBytes(Paths.get(absolutePath+"/image1.jpg"));
            encodedContent[0] = Base64.getEncoder().encodeToString(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //then
        try {
            mockMvc.perform(post("/image")
                    .content("{\"images\":[\""+encodedContent[0]+"\"]}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturn202GivenMultipleImages(){
        //given
        try {
            content = Files.readAllBytes(Paths.get(absolutePath+"/image1.jpg"));
            encodedContent[0] = Base64.getEncoder().encodeToString(content);
            content = Files.readAllBytes(Paths.get(absolutePath+"/image2.png"));
            encodedContent[1] = Base64.getEncoder().encodeToString(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //then
        try {
            mockMvc.perform(post("/image")
                            .content("{\"images\":[\""+encodedContent[0]+"\",\""+encodedContent[1]+"\"]}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
