package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
}
