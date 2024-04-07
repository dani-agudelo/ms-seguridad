package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.User;
import java.util.HashMap;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MfaService {
  @Value("${mfa.url.send.code}")
  private String mfaUrl;

  @Value("${mfa.url.verify.code}")
  private String mfaVerifyUrl;

  @Autowired private NotificationsService notificationsService;

  /**
   * Generates a random code consisting of 5 digits.
   *
   * @return the generated code as a string
   */
  public String generateCode() {
    return Stream.generate(() -> (int) (Math.random() * 10))
        .limit(8) // 8 digits
        .map(String::valueOf)
        .reduce("", String::concat);
  }

  /**
   * Sends the generated code to the specified user's email.
   *
   * @param user the user to send the code to
   * @param code the generated code
   */
  public void sendCodeByEmail(User user, String code) {
    var body = new HashMap<String, String>();
    mfaVerifyUrl = mfaVerifyUrl.replace("{userId}", user.get_id()).replace("{code2fa}", code);
    body.put("email", user.getEmail());
    body.put("username", user.getName());
    body.put("verifyUrl", mfaVerifyUrl);
    body.put("code", code);

    notificationsService.send(mfaUrl, body);
  }
}
