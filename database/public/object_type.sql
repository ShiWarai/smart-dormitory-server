create table object_type
(
    id   serial
        constraint object_type_pk
            primary key,
    name varchar(32) not null
);

alter table object_type
    owner to shiwarai;

create unique index object_type_id_uindex
    on object_type (id);

create unique index object_type_name_uindex
    on object_type (name);

