CREATE KEYSPACE IF NOT EXISTS stocks WITH replication = {'class':'SimpleStrategy', 'replication_factor':'1'} AND durable_writes=true;

CREATE TABLE IF NOT EXISTS stocks.codes(
    market_id text,
    code text,
    data_set text,
    description text,
    PRIMARY KEY (market_id, code)
) WITH CLUSTERING ORDER BY (code DESC);

CREATE TABLE IF NOT EXISTS stocks.stock_daily_time_series(
    market_id text,
    code text,
    year text,
    date timestamp,
    open_price double,
    high_price double,
    low_price double,
    close_price double,
    PRIMARY KEY((market_id, code, year), date)
);