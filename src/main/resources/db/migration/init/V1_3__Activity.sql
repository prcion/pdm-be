create table activity
(
    id           bigint auto_increment
        primary key,
    created_date datetime     null,
    updated_date datetime     null,
    name         varchar(255) null,
    user_id      bigint       null
)
    engine = MyISAM;

create index FKdlw7jbugyjqx8prk4p95okqtq
    on activity (user_id);
