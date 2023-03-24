package org.scortesc.springcloud.msvc.courses.controllers;

import feign.FeignException;
import org.scortesc.springcloud.msvc.courses.models.entity.Course;
import org.scortesc.springcloud.msvc.courses.models.entity.User;
import org.scortesc.springcloud.msvc.courses.services.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CourseController {
    @Autowired
    private ICourseService courseService;

    @GetMapping
    public List<Course> findAll () {
        return courseService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Long id, @RequestHeader(value="Authorization", required = true) String token) {
        Optional<Course> courseOptional = courseService.findCourseByIdWithUsers(id, token);//courseService.findCourseById(id);
        if (courseOptional.isPresent()) {
            return ResponseEntity.ok(courseOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCourse (@Valid @RequestBody Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (courseService.existsCourseByName(course.getName())) {
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("error", "Ya existe un curso con el name proporcionado!!!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse (@PathVariable Long id, @Valid @RequestBody Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Optional<Course> courseOptional = courseService.findCourseById(id);
        if (courseOptional.isPresent()) {
            Course courseDB = courseOptional.get();
            if (!course.getName().equals(courseDB.getName()) && courseService.findCourseByName(course.getName()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("error", "Ya existe un curso con el name proporcionado!!!"));
            }
            courseDB.setName(course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(courseService.saveCourse(courseDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse (@PathVariable Long id) {
        Optional<Course> CourseOptional = courseService.findCourseById(id);
        if (CourseOptional.isPresent()) {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUser (@PathVariable Long courseId, @RequestBody User user) {
        Optional<User> userOptional;
        try {
            userOptional =courseService.assignUser(courseId, user);
            if (userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe el usuario por el Id o error en la comunicación: " + e.getMessage()));
        }
    }

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser (@PathVariable Long courseId, @RequestBody User user) {
        Optional<User> userOptional;
        try {
            userOptional =courseService.createUser(courseId, user);
            if (userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se pudo crear el usuario o error en la comunicación: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deassign-user/{courseId}")
    public ResponseEntity<?> deassignUser (@PathVariable Long courseId, @RequestBody User user) {
        Optional<User> userOptional;
        try {
            userOptional =courseService.deassignUser(courseId, user);
            if (userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se pudo eliminar el usuario o error en la comunicación: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteCourseUserByUserId (@PathVariable Long userId) {
        courseService.deleteCourseUserByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validate (BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
