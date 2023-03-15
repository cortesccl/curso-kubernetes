package org.scortesc.springcloud.msvc.courses.services.impl;

import org.scortesc.springcloud.msvc.courses.clients.UserClientRest;
import org.scortesc.springcloud.msvc.courses.models.entity.Course;
import org.scortesc.springcloud.msvc.courses.models.entity.CourseUser;
import org.scortesc.springcloud.msvc.courses.models.entity.User;
import org.scortesc.springcloud.msvc.courses.repositories.CourseRepository;
import org.scortesc.springcloud.msvc.courses.services.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service 
public class CourseServiceImpl implements ICourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserClientRest userClient;

    @Transactional(readOnly = true)
    @Override
    public List<Course> findAll() {
        return (List<Course>) courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findCourseByIdWithUsers(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> ids = course.getCourseUsers().stream()
                        .map(CourseUser::getUserId)
                        .collect(Collectors.toList());
                List<User> usersList = (List<User>) userClient.findAllUserById(ids);
                course.setUsers(usersList);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findCourseByName(String name) {
        return courseRepository.findByName(name);
    }

    @Override
    public boolean existsCourseByName(String name) {
        return courseRepository.existsByName(name);
    }

    @Transactional
    @Override
    public Course saveCourse(Course Course) {
        return courseRepository.save(Course);
    }

    @Transactional
    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteCourseUserByUserId(Long userId) {
        courseRepository.deleteCourseUserByUserId(userId);
    }

    @Transactional
    @Override
    public Optional<User> assignUser(Long courseId, User user) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            User userMsvc = userClient.findUserById(user.getId());
            Course course = courseOptional.get();
            CourseUser courseUser  = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }
    @Transactional
    @Override
    public Optional<User> createUser(Long courseId, User user) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            User userNewMsvc = userClient.createUser(user);

            Course course = courseOptional.get();
            CourseUser courseUser  = new CourseUser();
            courseUser.setUserId(userNewMsvc.getId());

            course.addCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userNewMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<User> deassignUser(Long courseId, User user) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            User userMsvc = userClient.findUserById(user.getId());

            Course course = courseOptional.get();
            CourseUser courseUser  = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.removeCourseUser(courseUser);
            courseRepository.save(course);
            return Optional.of(userMsvc);
        }
        return Optional.empty();
    }
}
