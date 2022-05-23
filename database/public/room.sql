create table room
(
    number  integer not null,
    floor   integer not null,
    type_id integer not null
        constraint room_room_type_id_fk
            references room_type
            on update cascade on delete restrict
);

alter table room
    owner to shiwarai;

create unique index room_number_uindex
    on room (number);

