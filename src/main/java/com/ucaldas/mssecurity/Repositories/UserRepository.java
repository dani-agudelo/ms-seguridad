package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User,String> {
    // busca documentos donde el campo `email` coincide con el primer parámetro del método (`?0`).
    @Query("{'email': ?0}")
    public User getUserByEmail(String email);
}
