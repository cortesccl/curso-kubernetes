package org.scortesc.springcloud.msvc.users.repositories;

import org.scortesc.springcloud.msvc.users.models.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByName (String name);
    boolean existsByName (String name);
    Optional<User> findByEmail (String email);
    boolean existsByEmail (String email);
}
