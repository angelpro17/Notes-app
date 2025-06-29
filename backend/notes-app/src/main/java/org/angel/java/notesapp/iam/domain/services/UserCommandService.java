package org.angel.java.notesapp.iam.domain.services;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.domain.model.commands.SignInCommand;
import org.angel.java.notesapp.iam.domain.model.commands.SignUpCommand;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
    Optional<User> handle(SignUpCommand command);

}
