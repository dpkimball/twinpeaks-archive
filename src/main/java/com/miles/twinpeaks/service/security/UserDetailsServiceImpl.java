package com.miles.twinpeaks.service.security;

import com.miles.twinpeaks.dao.jpa.security.IApplicationUserRepository;
import com.miles.twinpeaks.domain.security.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Repository lookup used by framework-managed authentication. */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IApplicationUserRepository accounts;

    public UserDetailsServiceImpl(IApplicationUserRepository accounts) {
        this.accounts = accounts;
    }

    public ApplicationUser loadUserWithIdByUsername(String username) {
        ApplicationUser account = accounts.findByUsername(username);
        if (account != null) {
            return account;
        }
        throw new UsernameNotFoundException("Account not found");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        ApplicationUser account = loadUserWithIdByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getUsername())
                .password(account.getPassword()).authorities("ROLE_USER").build();
    }
}
