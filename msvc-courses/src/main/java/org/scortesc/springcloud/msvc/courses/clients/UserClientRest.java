package org.scortesc.springcloud.msvc.courses.clients;

import org.scortesc.springcloud.msvc.courses.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="msvc-users", url="${msvc.users.url}")
public interface UserClientRest {

    @GetMapping("/{id}")
    User findUserById (@PathVariable Long id);

    @GetMapping("/users-course")
    List<User> findAllUserById (@RequestParam Iterable<Long> ids);

    @PostMapping("/")
    User createUser (@RequestBody User user);
}
