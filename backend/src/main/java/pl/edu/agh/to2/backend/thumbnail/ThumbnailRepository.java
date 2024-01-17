package pl.edu.agh.to2.backend.thumbnail;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Integer>, PagingAndSortingRepository<Thumbnail, Integer> {

    @Query("select t from Thumbnail t WHERE t.size = :size AND t.image.imageId IN :imagesIds")
    List<Thumbnail> findThumbnailsByIdAndSize(@Param("imagesIds") List<Integer> imagesIds, @Param("size") ThumbnailSize size); //TODO: remove
    List<Thumbnail> findAllByImage_Directory_PathAndSize(String path, ThumbnailSize size, Pageable pageable);
}
