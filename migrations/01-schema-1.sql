CREATE TABLE chat (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    tg_chat_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE link (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    link TEXT NOT NULL UNIQUE,
    last_update TIMESTAMP NOT NULL,
    last_activity TIMESTAMP NOT NULL,
    open_issues_count INTEGER,
    answer_count INTEGER
);

CREATE TABLE chat_link (
    id SERIAL PRIMARY KEY NOT NULL,
    chat_id BIGINT,
    link_id BIGINT,
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES link(id)
)