package com.mobiauto.manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
	    @NotBlank @Email String email,
	    @NotBlank String senha
	) {

		public String getEmail() {
	        return email;
	    }
	    
	    public String getSenha() {
	        return senha;
	    }
	}