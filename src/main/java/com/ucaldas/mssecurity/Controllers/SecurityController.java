package com.ucaldas.mssecurity.Controllers;

import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import com.ucaldas.mssecurity.Services.EncryptionService;
import com.ucaldas.mssecurity.Services.JwtService;
import com.ucaldas.mssecurity.Services.MfaService;
import com.ucaldas.mssecurity.Services.NotificationsService;
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
  @Autowired private SecurityService securityService;
  @Autowired private EncryptionService encryptionService;
  @Autowired private NotificationsService notificationsService;

  @Autowired private SessionRepository sessionRepository;
  @Autowired private UserRepository userRepository;

  @PostMapping("login")
  public User login(@RequestBody User theUser, final HttpServletResponse response)
      throws IOException {
    User currentUser = this.securityService.validateUser(theUser);

    if (currentUser != null) {
      String code2fa = this.mfaService.generateCode();
      boolean status = this.notificationsService.sendCodeByEmail(currentUser, code2fa);

      if (!status) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return null;
      }

      Session currentSession = new Session(code2fa, currentUser);
      this.sessionRepository.save(currentSession);

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

  @PostMapping("users/{userId}/verify-2fa/{code2fa}")
  public String verify2fa(
      @PathVariable String userId, @PathVariable String code2fa, final HttpServletResponse response)
      throws IOException {
    HashMap<String, String> credentials = new HashMap<>();
    credentials.put("userId", userId);
    credentials.put("code2fa", code2fa);
    return this.verify2fa(credentials, response);
  }

  @PostMapping("password-reset")
  public User passwordReset(
      @RequestBody HashMap<String, String> credentials, final HttpServletResponse response)
      throws IOException {
    User currentUser = this.userRepository.getUserByEmail(credentials.get("email"));

    if (currentUser != null) {
      String newPassword = this.encryptionService.generatePassword();
      boolean status = this.notificationsService.sendPasswordResetEmail(currentUser, newPassword);

      if (!status) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return null;
      }

      currentUser.setPassword(this.encryptionService.convertSHA256(newPassword));
      this.userRepository.save(currentUser);
      currentUser.setPassword("");

      response.setStatus(HttpServletResponse.SC_ACCEPTED);
      return currentUser;
    }

    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    return null;
  }
}
