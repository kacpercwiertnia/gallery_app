package pl.edu.agh.to2.backend.image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Integer countAllByDirectory_Path(String path);
}
