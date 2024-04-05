package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.User;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MfaService {
  @Value("${mfa.url}")
  private String mfaUrl;

  public String generateCode() {
    return Stream.generate(() -> (int) (Math.random() * 10))
        .limit(5)
        .map(String::valueOf)
        .reduce("", String::concat);
  }

  public void sendCode(User user, String code) {
    String message = "Your code is: " + code;
    // send message to user email
  }
}
