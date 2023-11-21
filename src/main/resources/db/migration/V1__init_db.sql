create table account
(
    id      bigserial           primary key,
    balance numeric(20, 2) not null                             default 0,
    version timestamp DEFAULT CURRENT_TIMESTAMP
);
