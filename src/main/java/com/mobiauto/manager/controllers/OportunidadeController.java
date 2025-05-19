package com.mobiauto.manager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiauto.manager.dto.EditarOportunidadeDTO;
import com.mobiauto.manager.dto.OportunidadeDTO;
import com.mobiauto.manager.models.Usuario;
import com.mobiauto.manager.services.OportunidadeService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
public class OportunidadeController {
	
    private final OportunidadeService oportunidadeService;

    @PostMapping
    public ResponseEntity<OportunidadeDTO> criarOportunidade(@Valid @RequestBody OportunidadeDTO oportunidadeDTO, Authentication authentication) {
        return ResponseEntity.ok(oportunidadeService.criarOportunidade(oportunidadeDTO, getLoggedUser(authentication)));
    }

    @PutMapping("/{id}/atribuir")
    public ResponseEntity<OportunidadeDTO> atribuirOportunidade(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(oportunidadeService.atribuirOportunidade(id, getLoggedUser(authentication)));
    }

    @PutMapping("/{id}/concluir")
    public ResponseEntity<OportunidadeDTO> concluirOportunidade(
            @PathVariable Long id, 
            @RequestParam String motivo,
            Authentication authentication) {
        return ResponseEntity.ok(oportunidadeService.concluirOportunidade(id, motivo, getLoggedUser(authentication)));
    }

    @PutMapping("/{id}/transferir")
    public ResponseEntity<OportunidadeDTO> transferirOportunidade(
            @PathVariable Long id, 
            @RequestParam Long novoResponsavelId,
            Authentication authentication) {
        return ResponseEntity.ok(oportunidadeService.transferirOportunidade(id, novoResponsavelId, getLoggedUser(authentication)));
    }
    
	private Usuario getLoggedUser(Authentication authentication) {
		return (Usuario) authentication.getPrincipal();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OportunidadeDTO> editarOportunidade(
	        @PathVariable Long id,
	        @Valid @RequestBody EditarOportunidadeDTO edicaoDTO,
	        Authentication authentication) {
	    
	    Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
	    return ResponseEntity.ok(oportunidadeService.editarOportunidade(id, edicaoDTO, usuarioLogado));
	}
}