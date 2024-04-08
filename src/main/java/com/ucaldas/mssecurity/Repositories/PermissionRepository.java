package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    // Consulta para buscar un permiso por url y método
    @Query("{'url': ?0,'method': ?1}")
    Permission getPermission(String url, String method);
}
