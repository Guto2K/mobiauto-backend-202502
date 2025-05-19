package com.mobiauto.manager.dto;

import com.mobiauto.manager.enums.CargoUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank String nome,
    @Email @NotBlank String email,
    @NotBlank @Size(min = 6) String senha,
    @NotNull CargoUsuario cargo,
    @NotNull Long revendaId
) {}

