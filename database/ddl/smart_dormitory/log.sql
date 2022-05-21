create table log
(
    id          int auto_increment
        primary key,
    name        char(128) not null,
    description tinytext  null,
    author_id   int       null,
    constraint log_id_uindex
        unique (id),
    constraint log_resident_id_fk
        foreign key (author_id) references resident (id)
            on update cascade on delete set null
);

