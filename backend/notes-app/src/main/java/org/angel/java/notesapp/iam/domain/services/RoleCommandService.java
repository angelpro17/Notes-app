package org.angel.java.notesapp.iam.domain.services;

import org.angel.java.notesapp.iam.domain.model.commands.SeedRolesCommand;

public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
