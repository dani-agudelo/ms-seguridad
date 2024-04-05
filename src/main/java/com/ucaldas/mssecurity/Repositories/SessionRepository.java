package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SessionRepository extends MongoRepository<Session, String> {
  @Query("{'user.$id': ObjectId(?0),'code2fa': ?1}")
  Session getSessionByUserAndCode2fa(String userId, String code2fa);
}
