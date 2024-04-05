package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Permission;
import com.ucaldas.mssecurity.Repositories.PermissionRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/permissions")
public class PermissionsController {
  @Autowired private PermissionRepository thePermissionRepository;

  @GetMapping("")
  public List<Permission> findAll() {
    return this.thePermissionRepository.findAll();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public Permission create(@RequestBody Permission theNewPermission) {
    return this.thePermissionRepository.save(theNewPermission);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("{id}")
  public void delete(@PathVariable String id) {
    Permission thePermission = this.thePermissionRepository.findById(id).orElse(null);
    if (thePermission != null) {
      this.thePermissionRepository.delete(thePermission);
    }
  }
}
