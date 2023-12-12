package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer> {
}
