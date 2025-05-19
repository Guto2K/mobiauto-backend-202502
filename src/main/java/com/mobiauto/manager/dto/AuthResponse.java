package com.mobiauto.manager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobiauto.manager.enums.CargoUsuario;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
	    String token,
	    String email,
	    String nome,
	    CargoUsuario cargo
	) {
	    // Construtor simplificado para o register
	    public AuthResponse(String token) {
	        this(token, null, null, null);
	    }
	    
	    // Método builder para compatibilidade
	    public static AuthResponseBuilder builder() {
	        return new AuthResponseBuilder();
	    }

	    public static class AuthResponseBuilder {
	        private String token;
	        private String email;
	        private String nome;
	        private CargoUsuario cargo;

	        public AuthResponseBuilder token(String token) {
	            this.token = token;
	            return this;
	        }

	        public AuthResponseBuilder email(String email) {
	            this.email = email;
	            return this;
	        }

	        public AuthResponseBuilder nome(String nome) {
	            this.nome = nome;
	            return this;
	        }

	        public AuthResponseBuilder cargo(CargoUsuario cargo) {
	            this.cargo = cargo;
	            return this;
	        }

	        public AuthResponse build() {
	            return new AuthResponse(token, email, nome, cargo);
	        }
	    }
	}
