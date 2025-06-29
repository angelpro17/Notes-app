package org.angel.java.notesapp.iam.application.internal.queryservices;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.domain.model.queries.GetAllUsersQuery;
import org.angel.java.notesapp.iam.domain.model.queries.GetUserByIdQuery;
import org.angel.java.notesapp.iam.domain.model.queries.GetUserByUsernameQuery;
import org.angel.java.notesapp.iam.domain.services.UserQueryService;
import org.angel.java.notesapp.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }
}
