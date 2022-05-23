create table room_type
(
    id   serial
        constraint room_type_pk
            primary key,
    name varchar(32) not null
);

alter table room_type
    owner to shiwarai;

create unique index room_type_id_uindex
    on room_type (id);

create unique index room_type_name_uindex
    on room_type (name);

