package org.scortesc.springcloud.msvc.users.services;

import org.scortesc.springcloud.msvc.users.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll ();

    Optional<User> findUserById (Long id);

    Optional<User> findUserByName (String name);

    boolean existsUserByName (String name);

    Optional<User> findUserByEmail (String email);

    boolean existsUserByEmail (String email);

    User saveUser (User user);

    void deleteUser (Long id);

    List<User> findAllById(Iterable<Long> ids);

}
