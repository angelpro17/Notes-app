package org.angel.java.notesapp.iam.interfaces.rest.transform;

import org.angel.java.notesapp.iam.domain.model.aggregates.User;
import org.angel.java.notesapp.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getUsername(), token);
    }
}