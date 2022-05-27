create table object
(
    id          serial
        constraint object_pk
            primary key,
    name        varchar(64)       not null,
    description text,
    type_id     integer           not null
        constraint object_object_type_id_fk
            references object_type
            on update restrict on delete restrict,
    status_id   integer default 0 not null
        constraint object_status_id_fk
            references status_type
            on update cascade on delete set default,
    room_number integer           not null
        constraint object_room_number_fk
            references room (number)
            on update cascade on delete cascade
);

alter table object
    owner to shiwarai;

create unique index object_id_uindex
    on object (id);

