-- drop table if exists BOOKING;
--
-- drop table if exists COMMENT;
--
-- drop table if exists ITEM;
--
-- drop table if exists REQUEST;
--
-- drop table if exists USERS;


CREATE TABLE IF NOT EXISTS USERS
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS REQUEST
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        constraint pk_request primary key,
    description   VARCHAR(512),
    requestor_id BIGINT references USERS (ID) on delete cascade,
    creation_date TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS ITEM
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        constraint pk_item primary key,
    name         VARCHAR(255)                            NOT NULL,
    description  VARCHAR(255),
    is_available boolean,
    owner_id     bigint references USERS (ID) on delete cascade,
    request_id   bigint references REQUEST (ID)
)
;

CREATE TABLE IF NOT EXISTS BOOKING
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        constraint pk_booking primary key,
    start_date     TIMESTAMP WITHOUT TIME ZONE,
    end_date       TIMESTAMP WITHOUT TIME ZONE,
    item_id        BIGINT references ITEM (ID),
    booker_id      BIGINT references USERS (ID) on delete cascade,
    booking_status varchar(100)
)
;

CREATE TABLE IF NOT EXISTS COMMENT
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    text      varchar(1024),
    item_id   BIGINT references ITEM (ID),
    author_id BIGINT references USERS (ID) on delete cascade,
    created   TIMESTAMP WITHOUT TIME ZONE
)