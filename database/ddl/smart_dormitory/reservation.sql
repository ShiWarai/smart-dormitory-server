create table reservation
(
    id                int auto_increment
        primary key,
    subject_id        int      not null,
    resident_id       int      not null,
    reason            tinytext null,
    start_reservation datetime not null,
    end_reservation   datetime not null,
    constraint reservation_id_uindex
        unique (id),
    constraint reservation_resident_id_fk
        foreign key (resident_id) references resident (id)
            on update cascade on delete cascade,
    constraint reservation_subject_id_fk
        foreign key (subject_id) references subject (id)
            on update cascade on delete cascade
);

