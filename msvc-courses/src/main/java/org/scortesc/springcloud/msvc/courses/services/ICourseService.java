package org.scortesc.springcloud.msvc.courses.services;

import org.scortesc.springcloud.msvc.courses.models.entity.Course;
import org.scortesc.springcloud.msvc.courses.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface ICourseService {
    List<Course> findAll ();

    Optional<Course> findCourseById (Long id);
    Optional<Course> findCourseByIdWithUsers (Long id, String token);

    Optional<Course> findCourseByName (String name);

    boolean existsCourseByName (String name);

    Course saveCourse (Course Course);

    void deleteCourse (Long id);

    void deleteCourseUserByUserId (Long userId);

    Optional<User> assignUser (Long courseId,  User user);

    Optional<User> createUser (Long courseId,  User user);

    Optional<User> deassignUser (Long courseId, User user);

}
