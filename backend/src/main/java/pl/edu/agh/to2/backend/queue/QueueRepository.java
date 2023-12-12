package pl.edu.agh.to2.backend.queue;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueRepository extends JpaRepository<Queue, Integer> {
}
