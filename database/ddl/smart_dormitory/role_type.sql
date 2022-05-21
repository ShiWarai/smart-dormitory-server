create table role_type
(
    id   int         not null
        primary key,
    name varchar(16) not null,
    constraint role_type_id_uindex
        unique (id),
    constraint role_type_name_uindex
        unique (name)
)
    charset = utf8mb4;

