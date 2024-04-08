package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Role;
import com.ucaldas.mssecurity.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired  // inyecta la dependencia
    private RoleRepository theRoleRepository;

    /**
     * Maneja las solicitudes GET que se envían a /roles
     * devuelve una lista de todos los roles en el sistema
     */
    @GetMapping("")
    public List<Role> findAll(){
        return this.theRoleRepository.findAll();
    }
    @ResponseStatus(HttpStatus.CREATED)

    /**
     * Maneja las solicitudes POST que se envían a /roles
     * crea un nuevo rol en el sistema con lo que se envía en el cuerpo de la
     * solicitud
     */
    @PostMapping
    public Role create(@RequestBody Role theNewRole){
        return this.theRoleRepository.save(theNewRole);
    }

    /**
     * Maneja las solicitudes GET que se envían a /roles/{id}
     * devuelve el rol con el id especificado
     */
    @GetMapping("{id}")
    public Role findById(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        return theRole;
    }

    /**
     * Maneja las solicitudes PUT que se envían a /roles/{id}
     * actualiza el rol con el id especificado con lo que se envía en el cuerpo de
     * la solicitud
     */
    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role theNewRole) {
        Role theActualRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theActualRole != null) {
            theActualRole.setName(theNewRole.getName());
            theActualRole.setDescription(theNewRole.getDescription());

            return this.theRoleRepository.save(theActualRole);
        } else {
            return null;
        }
    }

    /**
     * Maneja las solicitudes DELETE que se envían a /roles/{id}
     * elimina el rol con el id especificado
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Role theRole = this.theRoleRepository
                .findById(id)
                .orElse(null);
        if (theRole != null) {
            this.theRoleRepository.delete(theRole);
        }
    }
}
