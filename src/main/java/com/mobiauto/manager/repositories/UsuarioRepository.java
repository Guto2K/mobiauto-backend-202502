package com.mobiauto.manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiauto.manager.enums.CargoUsuario;
import com.mobiauto.manager.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRevendaIdAndCargo(Long revendaId, CargoUsuario cargo);
    List<Usuario> findByRevendaId(Long revendaId);
    
}

