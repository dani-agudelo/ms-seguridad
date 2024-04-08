package com.ucaldas.mssecurity.Repositories;

import com.ucaldas.mssecurity.Models.RolePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RolePermissionRepository extends MongoRepository<RolePermission,String> {
    // Consulta para buscar un rolpermiso por rol, devuelve una lista
     // Ojo, se ponen los nombres de las propiedades de la clase RolePermission que son referencias a otras colecciones
    @Query("{'role.$id': ObjectId(?0)}")
    List<RolePermission> getPermissionsByRole(String roleId);

    // Consulta para buscar un permiso-rol por rol y permiso específico
    @Query("{'role.$id': ObjectId(?0),'permission.$id': ObjectId(?1)}")
    RolePermission getRolePermission(String roleId,String permissionId);
}
