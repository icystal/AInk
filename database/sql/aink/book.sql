create table book
(
    id          bigint                             not null
        primary key,
    email       varchar(128)                       not null,
    book_detail json                               null,
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create index book_email_time_index
    on book (email asc, update_time desc);

