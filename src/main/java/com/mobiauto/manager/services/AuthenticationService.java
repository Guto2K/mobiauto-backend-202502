package com.mobiauto.manager.services;

import org.springframework.stereotype.Service;

import com.mobiauto.manager.enums.CargoUsuario;
import com.mobiauto.manager.models.Oportunidade;
import com.mobiauto.manager.models.Usuario;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
	public boolean usuarioTemAcessoARevenda(Usuario usuario, Long revendaId) {
        // ADMIN tem acesso a todas as revendas
        if (usuario.getCargo() == CargoUsuario.ADMINISTRADOR) {
            return true;
        }
        
        // Outros usuários só têm acesso à sua própria revenda
        return usuario.getRevenda() != null && 
               usuario.getRevenda().getId().equals(revendaId);
    }

    public boolean usuarioPodeAtribuirOportunidade(Usuario usuario, Long revendaId) {
        // ADMIN pode atribuir em qualquer revenda
        if (usuario.getCargo() == CargoUsuario.ADMINISTRADOR) {
            return true;
        }
        
        // GERENTE e PROPRIETARIO podem atribuir na sua revenda
        return (usuario.getCargo() == CargoUsuario.GERENTE || 
                usuario.getCargo() == CargoUsuario.PROPRIETARIO) &&
               usuario.getRevenda() != null && 
               usuario.getRevenda().getId().equals(revendaId);
    }

    public boolean usuarioPodeEditarOportunidade(Usuario usuario, Oportunidade oportunidade) {
        // ADMIN pode editar qualquer oportunidade
        if (usuario.getCargo() == CargoUsuario.ADMINISTRADOR) {
            return true;
        }
        
        // Dono da oportunidade pode editar
        if (oportunidade.getResponsavel() != null && 
            oportunidade.getResponsavel().getId().equals(usuario.getId())) {
            return true;
        }
        
        // GERENTE e PROPRIETARIO podem editar oportunidades da sua revenda
        return (usuario.getCargo() == CargoUsuario.GERENTE || 
                usuario.getCargo() == CargoUsuario.PROPRIETARIO) &&
               usuario.getRevenda() != null && 
               usuario.getRevenda().getId().equals(oportunidade.getRevenda().getId());
    }

    public boolean usuarioPodeTransferirOportunidade(Usuario usuario, Long revendaId) {
        // ADMIN pode transferir em qualquer revenda
        if (usuario.getCargo() == CargoUsuario.ADMINISTRADOR) {
            return true;
        }
        
        // GERENTE e PROPRIETARIO podem transferir na sua revenda
        return (usuario.getCargo() == CargoUsuario.GERENTE || 
                usuario.getCargo() == CargoUsuario.PROPRIETARIO) &&
               usuario.getRevenda() != null && 
               usuario.getRevenda().getId().equals(revendaId);
    }
}