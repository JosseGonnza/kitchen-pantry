-- Tabla de productos
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Tabla de lotes
CREATE TABLE IF NOT EXISTS batches (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   product_id BIGINT NOT NULL,
   quantity INT NOT NULL,
   expiry_date DATE NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
    );

-- Índices para optimizar búsquedas
CREATE INDEX IF NOT EXISTS idx_batches_product_id ON batches(product_id);
CREATE INDEX IF NOT EXISTS idx_batches_expiry_date ON batches(expiry_date);