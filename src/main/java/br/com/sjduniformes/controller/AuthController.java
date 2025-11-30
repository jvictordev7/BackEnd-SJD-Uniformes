package br.com.sjduniformes.controller;

import br.com.sjduniformes.dto.AuthResponse;
import br.com.sjduniformes.dto.LoginRequest;
import br.com.sjduniformes.model.Usuario;
import br.com.sjduniformes.repository.UsuarioRepository;
import br.com.sjduniformes.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public Usuario registrar(@RequestBody Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuario.getRole() == null) {
            usuario.setRole("USER");
        }
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        String token = jwtService.gerarToken(request.getEmail());
        return new AuthResponse(token, "Bearer");
    }
}
