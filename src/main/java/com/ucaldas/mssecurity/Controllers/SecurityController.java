package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.ChangePasswordCredential;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.EncryptionService;
import com.ucaldas.mssecurity.Services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private EncryptionService theEncryptionService;
    @Autowired
    private JwtService theJwtService;
    
    @PostMapping("login")
    public String login(@RequestBody User theUser, final HttpServletResponse response) throws IOException {
        String token="";
        User actualUser=this.theUserRepository.getUserByEmail(theUser.getEmail());
        if(actualUser!=null &&
            actualUser.getPassword().equals(this.theEncryptionService.convertSHA256(theUser.getPassword()))){
            token=this.theJwtService.generateToken(actualUser);
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return token;
    }

    /**
     * Metodo para cambiar la contraseña de un usuario
     */

    @PostMapping("changePassword")
    public String changePassword(@RequestBody ChangePasswordCredential newUser, final HttpServletResponse response) throws IOException {
        User actualUser=this.theUserRepository.getUserByEmail(newUser.getEmail());
        if(actualUser!=null && actualUser.getPassword().equals(this.theEncryptionService.convertSHA256(newUser.getPassword()))){
            actualUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getNewPassword()));
            this.theUserRepository.save(actualUser);
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return "Contraseña cambiada";
    }
}
