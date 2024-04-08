package com.ucaldas.mssecurity.Services;

import com.ucaldas.mssecurity.Models.User;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/** This class represents a service for sending notifications. */
@Service
public class NotificationsService {
  @Value("${mfa.url.send.code}")
  private String mfaUrl;

  @Value("${mfa.url.verify.code}")
  private String mfaVerifyUrl;

  @Value("${user.url.send.password}")
  private String passwordUrl;

  @Value("${user.url.reset.password}")
  private String passwordResetUrl;

  private RestTemplate restTemplate = new RestTemplate();
  private HttpHeaders headers = new HttpHeaders();

  /**
   * Sends a notification to the specified URL with the given body.
   *
   * @param url  the URL to send the notification to
   * @param body the body of the notification
   */
  public boolean send(String url, HashMap<String, String> body) {
    headers.set("Content-Type", "application/json");
    HttpEntity<HashMap<String, String>> request = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
      System.out.println(response.getBody());
      return true;
    } catch (Exception e) {
      System.out.println("Error sending notification: " + e.getMessage());
      return false;
    }
  }

  /**
   * Sends the generated code to the specified user's email.
   *
   * @param user the user to send the code to
   * @param code the generated code
   */
  public boolean sendCodeByEmail(User user, String code) {
    var body = new HashMap<String, String>();
    mfaVerifyUrl = mfaVerifyUrl.replace("{userId}", user.get_id()).replace("{code2fa}", code);
    body.put("email", user.getEmail());
    body.put("username", user.getName());
    body.put("verifyUrl", mfaVerifyUrl);
    body.put("code", code);

    return send(mfaUrl, body);
  }

  /**
   * Sends the generated code to the specified user's email.
   *
   * @param user        the user to send the code to
   * @param newPassword the generated code
   */
  public boolean sendPasswordResetEmail(User user, String newPassword) {
    var body = new HashMap<String, String>();
    body.put("email", user.getEmail());
    body.put("username", user.getName());
    body.put("newPassword", newPassword);
    body.put("resetUrl", passwordResetUrl);
    
    return send(passwordUrl, body);
  }
}
