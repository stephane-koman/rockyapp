CREATE TABLE roles
(
    id          BIGSERIAL,
    name        VARCHAR(50)              NOT NULL UNIQUE,
    description TEXT,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id          BIGSERIAL,
    name        VARCHAR(50)              NOT NULL UNIQUE,
    description TEXT,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE users
(
    id         BIGSERIAL,
    name       VARCHAR(250)             NOT NULL,
    username   VARCHAR(250)             NOT NULL,
    password   VARCHAR(250)             NOT NULL,
    email      VARCHAR(250)             NOT NULL,

    is_active  NUMERIC(1) DEFAULT 1,
    is_delete  NUMERIC(1) DEFAULT 0,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,

    unique (username, email),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, role_id)
);
ALTER TABLE users_roles
    ADD CONSTRAINT fk_users_roles_reference_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_roles
    ADD CONSTRAINT fk_users_roles_reference_roles FOREIGN KEY (role_id) REFERENCES roles (id);

CREATE TABLE users_permissions
(
    user_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    CONSTRAINT pk_users_permissions PRIMARY KEY (user_id, permission_id)
);
ALTER TABLE users_permissions
    ADD CONSTRAINT fk_users_permissions_reference_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_permissions
    ADD CONSTRAINT fk_users_permissions_reference_permissions FOREIGN KEY (permission_id) REFERENCES permissions (id);

CREATE TABLE roles_permissions
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    CONSTRAINT pk_roles_permissions PRIMARY KEY (role_id, permission_id)
);
ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_roles_permissions_reference_roles FOREIGN KEY (role_id) REFERENCES roles (id);
ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_roles_permissions_reference_permissions FOREIGN KEY (permission_id) REFERENCES permissions (id);

CREATE TABLE volumes
(
    id          BIGSERIAL,
    quantity    BIGINT                   NOT NULL,
    mesure      VARCHAR(4)               NOT NULL DEFAULT 'ML',
    description TEXT,

    is_active   NUMERIC(1)                        DEFAULT 1,
    is_delete   NUMERIC(1)                        DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_volumes PRIMARY KEY (id)
);

CREATE TABLE product_types
(
    id          BIGSERIAL,
    name        VARCHAR(50)              NOT NULL UNIQUE,
    description TEXT,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_product_types PRIMARY KEY (id)
);

CREATE TABLE products
(
    product_id      VARCHAR(250),
    product_code    VARCHAR(250)             NOT NULL UNIQUE,
    name            VARCHAR(50)              NOT NULL,
    description     TEXT,
    price           DECIMAL,

    product_type_id BIGINT                   NOT NULL,
    volume_id       BIGINT                   NOT NULL,

    is_active       NUMERIC(1) DEFAULT 1,
    is_delete       NUMERIC(1) DEFAULT 0,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    deleted_at      TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_products PRIMARY KEY (product_id)
);
ALTER TABLE products
    ADD CONSTRAINT fk_products_reference_product_types FOREIGN KEY (product_type_id) REFERENCES product_types (id);
ALTER TABLE products
    ADD CONSTRAINT fk_products_reference_volumes FOREIGN KEY (volume_id) REFERENCES volumes (id);

CREATE TABLE documents
(
    id         BIGSERIAL,
    content    TEXT                     NOT NULL,
    filename   VARCHAR(100)             NOT NULL,
    mime_type  VARCHAR(10)              NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    product_id VARCHAR(250)             NOT NULL,

    CONSTRAINT pk_documents PRIMARY KEY (id)
);
ALTER TABLE documents
    ADD CONSTRAINT fk_documents_reference_products FOREIGN KEY (product_id) REFERENCES products (product_id);

CREATE TABLE customers
(
    customer_id VARCHAR(250),
    name        VARCHAR(250)             NOT NULL UNIQUE,
    email       VARCHAR(250),
    fixe        VARCHAR(20),
    mobile      VARCHAR(20),
    address     TEXT,
    description TEXT,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_customers PRIMARY KEY (customer_id)
);

CREATE TABLE orders
(
    order_id      VARCHAR(250),
    price         DECIMAL,

    user_id       BIGINT                   NOT NULL,
    customer_id   VARCHAR(250)             NOT NULL,

    due_date      DATE,
    delivery_date DATE,

    is_delivery   NUMERIC(1) DEFAULT 0,
    is_active     NUMERIC(1) DEFAULT 1,
    is_delete     NUMERIC(1) DEFAULT 0,

    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE,
    deleted_at    TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_reference_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_reference_customers FOREIGN KEY (customer_id) REFERENCES customers (customer_id);

CREATE TABLE order_items
(
    order_id   VARCHAR(250),
    product_id VARCHAR(250),

    quantity   BIGINT,
    price      DECIMAL,

    is_active  NUMERIC(1) DEFAULT 1,
    is_delete  NUMERIC(1) DEFAULT 0,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_order_items PRIMARY KEY (order_id, product_id)
);
ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_reference_orders FOREIGN KEY (order_id) REFERENCES orders (order_id);
ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_reference_products FOREIGN KEY (product_id) REFERENCES products (product_id);

CREATE TABLE payments
(
    id           BIGSERIAL,
    price        DECIMAL                  NOT NULL,
    payment_type VARCHAR(10)              NOT NULL DEFAULT 'VIREMENT',

    order_id     VARCHAR(250)             NOT NULL,

    is_active    NUMERIC(1)                        DEFAULT 1,
    is_delete    NUMERIC(1)                        DEFAULT 0,

    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    deleted_at   TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_payments PRIMARY KEY (id)
);
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_reference_orders FOREIGN KEY (order_id) REFERENCES orders (order_id);