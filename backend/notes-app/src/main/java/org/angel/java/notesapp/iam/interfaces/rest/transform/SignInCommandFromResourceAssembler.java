package org.angel.java.notesapp.iam.interfaces.rest.transform;

import org.angel.java.notesapp.iam.domain.model.commands.SignInCommand;
import org.angel.java.notesapp.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.username(), signInResource.password());
    }
}

