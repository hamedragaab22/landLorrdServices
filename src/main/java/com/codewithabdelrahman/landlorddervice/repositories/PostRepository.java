package com.codewithabdelrahman.landlorddervice.repositories;

import com.codewithabdelrahman.landlorddervice.models.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}