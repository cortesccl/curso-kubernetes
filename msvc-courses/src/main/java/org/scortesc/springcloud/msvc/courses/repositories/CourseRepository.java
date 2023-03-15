package org.scortesc.springcloud.msvc.courses.repositories;

import org.scortesc.springcloud.msvc.courses.models.entity.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Optional<Course> findByName (String name);

    boolean existsByName (String name);

    @Modifying
    @Query("delete from CourseUser cu where cu.userId=?1")
    void deleteCourseUserByUserId (Long userId);
}
