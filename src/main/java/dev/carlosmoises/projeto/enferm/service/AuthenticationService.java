package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userExists = userRepository.findByEmailOrCoren(username, username);
        if (userExists != null) {
            return userExists;
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

    }
}
