package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.User;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MfaService {
  @Value("${mfa.url.send.code}")
  private String mfaUrl;

  /**
   * Generates a random code consisting of 5 digits.
   *
   * @return the generated code as a string
   */
  public String generateCode() {
    return Stream.generate(() -> (int) (Math.random() * 10))
        .limit(5)
        .map(String::valueOf)
        .reduce("", String::concat);
  }

  /**
   * Sends the generated code to the specified user's email.
   *
   * @param user the user to send the code to
   * @param code the generated code
   */
  public void sendCode(User user, String code) {
    String message = "Your code is: " + code;
    // send message to user email
  }
}
