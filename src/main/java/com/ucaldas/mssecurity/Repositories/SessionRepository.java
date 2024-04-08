package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {
}
