package com.mobiauto.manager.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "revendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Revenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String cnpj;
    
    @Column(nullable = false)
    private String nomeSocial;
    
    @OneToMany(mappedBy = "revenda", cascade = CascadeType.ALL)
    private List<Usuario> usuarios;
    
    @OneToMany(mappedBy = "revenda", cascade = CascadeType.ALL)
    private List<Oportunidade> oportunidades;
}

