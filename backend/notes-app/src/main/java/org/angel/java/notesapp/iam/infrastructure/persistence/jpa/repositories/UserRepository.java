package org.angel.java.notesapp.iam.infrastructure.persistence.jpa.repositories;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("iamUserRepository")
public interface UserRepository extends JpaRepository<User, Long>
{

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

}

