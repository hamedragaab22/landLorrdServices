package com.codewithabdelrahman.landlorddervice.repositories;

import com.codewithabdelrahman.landlorddervice.models.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
    AppUser findByToken(String token);
    AppUser findByVerificationCode (String verificationCode);
}
