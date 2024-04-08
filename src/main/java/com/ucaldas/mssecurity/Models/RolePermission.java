package com.ucaldas.mssecurity.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class RolePermission {
    @Id
    private String _id;
    @DBRef
    private Role role;
    @DBRef
    private Permission permission;

    // es un constructor vacío porque id se genera automáticamente y las relaciones se manejan con las anotaciones
    public RolePermission() {
    }

    public String get_id() {
        return _id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
