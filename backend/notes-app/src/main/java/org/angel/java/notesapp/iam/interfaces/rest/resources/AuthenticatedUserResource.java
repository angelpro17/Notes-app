package org.angel.java.notesapp.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String username, String token) {
}
