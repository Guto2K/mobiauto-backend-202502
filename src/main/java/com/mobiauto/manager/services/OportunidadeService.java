package com.mobiauto.manager.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.mobiauto.manager.dto.EditarOportunidadeDTO;
import com.mobiauto.manager.dto.OportunidadeDTO;
import com.mobiauto.manager.enums.CargoUsuario;
import com.mobiauto.manager.enums.StatusOportunidade;
import com.mobiauto.manager.exceptions.BusinessException;
import com.mobiauto.manager.exceptions.EntityNotFoundException;
import com.mobiauto.manager.models.Oportunidade;
import com.mobiauto.manager.models.Revenda;
import com.mobiauto.manager.models.Usuario;
import com.mobiauto.manager.repositories.OportunidadeRepository;
import com.mobiauto.manager.repositories.RevendaRepository;
import com.mobiauto.manager.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OportunidadeService {
    private final OportunidadeRepository oportunidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final RevendaRepository revendaRepository;
    private final AuthenticationService authenticationService;

    public OportunidadeDTO criarOportunidade(OportunidadeDTO oportunidadeDTO, Usuario usuarioLogado) {
        // Verifica se a revenda existe
        Revenda revenda = revendaRepository.findById(oportunidadeDTO.getRevendaId())
                .orElseThrow(() -> new EntityNotFoundException("Revenda não encontrada"));

        // Verifica permissões - usuário deve estar vinculado à revenda
        if (!authenticationService.usuarioTemAcessoARevenda(usuarioLogado, revenda.getId())) {
            throw new AccessDeniedException("Acesso negado a esta revenda");
        }

        // Cria a oportunidade com status NOVO
        Oportunidade oportunidade = Oportunidade.builder()
                .revenda(revenda)
                .nomeCliente(oportunidadeDTO.getNomeCliente())
                .emailCliente(oportunidadeDTO.getEmailCliente())
                .telefoneCliente(oportunidadeDTO.getTelefoneCliente())
                .marcaVeiculo(oportunidadeDTO.getMarcaVeiculo())
                .modeloVeiculo(oportunidadeDTO.getModeloVeiculo())
                .versaoVeiculo(oportunidadeDTO.getVersaoVeiculo())
                .anoModelo(oportunidadeDTO.getAnoModelo())
                .status(StatusOportunidade.NOVO)
                .dataCriacao(LocalDateTime.now())
                .build();

        oportunidade = oportunidadeRepository.save(oportunidade);
        return convertToDto(oportunidade);
    }

    public OportunidadeDTO atribuirOportunidade(Long oportunidadeId, Usuario usuarioLogado) {
        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        // Verifica se a oportunidade já está atribuída
        if (oportunidade.getResponsavel() != null) {
            throw new BusinessException("Oportunidade já está atribuída a um responsável");
        }

        // Verifica permissões
        if (!authenticationService.usuarioPodeAtribuirOportunidade(usuarioLogado, oportunidade.getRevenda().getId())) {
            throw new AccessDeniedException("Você não tem permissão para atribuir oportunidades");
        }

        // Encontra o assistente disponível automaticamente
        Usuario assistente = encontrarAssistenteDisponivel(oportunidade.getRevenda().getId());
        
        oportunidade.setResponsavel(assistente);
        oportunidade.setStatus(StatusOportunidade.EM_ATENDIMENTO);
        oportunidade.setDataAtribuicao(LocalDateTime.now());
        
        oportunidade = oportunidadeRepository.save(oportunidade);
        return convertToDto(oportunidade);
    }

    public OportunidadeDTO concluirOportunidade(Long oportunidadeId, String motivo, Usuario usuarioLogado) {
        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        // Verifica permissões - somente responsável ou gerentes/proprietários podem concluir
        if (!authenticationService.usuarioPodeEditarOportunidade(usuarioLogado, oportunidade)) {
            throw new AccessDeniedException("Você não tem permissão para concluir esta oportunidade");
        }

        if (oportunidade.getStatus() != StatusOportunidade.EM_ATENDIMENTO) {
            throw new BusinessException("Apenas oportunidades em atendimento podem ser concluídas");
        }

        oportunidade.setStatus(StatusOportunidade.CONCLUIDO);
        oportunidade.setMotivoConclusao(motivo);
        oportunidade.setDataConclusao(LocalDateTime.now());
        
        oportunidade = oportunidadeRepository.save(oportunidade);
        return convertToDto(oportunidade);
    }

    public OportunidadeDTO transferirOportunidade(Long oportunidadeId, Long novoResponsavelId, Usuario usuarioLogado) {
        Oportunidade oportunidade = oportunidadeRepository.findById(oportunidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        Usuario novoResponsavel = usuarioRepository.findById(novoResponsavelId)
                .orElseThrow(() -> new EntityNotFoundException("Novo responsável não encontrado"));

        // Verifica permissões - somente gerentes/proprietários podem transferir
        if (!authenticationService.usuarioPodeTransferirOportunidade(usuarioLogado, oportunidade.getRevenda().getId())) {
            throw new AccessDeniedException("Você não tem permissão para transferir oportunidades");
        }

        // Verifica se o novo responsável é um assistente da mesma revenda
        if (!novoResponsavel.getRevenda().getId().equals(oportunidade.getRevenda().getId()) ||
            novoResponsavel.getCargo() != CargoUsuario.ASSISTENTE) {
            throw new BusinessException("O novo responsável deve ser um assistente da mesma revenda");
        }

        oportunidade.setResponsavel(novoResponsavel);
        oportunidade.setDataAtribuicao(LocalDateTime.now());
        
        oportunidade = oportunidadeRepository.save(oportunidade);
        return convertToDto(oportunidade);
    }

    private Usuario encontrarAssistenteDisponivel(Long revendaId) {
        // Busca todos os assistentes da revenda
        List<Usuario> assistentes = usuarioRepository.findByRevendaIdAndCargo(revendaId, CargoUsuario.ASSISTENTE);
        
        if (assistentes.isEmpty()) {
            throw new BusinessException("Nenhum assistente disponível encontrado");
        }
        
        // Para cada assistente, obtém a contagem de oportunidades e a última data de atribuição
        Map<Usuario, Long> contagemOportunidades = new HashMap<>();
        Map<Usuario, LocalDateTime> ultimaAtribuicao = new HashMap<>();
        
        for (Usuario assistente : assistentes) {
            Long count = oportunidadeRepository.countByResponsavelIdAndStatus(
                assistente.getId(), StatusOportunidade.EM_ATENDIMENTO);
            contagemOportunidades.put(assistente, count);
            
            // Busca a última data de atribuição (pode ser null se nunca recebeu oportunidade)
            LocalDateTime data = oportunidadeRepository.findTopByResponsavelIdOrderByDataAtribuicaoDesc(assistente.getId())
                .map(Oportunidade::getDataAtribuicao)
                .orElse(null);
            ultimaAtribuicao.put(assistente, data);
        }
        
        // Cria o comparador composto(regras) corretamente
        Comparator<Usuario> comparador = Comparator
            .comparing((Usuario u) -> contagemOportunidades.get(u))
            .thenComparing(
                u -> ultimaAtribuicao.get(u),
                Comparator.nullsFirst(Comparator.naturalOrder())
            );
        
        return assistentes.stream()
            .min(comparador)
            .orElse(assistentes.get(0));
    }
    
    public OportunidadeDTO editarOportunidade(Long id, EditarOportunidadeDTO oportunidadeDTO, Usuario usuarioLogado) {
        Oportunidade oportunidadeExistente = oportunidadeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Oportunidade não encontrada"));

        // Verifica permissões de edição
        if (!podeEditarOportunidade(usuarioLogado, oportunidadeExistente)) {
            throw new AccessDeniedException("Você não tem permissão para editar esta oportunidade");
        }
        
        /* Verifica se a revenda foi alterada (não permitido)
        if (!oportunidadeExistente.getRevenda().getId().equals(oportunidadeDTO.getRevendaId())) {
            throw new BusinessException("Não é permitido alterar a revenda da oportunidade");
        }*/
        
        // Atualiza apenas os campos permitidos
        if (oportunidadeDTO.getNomeCliente() != null) oportunidadeExistente.setNomeCliente(oportunidadeDTO.getNomeCliente());
        if (oportunidadeDTO.getEmailCliente() != null) oportunidadeExistente.setEmailCliente(oportunidadeDTO.getEmailCliente());
        if (oportunidadeDTO.getTelefoneCliente() != null) oportunidadeExistente.setTelefoneCliente(oportunidadeDTO.getTelefoneCliente());
        if (oportunidadeDTO.getMarcaVeiculo() != null) oportunidadeExistente.setMarcaVeiculo(oportunidadeDTO.getMarcaVeiculo());
        if (oportunidadeDTO.getModeloVeiculo() != null) oportunidadeExistente.setModeloVeiculo(oportunidadeDTO.getModeloVeiculo());
        if (oportunidadeDTO.getVersaoVeiculo() != null) oportunidadeExistente.setVersaoVeiculo(oportunidadeDTO.getVersaoVeiculo());
        if (oportunidadeDTO.getAnoModelo() != null) oportunidadeExistente.setAnoModelo(oportunidadeDTO.getAnoModelo());

        Oportunidade oportunidadeAtualizada = oportunidadeRepository.save(oportunidadeExistente);
        return convertToDto(oportunidadeAtualizada);
    }

    private boolean podeEditarOportunidade(Usuario usuario, Oportunidade oportunidade) {
        // Administradores podem editar qualquer oportunidade
        if (usuario.getCargo() == CargoUsuario.ADMINISTRADOR) {
            return true;
        }
        
        // Dono da oportunidade pode editar
        if (oportunidade.getResponsavel() != null && 
            oportunidade.getResponsavel().getId().equals(usuario.getId())) {
            return true;
        }
        
        // Gerentes e proprietários podem editar oportunidades da sua revenda
        return (usuario.getCargo() == CargoUsuario.GERENTE || 
                usuario.getCargo() == CargoUsuario.PROPRIETARIO) &&
               usuario.getRevenda() != null && 
               usuario.getRevenda().getId().equals(oportunidade.getRevenda().getId());
    }

    private OportunidadeDTO convertToDto(Oportunidade oportunidade) {
        return OportunidadeDTO.builder()
                .id(oportunidade.getId())
                .revendaId(oportunidade.getRevenda().getId())
                .responsavelId(oportunidade.getResponsavel() != null ? oportunidade.getResponsavel().getId() : null)
                .nomeCliente(oportunidade.getNomeCliente())
                .emailCliente(oportunidade.getEmailCliente())
                .telefoneCliente(oportunidade.getTelefoneCliente())
                .marcaVeiculo(oportunidade.getMarcaVeiculo())
                .modeloVeiculo(oportunidade.getModeloVeiculo())
                .versaoVeiculo(oportunidade.getVersaoVeiculo())
                .anoModelo(oportunidade.getAnoModelo())
                .status(oportunidade.getStatus())
                .motivoConclusao(oportunidade.getMotivoConclusao())
                .dataCriacao(oportunidade.getDataCriacao())
                .dataAtribuicao(oportunidade.getDataAtribuicao())
                .dataConclusao(oportunidade.getDataConclusao())
                .build();
    }
}