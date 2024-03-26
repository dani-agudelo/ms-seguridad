package com.ucaldas.mssecurity.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.ucaldas.mssecurity.Models.Role;


public interface RoleRepository extends MongoRepository< Role, String> {
}
