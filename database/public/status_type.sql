create table status_type
(
    id   integer not null
        constraint status_pk
            primary key,
    name varchar(16)
);

alter table status_type
    owner to shiwarai;

create unique index status_id_uindex
    on status_type (id);

create unique index status_name_uindex
    on status_type (name);

