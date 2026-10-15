package com.miles.twinpeaks.dao.jpa.security;

import com.miles.twinpeaks.domain.security.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

/** Account lookup contract for the historical receipt model. */
public interface IApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findByUsername(String username);
}
