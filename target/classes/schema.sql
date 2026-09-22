CREATE TABLE IF NOT EXISTS pedidos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_unico        INT NOT NULL,
    pedido          VARCHAR(50) NOT NULL,
    cliente         VARCHAR(255),
    producto        VARCHAR(255),
    cantidad        DECIMAL(10,2) DEFAULT 0,
    supervisor      VARCHAR(100),
    estado          VARCHAR(50),
    ubigeo          VARCHAR(20) DEFAULT 'SIN UBIGEO',
    agencia         VARCHAR(255) DEFAULT 'SIN AGENCIA ASIGNADA',
    direccion       TEXT,
    codigo_venta    VARCHAR(50),
    linea           VARCHAR(100),
    observacion     TEXT,
    fecha           VARCHAR(20),
    fecha_iso       DATE,
    fecha_entrega   VARCHAR(20),
    fecha_entrega_iso DATE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_pedido (pedido),
    INDEX idx_ubigeo (ubigeo),
    INDEX idx_supervisor (supervisor),
    INDEX idx_fecha_iso (fecha_iso),
    INDEX idx_estado (estado)
);

CREATE TABLE IF NOT EXISTS hojas_carga (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_carga        VARCHAR(50) NOT NULL UNIQUE,
    fecha_hora      VARCHAR(30),
    fecha_filtro    DATE,
    chofer          VARCHAR(100),
    placa           VARCHAR(20),
    items_count     INT DEFAULT 0,
    espumas         INT DEFAULT 0,
    resortes        INT DEFAULT 0,
    data_json       TEXT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_fecha_filtro (fecha_filtro)
);

CREATE TABLE IF NOT EXISTS choferes (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS placas (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    placa           VARCHAR(20) NOT NULL UNIQUE
);
