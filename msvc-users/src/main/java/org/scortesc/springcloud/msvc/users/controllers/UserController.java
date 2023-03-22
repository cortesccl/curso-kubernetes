package org.scortesc.springcloud.msvc.users.controllers;

import org.scortesc.springcloud.msvc.users.models.entity.User;
import org.scortesc.springcloud.msvc.users.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment env;

    @GetMapping("/crash")
    public void crash () {
        ((ConfigurableApplicationContext) context).close();
    }
    @GetMapping
    public ResponseEntity<?> findAll () {

        Map<String, Object> body = new HashMap<>();
        body.put("users", userService.findAll());
        body.put("pod_info", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
        body.put("texto", env.getProperty("config.texto"));
//        return Collections.singletonMap("users", userService.findAll());
//        return userService.findAll();

//        return body;
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Long id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser (@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (userService.existsUserByName(user.getName())) {
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("message", "Ya existe un usuario con el name proporcionado!!!"));
        }

        if (userService.existsUserByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("message", "Ya existe un usuario con el email proporcionado!!!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser (@PathVariable Long id, @Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            if (!user.getName().isEmpty() && !user.getName().equals(userDb.getName()) && userService.findUserByName(user.getName()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("message", "Ya existe un usuario con el name proporcionado!!!"));
            }
            if (!user.getEmail().isEmpty() &&
                    !user.getEmail().equalsIgnoreCase(userDb.getEmail()) && userService.findUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("message", "Ya existe un usuario con el email proporcionado!!!"));
            }
            userDb.setName(user.getName());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser (@PathVariable Long id) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-course")
    public ResponseEntity<?> findByIds (@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.findAllById(ids));
    }

    @GetMapping("/authorized")
    public Map<String, Object> authorized(@RequestParam(name = "code") String code) {
        return Collections.singletonMap("code", code);
    }


    private static ResponseEntity<Map<String, String>> validate (BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
