-- Inserção de Revendas
INSERT INTO revendas (cnpj, nome_social) VALUES 
('11.111.111/0001-11', 'Revenda Automóveis São Paulo'),
('22.222.222/0001-22', 'Revenda Veículos Rio de Janeiro'),
('33.333.333/0001-33', 'Concessionária BH');

-- Inserção de Usuários
-- Senhas: todas são "senha123" criptografadas com BCrypt
INSERT INTO usuarios (nome, email, senha, cargo, revenda_id) VALUES
-- Administrador (sem revenda)
('Admin Sistema', 'admin@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'ADMINISTRADOR', NULL),

-- Revenda 1 											$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2
('Proprietário SP', 'proprietario.sp@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'PROPRIETARIO', 1),
('Gerente SP', 'gerente.sp@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'GERENTE', 1),
('Assistente 1 SP', 'assistente1.sp@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'ASSISTENTE', 1),
('Assistente 2 SP', 'assistente2.sp@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'ASSISTENTE', 1),

-- Revenda 2
('Proprietário RJ', 'proprietario.rj@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'PROPRIETARIO', 2),
('Gerente RJ', 'gerente.rj@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'GERENTE', 2),
('Assistente RJ', 'assistente.rj@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'ASSISTENTE', 2),

-- Revenda 3
('Proprietário BH', 'proprietario.bh@mobiauto.com.br', '$2a$10$/kuYLyLFzmMqyutFLSvTy.H8vFDr6rtAqEcfY.N//IV1yTRc/KGh2', 'PROPRIETARIO', 3);

-- Inserção de Oportunidades
INSERT INTO oportunidades (
    revenda_id, responsavel_id, nome_cliente, email_cliente, telefone_cliente,
    marca_veiculo, modelo_veiculo, versao_veiculo, ano_modelo, status,
    data_criacao, data_atribuicao, data_conclusao, motivo_conclusao
) VALUES
-- Oportunidades da Revenda 1
(1, NULL, 'Cliente 1', 'cliente1@email.com', '11999999999', 'Toyota', 'Corolla', 'GLI', 2023, 'NOVO', CURRENT_TIMESTAMP, NULL, NULL, NULL),
(1, 4, 'Cliente 2', 'cliente2@email.com', '11988888888', 'Honda', 'Civic', 'Touring', 2022, 'EM_ATENDIMENTO', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day', NULL, NULL),
(1, 4, 'Cliente 3', 'cliente3@email.com', '11977777777', 'Volkswagen', 'Golf', 'Highline', 2021, 'CONCLUIDO', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'Venda realizada'),

-- Oportunidades da Revenda 2
(2, NULL, 'Cliente 4', 'cliente4@email.com', '21999999999', 'Ford', 'Mustang', 'GT', 2023, 'NOVO', CURRENT_TIMESTAMP, NULL, NULL, NULL),
(2, 7, 'Cliente 5', 'cliente5@email.com', '21988888888', 'Chevrolet', 'Camaro', 'SS', 2022, 'EM_ATENDIMENTO', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP, NULL, NULL),

-- Oportunidades da Revenda 3
(3, NULL, 'Cliente 6', 'cliente6@email.com', '31999999999', 'BMW', 'X5', 'M50i', 2023, 'NOVO', CURRENT_TIMESTAMP, NULL, NULL, NULL);