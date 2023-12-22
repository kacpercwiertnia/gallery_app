package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer> {

    @Query("select t from Thumbnail t WHERE t.size = :size AND t.image.imageId IN :imagesIds")
    List<Thumbnail> findThumbnailsByIdAndSize(@Param("imagesIds") List<Integer> imagesIds, @Param("size") ThumbnailSize size);
}
