package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides security-related services for user validation and two-factor authentication.
 */
@Service
public class SecurityService {
  @Autowired private UserRepository userRepository;
  @Autowired private EncryptionService encryptionService;
  @Autowired private SessionRepository sessionRepository;

  public User validateUser(User user) {
    User currentUser = this.userRepository.getUserByEmail(user.getEmail());
    boolean isvalid =
        currentUser != null
            && currentUser
                .getPassword()
                .equals(this.encryptionService.convertSHA256(user.getPassword()));
    return isvalid ? currentUser : null;
  }

  public Session validateCode2fa(HashMap<String, String> credentials) {
    if (!credentials.containsKey("userId") || !credentials.containsKey("code2fa")) {
      return null;
    }
    String userId = credentials.get("userId");
    String code2fa = credentials.get("code2fa");

    try {
      Session session = this.sessionRepository.getSessionByUserAndCode2fa(userId, code2fa);

      if (session != null) {
        session.setActive(true);
        session.setStartAt(LocalDateTime.now());
        session.setEndAt(LocalDateTime.now().plusMinutes(2));
      }
      return session;
    } catch (Exception e) {
      return null;
    }
  }
}
