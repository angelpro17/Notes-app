package org.angel.java.notesapp.iam.domain.services;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.domain.model.queries.GetAllUsersQuery;
import org.angel.java.notesapp.iam.domain.model.queries.GetUserByIdQuery;
import org.angel.java.notesapp.iam.domain.model.queries.GetUserByUsernameQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    List<User> handle(GetAllUsersQuery query);
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByUsernameQuery query);

}
