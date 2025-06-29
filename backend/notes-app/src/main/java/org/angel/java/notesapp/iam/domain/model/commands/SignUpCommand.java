package org.angel.java.notesapp.iam.domain.model.commands;

import org.angel.java.notesapp.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(String username, String password, List<Role> roles) {
}

