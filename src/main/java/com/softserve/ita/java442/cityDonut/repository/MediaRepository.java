package com.softserve.ita.java442.cityDonut.repository;

import com.softserve.ita.java442.cityDonut.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByName(String name);
}
