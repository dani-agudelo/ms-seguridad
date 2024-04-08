package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Models.RolePermission;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.PermissionRepository;
import com.ucaldas.mssecurity.Repositories.RolePermissionRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorsService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private PermissionRepository thePermissionRepository;
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RolePermissionRepository theRolePermissionRepository;
    // Prefijo para el token
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Método que valida si el usuario tiene permisos para acceder a un recurso
     * 
     * @param request: solicitud HTTP que contiene la información de la solicitud
     */
    public boolean validationRolePermission(HttpServletRequest request,String url,String method){
        boolean success=false;
        User theUser=this.getUser(request);
        if(theUser!=null){
            // Se obtiene el rol del usuario
            Role theRole=theUser.getRole();
            System.out.println("Antes URL "+url+" metodo "+method);
            // Se reemplazan los números y letras por un signo de interrogación para que la
            // URL sea más general y que el id no afecte la validación
            // Así no hay que crear un permiso para cada usuario individualmente 
            url = url.replaceAll("[0-9a-fA-F]{24}|\\d+", "?");
            System.out.println("URL "+url+" metodo "+method);
            Permission thePermission=this.thePermissionRepository.getPermission(url,method);
            if(theRole!=null && thePermission!=null){
                System.out.println("Rol "+theRole.get_id()+ " Permission "+thePermission.get_id());
                RolePermission theRolePermission=this.theRolePermissionRepository.getRolePermission(theRole.get_id(),thePermission.get_id());
                System.out.println();
                // Si el permiso del rol no es null, esto significa que el usuario tiene permiso para acceder al recurso
                if (theRolePermission!=null){
                    success=true;
                }
            }else{
                success=false;
            }
        }
        return success;
    }

    /**
     * Método que obtiene el usuario a partir del token
     * 
     */
    public User getUser(final HttpServletRequest request) {
        User theUser=null;
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Header "+authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            System.out.println("Bearer Token: " + token);
            User theUserFromToken=jwtService.getUserFromToken(token);
            if(theUserFromToken!=null) {
                theUser= this.theUserRepository.findById(theUserFromToken.get_id())
                        .orElse(null);
                // Se establece la contraseña en blanco por seguridad
                theUser.setPassword("");
            }
        }
        return theUser;
    }
}
