package org.angel.java.notesapp.iam.interfaces.rest.transform;

import org.angel.java.notesapp.iam.domain.model.entities.Role;
import org.angel.java.notesapp.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}

