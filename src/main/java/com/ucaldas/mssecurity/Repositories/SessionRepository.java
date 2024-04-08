package com.ucaldas.mssecurity.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ucaldas.mssecurity.Models.Session;

public interface SessionRepository extends MongoRepository<Session, String>{
    // Query to get a session by user and code2fa
    @Query("{'user.$id': ObjectId(?0), 'code2fa': ?1}")
    Session getSessionByUserAndCode2fa(String userId, String code2fa);
    
}
