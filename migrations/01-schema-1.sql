CREATE TABLE IF NOT EXISTS chat (
    chat_id SERIAL PRIMARY KEY NOT NULL,
    tg_chat_id INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS link (
    link_id SERIAL PRIMARY KEY NOT NULL,
    link TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_link (
    chat_link_id SERIAL PRIMARY KEY NOT NULL,
    chat_id INTEGER,
    link_id INTEGER,
    FOREIGN KEY (chat_id) REFERENCES chat(chat_id),
    FOREIGN KEY (link_id) REFERENCES link(link_id)
);