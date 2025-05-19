CREATE TABLE revendas (
    id BIGSERIAL PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    nome_social VARCHAR(100) NOT NULL
);

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    cargo VARCHAR(20) NOT NULL,
    revenda_id BIGINT REFERENCES revendas(id)
);

CREATE TABLE oportunidades (
    id BIGSERIAL PRIMARY KEY,
    revenda_id BIGINT NOT NULL REFERENCES revendas(id),
    responsavel_id BIGINT REFERENCES usuarios(id),
    nome_cliente VARCHAR(100) NOT NULL,
    email_cliente VARCHAR(100) NOT NULL,
    telefone_cliente VARCHAR(20) NOT NULL,
    marca_veiculo VARCHAR(50) NOT NULL,
    modelo_veiculo VARCHAR(50) NOT NULL,
    versao_veiculo VARCHAR(50) NOT NULL,
    ano_modelo INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    motivo_conclusao VARCHAR(255),
    data_criacao TIMESTAMP NOT NULL,
    data_atribuicao TIMESTAMP,
    data_conclusao TIMESTAMP
);