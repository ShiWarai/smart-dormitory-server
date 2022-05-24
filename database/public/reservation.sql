create table reservation
(
    id                serial
        constraint reservation_pk
            primary key,
    object_id         integer   not null
        constraint reservation_object_id_fk
            references object
            on update cascade on delete cascade,
    resident_id       integer   not null
        constraint reservation_resident_id_fk
            references resident
            on update cascade on delete cascade,
    reason            text,
    start_reservation timestamp not null,
    end_reservation   timestamp not null
);

alter table reservation
    owner to shiwarai;

create unique index reservation_id_uindex
    on reservation (id);

