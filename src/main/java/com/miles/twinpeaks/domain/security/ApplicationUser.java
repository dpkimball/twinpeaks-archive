package com.miles.twinpeaks.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

/** Minimal receipt-owner record; publication replacement. */
@Entity
public class ApplicationUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String username;
    @JsonIgnore
    private String password;

    public long getId() { return id; }
    public void setId(long value) { id = value; }
    public String getUsername() { return username; }
    public void setUsername(String value) { username = value; }
    public String getPassword() { return password; }
    public void setPassword(String value) { password = value; }
}
