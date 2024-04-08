package com.ucaldas.mssecurity.Models;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Session {
  @Id private String _id;
  private String token;
  private String code2fa;
  private Boolean active = false;
  private LocalDateTime expiration;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  @DBRef private User user;

  public Session() {}

  public Session(String code2fa, User user) {
    this.code2fa = code2fa;
    this.user = user;
  }

  public Session(String code2fa, String token, Boolean active,LocalDateTime expiration,LocalDateTime startAt,LocalDateTime endAt) {
    this.code2fa = code2fa;
    this.token = token;
    this.active = active;
    this.expiration = expiration;
    this.startAt = startAt;
    this.endAt = endAt;
  }

  public String get_id() {
    return this._id;
  }

  public String getCode2fa() {
    return this.code2fa;
  }

  public void setCode2fa(String code2fa) {
    this.code2fa = code2fa;
  }

  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Boolean isActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public LocalDateTime getExpiration() {
    return this.expiration;
  }

  public void setExpiration(LocalDateTime expiration) {
    this.expiration = expiration;
  }

  public LocalDateTime getStartAt() {
    return this.startAt;
  }

  public void setStartAt(LocalDateTime startAt) {
    this.startAt = startAt;
  }

  public LocalDateTime getEndAt() {
    return this.endAt;
  }

  public void setEndAt(LocalDateTime endAt) {
    this.endAt = endAt;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
