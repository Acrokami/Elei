
CREATE TABLE users (
    user_id      BIGSERIAL PRIMARY KEY,
    username     VARCHAR(255) NOT NULL UNIQUE,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    total_experience BIGINT DEFAULT 0,
    current_streak   INTEGER DEFAULT 0,
    last_activity_date DATE,
    provider     VARCHAR(255) NOT NULL DEFAULT 'LOCAL',
    provider_id  VARCHAR(255),
    role         VARCHAR(255) DEFAULT 'USER',
    version      INTEGER
);

CREATE TABLE activities (
    activity_id       BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    points_multiplier INTEGER NOT NULL,
    unit_name         VARCHAR(255) NOT NULL,
    user_id           BIGINT NOT NULL,
    CONSTRAINT fk_activities_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE experience_logs (
    experience_id  BIGSERIAL PRIMARY KEY,
    "unitsCompleted" INTEGER,
    "earnedXp"       INTEGER,
    created_at     TIMESTAMP NOT NULL DEFAULT now(),
    user_id        BIGINT NOT NULL,
    activity_id    BIGINT NOT NULL,
    CONSTRAINT fk_experience_logs_user     FOREIGN KEY (user_id)     REFERENCES users(user_id),
    CONSTRAINT fk_experience_logs_activity FOREIGN KEY (activity_id) REFERENCES activities(activity_id)
);

CREATE TABLE quests (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT NOT NULL,
    event_type   VARCHAR(255) NOT NULL,
    target_count INTEGER NOT NULL,
    reward_xp    INTEGER NOT NULL,
    is_default   BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE user_quest_progress (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    quest_id      BIGINT NOT NULL,
    current_count INTEGER NOT NULL,
    is_completed  BOOLEAN NOT NULL,
    CONSTRAINT fk_uqp_user  FOREIGN KEY (user_id)  REFERENCES users(user_id),
    CONSTRAINT fk_uqp_quest FOREIGN KEY (quest_id) REFERENCES quests(id)
);
