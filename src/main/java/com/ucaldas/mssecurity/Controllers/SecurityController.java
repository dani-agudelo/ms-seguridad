package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Services.JwtService;
import com.ucaldas.mssecurity.Services.MfaService;
import com.ucaldas.mssecurity.Services.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
  @Autowired private JwtService jwtService;
  @Autowired private MfaService mfaService;
  @Autowired private SessionRepository sessionRepository;
  @Autowired private SecurityService securityService;

  @PostMapping("login")
  public User login(@RequestBody User theUser, final HttpServletResponse response)
      throws IOException {
    User currentUser = this.securityService.validateUser(theUser);

    if (currentUser != null) {
      String code2fa = this.mfaService.generateCode();
      Session currentSession = new Session(code2fa, currentUser);
      this.sessionRepository.save(currentSession);
      this.mfaService.sendCode(currentUser, code2fa);
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
      currentUser.setPassword("");
      return currentUser;
    }

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    return null;
  }

  @PostMapping("verify-2fa")
  public String verify2fa(
      @RequestBody HashMap<String, String> credentials, final HttpServletResponse response)
      throws IOException {
    Session session = this.securityService.validateCode2fa(credentials);
    if (session != null) {
      User currentUser = session.getUser();
      String token = this.jwtService.generateToken(currentUser);

      session.setToken(token);
      session.setExpiration(jwtService.getExpiration(token));
      sessionRepository.save(session);

      response.setStatus(HttpServletResponse.SC_ACCEPTED);
      return token;
    }

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    return "";
  }
}
