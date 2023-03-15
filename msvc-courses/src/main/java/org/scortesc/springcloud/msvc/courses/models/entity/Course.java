package org.scortesc.springcloud.msvc.courses.models.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message="El campo nombre no puede ser vacío")
    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private List<CourseUser> courseUsers;

    @Transient
    private List<User> users;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseUser> getCourseUsers() {
        if (courseUsers == null) {
            courseUsers = new ArrayList<>();
        }
        return courseUsers;
    }

    public void setCourseUsers(List<CourseUser> courseUsers) {
        this.courseUsers = courseUsers;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addCourseUser (CourseUser courseUser) {
        courseUsers.add(courseUser);
    }

    public void removeCourseUser (CourseUser courseUser) {
        courseUsers.remove(courseUser);
    }
}
