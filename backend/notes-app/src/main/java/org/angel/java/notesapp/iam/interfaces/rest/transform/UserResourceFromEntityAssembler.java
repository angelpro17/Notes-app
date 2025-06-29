package org.angel.java.notesapp.iam.interfaces.rest.transform;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.domain.model.entities.Role;
import org.angel.java.notesapp.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream().map(Role::getStringName).toList();
        return new UserResource(user.getId(), user.getUsername(), roles);
    }
}
