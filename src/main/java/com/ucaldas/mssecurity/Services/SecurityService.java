package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.Session;
import com.ucaldas.mssecurity.Models.User;
import com.ucaldas.mssecurity.Repositories.SessionRepository;
import com.ucaldas.mssecurity.Repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  @Autowired private UserRepository userRepository;
  @Autowired private EncryptionService encryptionService;
  @Autowired private SessionRepository sessionRepository;

  public User validateUser(User user) {
    User currentUser = this.userRepository.getUserByEmail(user.getEmail());
    boolean isPaswordCorrect =
        currentUser.getPassword().equals(this.encryptionService.convertSHA256(user.getPassword()));

    if (currentUser != null && isPaswordCorrect) {
      return currentUser;
    }
    return null;
  }

  public Session validateCode2fa(HashMap<String, String> credentials) {
    String userId = credentials.get("userId");
    String code2fa = credentials.get("code2fa");
    Session session = sessionRepository.getSessionByUserAndCode2fa(userId, code2fa);

    if (session != null) {
      session.setActive(false);
      session.setStartAt(LocalDateTime.now());
      session.setEndAt(LocalDateTime.now().plusMinutes(30));
    }
    return session;
  }
}
