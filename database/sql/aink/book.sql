create table book
(
    id          bigint                             not null
        primary key,
    title       varchar(128)                       null,
    email       varchar(128)                       not null,
    outline     json                               null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create index book_email_time_index
    on book (email asc, update_time desc);

