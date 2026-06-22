package com.saoco.perfumes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== Buscando usuario: " + username);
        
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.out.println("=== ERROR: Usuario NO encontrado: " + username);
                return new UsernameNotFoundException("Usuario no encontrado: " + username);
            });

        System.out.println("=== EXITO: Usuario encontrado: " + usuario.getUsername());
        System.out.println("=== Password en BD: " + usuario.getPassword());
        System.out.println("=== Rol: " + usuario.getRol());

        return new org.springframework.security.core.userdetails.User(
            usuario.getUsername(),
            usuario.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}