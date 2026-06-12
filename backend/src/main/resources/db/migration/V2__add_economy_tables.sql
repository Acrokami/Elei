CREATE TABLE wallets (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT fk_wallets_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE shop_items (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    price       BIGINT NOT NULL,
    stock       INTEGER NOT NULL
);

CREATE TABLE user_inventories (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    item_id      BIGINT NOT NULL,
    purchased_at TIMESTAMP NOT NULL DEFAULT now(),
    is_used      BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_inventory_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_inventory_item FOREIGN KEY (item_id) REFERENCES shop_items(id)
);
