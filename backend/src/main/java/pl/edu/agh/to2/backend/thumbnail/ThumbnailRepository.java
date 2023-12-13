package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer> {
    public List<Thumbnail> findThumbnailsBySize(ThumbnailSize size);
}
