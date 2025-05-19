package com.mobiauto.manager.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiauto.manager.enums.StatusOportunidade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oportunidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oportunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "revenda_id", nullable = false)
    private Revenda revenda;
    
    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;
    
    @Column(nullable = false)
    private String nomeCliente;
    
    @Column(nullable = false)
    private String emailCliente;
    
    @Column(nullable = false)
    private String telefoneCliente;
    
    @Column(nullable = false)
    private String marcaVeiculo;
    
    @Column(nullable = false)
    private String modeloVeiculo;
    
    @Column(nullable = false)
    private String versaoVeiculo;
    
    @Column(nullable = false)
    private Integer anoModelo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOportunidade status;
    
    private String motivoConclusao;
    
    @CreationTimestamp
    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataAtribuicao;
    
    private LocalDateTime dataConclusao;
}