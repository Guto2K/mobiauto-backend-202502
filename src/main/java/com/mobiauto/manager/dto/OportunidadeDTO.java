package com.mobiauto.manager.dto;

import java.time.LocalDateTime;

import com.mobiauto.manager.enums.StatusOportunidade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OportunidadeDTO {

    private Long id;

    @NotNull(message = "O ID da revenda é obrigatório.")
    private Long revendaId;

    private Long responsavelId;

    @NotBlank(message = "O nome do cliente é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome do cliente deve ter entre 2 e 100 caracteres.")
    private String nomeCliente;

    @NotBlank(message = "O e-mail do cliente é obrigatório.")
    @Email(message = "E-mail do cliente inválido.")
    private String emailCliente;

    @NotBlank(message = "O telefone do cliente é obrigatório.")
    private String telefoneCliente;

    @NotBlank(message = "A marca do veículo é obrigatória.")
    private String marcaVeiculo;

    @NotBlank(message = "O modelo do veículo é obrigatório.")
    private String modeloVeiculo;

    @NotBlank(message = "A versão do veículo é obrigatória.")
    private String versaoVeiculo;

    @NotNull(message = "O ano do modelo do veículo é obrigatório.")
    private Integer anoModelo;

    private StatusOportunidade status;

    private String motivoConclusao;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtribuicao;
    private LocalDateTime dataConclusao;
}
