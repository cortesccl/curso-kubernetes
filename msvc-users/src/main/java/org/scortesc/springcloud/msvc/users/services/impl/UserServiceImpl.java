package org.scortesc.springcloud.msvc.users.services.impl;

import org.scortesc.springcloud.msvc.users.clients.CourseClientRest;
import org.scortesc.springcloud.msvc.users.models.entity.User;
import org.scortesc.springcloud.msvc.users.repositories.UserRepository;
import org.scortesc.springcloud.msvc.users.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClientRest courseClient;

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public boolean existsUserByName(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        courseClient.deleteCourseUserByUserId(id);
    }

    @Override
    public List<User> findAllById(Iterable<Long> ids) {
        return (List<User>) userRepository.findAllById(ids);
    }
}
