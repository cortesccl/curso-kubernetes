package org.scortesc.springcloud.msvc.courses.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name= "courses_users")
public class CourseUser {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="user_id", unique=true)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CourseUser)) {
            return false;
        }
        CourseUser o = (CourseUser) obj;

        return this.userId != null && this.userId.equals(o.id);
    }
}
