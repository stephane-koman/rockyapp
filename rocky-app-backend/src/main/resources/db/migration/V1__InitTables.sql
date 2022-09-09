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
    mail      VARCHAR(250)             NOT NULL,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    role_id    BIGINT,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,

    unique (username, mail),
    CONSTRAINT pk_users PRIMARY KEY (id)
);
ALTER TABLE users
    ADD CONSTRAINT fk_users_reference_roles FOREIGN KEY (role_id) REFERENCES roles (id);

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
    name            VARCHAR(50)              NOT NULL UNIQUE,
    description     TEXT,
    price           DECIMAL,
    image           BYTEA,

    product_type_id BIGINT                   NOT NULL,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE,
    deleted_at      TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_products PRIMARY KEY (product_id)
);
ALTER TABLE products
    ADD CONSTRAINT fk_products_reference_product_types FOREIGN KEY (product_type_id) REFERENCES product_types (id);

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

CREATE TYPE mesure AS ENUM ('l', 'cl', 'ml');
CREATE TABLE volumes
(
    id          BIGSERIAL,
    quantity    BIGINT                   NOT NULL,
    mesure      mesure                   NOT NULL DEFAULT 'ml',
    description TEXT,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_volumes PRIMARY KEY (id)
);

CREATE TABLE invoices
(
    invoice_id  VARCHAR(250),
    price       DECIMAL,

    user_id     BIGINT                   NOT NULL,
    customer_id VARCHAR(250)             NOT NULL,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_invoices PRIMARY KEY (invoice_id)
);
ALTER TABLE invoices
    ADD CONSTRAINT fk_invoices_reference_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE invoices
    ADD CONSTRAINT fk_invoices_reference_customers FOREIGN KEY (customer_id) REFERENCES customers (customer_id);

CREATE TABLE invoice_items
(
    invoice_id VARCHAR(250),
    product_id VARCHAR(250),
    volume_id  BIGINT,

    quantity   BIGINT,
    price      DECIMAL,

    is_active   NUMERIC(1) DEFAULT 1,
    is_delete   NUMERIC(1) DEFAULT 0,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    deleted_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_invoice_items PRIMARY KEY (invoice_id, product_id, volume_id)
);
ALTER TABLE invoice_items
    ADD CONSTRAINT fk_invoice_items_reference_invoices FOREIGN KEY (invoice_id) REFERENCES invoices (invoice_id);
ALTER TABLE invoice_items
    ADD CONSTRAINT fk_invoice_items_reference_products FOREIGN KEY (product_id) REFERENCES products (product_id);
ALTER TABLE invoice_items
    ADD CONSTRAINT fk_invoice_items_reference_volumes FOREIGN KEY (volume_id) REFERENCES volumes (id);

CREATE TYPE paymentType AS ENUM ('CASH', 'CB', 'VIREMENT');
CREATE TABLE payments
(
    id           BIGSERIAL,
    payment_type paymentType  NOT NULL,

    invoice_id   VARCHAR(250) NOT NULL,

    CONSTRAINT pk_payments PRIMARY KEY (id)
);
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_reference_invoices FOREIGN KEY (invoice_id) REFERENCES invoices (invoice_id);