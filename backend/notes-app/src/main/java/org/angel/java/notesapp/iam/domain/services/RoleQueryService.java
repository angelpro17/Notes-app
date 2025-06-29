package org.angel.java.notesapp.iam.domain.services;

import org.angel.java.notesapp.iam.domain.model.entities.Role;
import org.angel.java.notesapp.iam.domain.model.queries.GetAllRolesQuery;
import org.angel.java.notesapp.iam.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {
    List<Role>handle(GetAllRolesQuery query);
    Optional<Role>handle(GetRoleByNameQuery query);
}
