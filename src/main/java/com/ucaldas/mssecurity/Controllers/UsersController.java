package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private RoleRepository theRoleRepository;
    @Autowired
    private EncryptionService theEncryptionService;

    /**
     * Maneja las peticiones GET a /api/users
     * @return una lista de todos los usuarios en el sistema
     */
    @GetMapping("")
    public List<User> findAll(){
        return this.theUserRepository.findAll();
    }

    /**
     * Maneja las peticiones POST a /api/users
     * Cifra la contraseña antes de guardar el usuario
     * param theNewUser: el usuario a crear
     * @return: el usuario creado
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody User theNewUser){
        theNewUser.setPassword(theEncryptionService.convertSHA256(theNewUser.getPassword()));
        return this.theUserRepository.save(theNewUser);
    }

    /**
     * Maneja las peticiones GET a /api/users/{id}
     * Busca un usuario por id
     */
    @GetMapping("{id}")
    public User findById(@PathVariable String id) {
        User theUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        return theUser;
    }

    /**
     * Maneja las peticiones PUT a /users/{id}
     * Cifra la contraseña antes de guardar el usuario
     * Modifica un usuario
     */
    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User theNewUser) {

        User theActualUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        if (theActualUser != null) {
            theActualUser.setName(theNewUser.getName());
            theActualUser.setEmail(theNewUser.getEmail());
            theActualUser.setPassword(theEncryptionService.convertSHA256(theNewUser.getPassword()));
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

    /**
     * Maneja las peticiones DELETE a /users/{id}
     * Elimina un usuario
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        User theUser = this.theUserRepository
                .findById(id)
                .orElse(null);
        if (theUser != null) {
            this.theUserRepository.delete(theUser);
        }
    }

    /**
     * Se encarga de modificar el rol de un usuario
     * param roleId: id del rol
     * return: el usuario modificado o null
     */
    @PutMapping("{userId}/role/{roleId}")
    public User matchRole(@PathVariable String userId,@PathVariable String roleId) {
        User theActualUser = this.theUserRepository
                .findById(userId)
                .orElse(null);
        Role theActualRole=this.theRoleRepository
                .findById(roleId)
                .orElse(null);

        if (theActualUser != null && theActualRole!=null) {
            theActualUser.setRole(theActualRole);
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

    /**
     * Se encarga de quitar el rol de un usuario
     * param roleId: id del rol
     * return: el usuario modificado o null
     */
    @PutMapping("{userId}/unmatch-role/{roleId}")
    public User unMatchRole(@PathVariable String userId,@PathVariable String roleId) {
        User theActualUser = this.theUserRepository
                .findById(userId)
                .orElse(null);
        Role theActualRole=this.theRoleRepository
                .findById(roleId)
                .orElse(null);

        if (theActualUser != null
                && theActualRole!=null
                && theActualUser.getRole().get_id().equals(roleId)) {
            theActualUser.setRole(null);
            return this.theUserRepository.save(theActualUser);
        } else {
            return null;
        }
    }

}
