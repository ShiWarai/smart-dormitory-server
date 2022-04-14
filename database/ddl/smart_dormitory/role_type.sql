create table role_type
(
    id    int                      not null
        primary key,
    name  char(16) default 'guest' not null,
    level int      default 0       not null,
    constraint role_type_name_uindex
        unique (name)
)
    charset = utf8mb4;

