
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id),
    order_reference VARCHAR(100) NOT NULL UNIQUE,
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    currency CHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL
        CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders(id),
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    currency CHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL
        CHECK (status IN (
            'PENDING', 'PROCESSING', 'SUCCEEDED',
            'FAILED', 'CANCELLED', 'PARTIALLY_REFUNDED',
            'REFUNDED'
        )),
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_attempts (
    id UUID PRIMARY KEY,
    payment_id UUID NOT NULL REFERENCES payments(id),
    attempt_number INTEGER NOT NULL CHECK (attempt_number > 0),
    status VARCHAR(30) NOT NULL
        CHECK (status IN (
            'PENDING', 'APPROVED', 'DECLINED', 'FAILED'
        )),
    provider_reference VARCHAR(150),
    failure_reason VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_payment_attempt_number
        UNIQUE (payment_id, attempt_number)
);

CREATE TABLE refunds (
    id UUID PRIMARY KEY,
    payment_id UUID NOT NULL REFERENCES payments(id),
    amount NUMERIC(19, 2) NOT NULL CHECK (amount > 0),
    currency CHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL
        CHECK (status IN (
            'PENDING', 'PROCESSING', 'SUCCEEDED', 'FAILED'
        )),
    provider_reference VARCHAR(150),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_orders_customer_created
    ON orders (customer_id, created_at DESC, id DESC);

CREATE INDEX idx_payments_status_created
    ON payments (status, created_at DESC, id DESC);

CREATE INDEX idx_payment_attempts_payment
    ON payment_attempts (payment_id, attempt_number DESC);

CREATE INDEX idx_refunds_payment
    ON refunds (payment_id, created_at DESC);