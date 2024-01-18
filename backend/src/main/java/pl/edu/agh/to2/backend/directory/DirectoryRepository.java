package pl.edu.agh.to2.backend.directory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DirectoryRepository extends JpaRepository<Directory, Integer> {
    Directory findByPath(String path);

    List<Directory> findAllByParent(String parent);
}
