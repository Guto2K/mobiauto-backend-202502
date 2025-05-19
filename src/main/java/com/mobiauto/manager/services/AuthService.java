package com.mobiauto.manager.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobiauto.manager.dto.AuthRequest;
import com.mobiauto.manager.dto.AuthResponse;
import com.mobiauto.manager.dto.RegisterRequest;
import com.mobiauto.manager.enums.CargoUsuario;
import com.mobiauto.manager.exceptions.BusinessException;
import com.mobiauto.manager.exceptions.EntityNotFoundException;
import com.mobiauto.manager.models.Revenda;
import com.mobiauto.manager.models.Usuario;
import com.mobiauto.manager.repositories.RevendaRepository;
import com.mobiauto.manager.repositories.UsuarioRepository;
import com.mobiauto.manager.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RevendaRepository revendaRepository;


    public AuthResponse register(RegisterRequest request, Usuario usuarioLogado) {
        // Validação básicas
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        // Verifica permissões do usuário que está cadastrando
        validarPermissoesCadastro(usuarioLogado, request);

        // Busca a revenda
        Revenda revenda = revendaRepository.findById(request.revendaId())
                .orElseThrow(() -> new BusinessException("Revenda não encontrada"));

        // Cria o novo usuário
        Usuario novoUsuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .cargo(request.cargo())
                .revenda(revenda)
                .build();

        // Salva o usuário
        usuarioRepository.save(novoUsuario);
        
        // Gera o token JWT
        String jwtToken = jwtService.generateToken(novoUsuario);
        return new AuthResponse(jwtToken);
    }

    private void validarPermissoesCadastro(Usuario usuarioLogado, RegisterRequest novoUsuario) {
        if (usuarioLogado.getCargo() == CargoUsuario.ASSISTENTE) {
            throw new AccessDeniedException("Assistentes não podem cadastrar usuários");
        }

        // Se for GERENTE ou PROPRIETARIO, só pode cadastrar usuários na mesma revenda
        if (usuarioLogado.getCargo() == CargoUsuario.GERENTE || 
            usuarioLogado.getCargo() == CargoUsuario.PROPRIETARIO) {
            
            if (!usuarioLogado.getRevenda().getId().equals(novoUsuario.revendaId())) {
                throw new AccessDeniedException("Você só pode cadastrar usuários na sua própria revenda");
            }

            // Gerentes não podem cadastrar outros gerentes ou proprietários
            if (usuarioLogado.getCargo() == CargoUsuario.GERENTE && 
                (novoUsuario.cargo() == CargoUsuario.GERENTE || 
                 novoUsuario.cargo() == CargoUsuario.PROPRIETARIO)) {
                throw new AccessDeniedException("Gerentes não podem cadastrar outros gerentes ou proprietários");
            }
        }
    }
    public AuthResponse authenticate(AuthRequest request) {
        // Autentica o usuário
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
            )
        );

        // Busca o usuário no banco
        var usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Gera o token JWT
        var jwtToken = jwtService.generateToken(usuario);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .cargo(usuario.getCargo())
                .build();
    }
}