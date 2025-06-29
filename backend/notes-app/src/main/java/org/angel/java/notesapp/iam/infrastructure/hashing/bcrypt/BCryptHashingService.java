package org.angel.java.notesapp.iam.infrastructure.hashing.bcrypt;

import org.angel.java.notesapp.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;


public interface BCryptHashingService extends HashingService, PasswordEncoder {
}

