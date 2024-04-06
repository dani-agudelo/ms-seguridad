package com.ucaldas.mssecurity.Services;

import java.util.HashMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * This class represents a service for sending notifications.
 */
public class NotificationsService {
  private RestTemplate restTemplate = new RestTemplate();
  private HttpHeaders headers = new HttpHeaders();

  /**
   * Sends a notification to the specified URL with the given body.
   *
   * @param url  the URL to send the notification to
   * @param body the body of the notification
   */
  public void send(String url, HashMap<String, String> body) {
    HttpEntity<HashMap<String, String>> request = new HttpEntity<>(body, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    System.out.println(response.getBody());
  }
}
