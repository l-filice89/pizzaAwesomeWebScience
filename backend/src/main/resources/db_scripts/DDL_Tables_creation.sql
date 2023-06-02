CREATE TABLE IF NOT EXISTS pizzas
(
    id          INT            NOT NULL AUTO_INCREMENT,
    name        VARCHAR(30)    NOT NULL,
    ingredients VARCHAR(100)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS status
(
    name VARCHAR(20),
    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS orders
(
    id               INT          NOT NULL AUTO_INCREMENT,
    customer_name    VARCHAR(30)  NOT NULL,
    customer_address VARCHAR(100) NOT NULL,
    customer_phone   VARCHAR(20)  NOT NULL,
    status           VARCHAR(20)  NOT NULL DEFAULT 'ordered',
    PRIMARY KEY (id),
    FOREIGN KEY (status) REFERENCES status (name) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS order_details
(
    order_id INT NOT NULL,
    pizza_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    PRIMARY KEY (order_id, pizza_id),
    FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    FOREIGN KEY (pizza_id) REFERENCES pizzas (id) ON DELETE CASCADE
);