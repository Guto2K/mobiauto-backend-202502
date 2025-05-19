package com.mobiauto.manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mobiauto.manager.enums.StatusOportunidade;
import com.mobiauto.manager.models.Oportunidade;

public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
	
    List<Oportunidade> findByRevendaId(Long revendaId);
    List<Oportunidade> findByResponsavelId(Long responsavelId);
    List<Oportunidade> findByRevendaIdAndStatus(Long revendaId, StatusOportunidade status);
    
    @Query("SELECT COUNT(o) FROM Oportunidade o WHERE o.responsavel.id = :responsavelId AND o.status = :status")
    long countByResponsavelIdAndStatus(Long responsavelId, StatusOportunidade status);
    
    Optional<Oportunidade> findTopByResponsavelIdOrderByDataAtribuicaoDesc(Long responsavelId);

}