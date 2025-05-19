package com.mobiauto.manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditarOportunidadeDTO {
    
    @Size(min = 2, max = 100, message = "O nome do cliente deve ter entre 2 e 100 caracteres.")
    private String nomeCliente;
    
    @Email(message = "E-mail do cliente inválido.")
    private String emailCliente;
    
    private String telefoneCliente;
    
    private String marcaVeiculo;
    
    private String modeloVeiculo;
    
    private String versaoVeiculo;
    
    @Min(value = 2000, message = "O ano deve ser a partir de 2000")
    private Integer anoModelo;
}