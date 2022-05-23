create table resident
(
    id          serial
        constraint resident_pk
            primary key,
    surname     varchar(50) not null,
    name        varchar(50) not null,
    patronymic  varchar(50),
    birthdate   date,
    student_id  char(7)     not null,
    pin_code    varchar(256),
    room_number integer
        constraint resident_room_number_fk
            references room (number)
            on update cascade on delete set null,
    role        varchar(16) not null
);

alter table resident
    owner to shiwarai;

create unique index resident_id_uindex
    on resident (id);

