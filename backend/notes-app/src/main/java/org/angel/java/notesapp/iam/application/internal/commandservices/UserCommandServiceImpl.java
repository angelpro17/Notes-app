package org.angel.java.notesapp.iam.application.internal.commandservices;

import org.angel.java.notesapp.iam.application.internal.outboundservices.hashing.HashingService;
import org.angel.java.notesapp.iam.application.internal.outboundservices.tokens.TokenService;
import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.domain.model.commands.SignInCommand;
import org.angel.java.notesapp.iam.domain.model.commands.SignUpCommand;
import org.angel.java.notesapp.iam.domain.services.UserCommandService;
import org.angel.java.notesapp.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.angel.java.notesapp.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }


    @Override
    public Optional<User> handle(SignUpCommand command) {
        // Verificar si el nombre de usuario ya existe en la base de datos
        if (userRepository.existsByUsername(command.username())) {
            throw new RuntimeException("Username already exists");
        }

        // Buscar y asignar roles de usuario
        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role name not found: " + role.getName())))
                .toList();

        // Crear nuevo usuario con la contraseña codificada
        User user = new User(
                command.username(),
                hashingService.encode(command.password()), // Codificar la contraseña
                roles
        );

        // Guardar el nuevo usuario en la base de datos
        userRepository.save(user);

        // Devolver el usuario recién creado
        return userRepository.findByUsername(command.username());
    }
}
