--changeset Eiztrips:01-init-db

-- 1. сиквенс
CREATE SEQUENCE IF NOT EXISTS weekly_bosses_id_seq START WITH 1 INCREMENT BY 1;

-- 2. Таблица боссов (чистый BIGINT, без IDENTITY, так как за ID отвечает сиквенс выше)
CREATE TABLE weekly_bosses
(
    id         BIGINT       NOT NULL,
    version    BIGINT       DEFAULT 0 NOT NULL,
    name       VARCHAR(255) NOT NULL,
    max_hp     INTEGER      NOT NULL,
    current_hp INTEGER      NOT NULL,
    CONSTRAINT pk_weekly_bosses PRIMARY KEY (id)
);

-- 3. Таблица гильдий (ID заполняется вручную, ссылается на боссов)
CREATE TABLE guilds
(
    id      BIGINT NOT NULL,
    version BIGINT DEFAULT 0 NOT NULL,
    boss_id BIGINT,
    CONSTRAINT pk_guilds PRIMARY KEY (id),
    CONSTRAINT uc_guilds_boss UNIQUE (boss_id),
    CONSTRAINT fk_guilds_on_boss FOREIGN KEY (boss_id) REFERENCES weekly_bosses (id)
);

-- 4. Таблица пользователей (ID — это telegramId, заполняется вручную)
CREATE TABLE users
(
    telegram_id                BIGINT       NOT NULL,
    leetcode_username          VARCHAR(255) NOT NULL,
    experience                 INTEGER      DEFAULT 0 NOT NULL,
    guild_id                   BIGINT,
    last_submission_check_time TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (telegram_id),
    CONSTRAINT uc_users_leetcode_username UNIQUE (leetcode_username),
    CONSTRAINT fk_users_on_guild FOREIGN KEY (guild_id) REFERENCES guilds (id)
);

-- 5. Таблица сабмитов (ID — submissionId с LeetCode, заполняется вручную)
CREATE TABLE user_submissions
(
    submission_id   BIGINT                      NOT NULL,
    task_title      VARCHAR(255)                NOT NULL,
    task_slug       VARCHAR(255)                NOT NULL,
    task_difficulty VARCHAR(255)                NOT NULL,
    completed_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id         BIGINT                      NOT NULL,
    CONSTRAINT pk_user_submissions PRIMARY KEY (submission_id),
    CONSTRAINT fk_user_submissions_on_user FOREIGN KEY (user_id) REFERENCES users (telegram_id)
);