DROP TABLE IF EXISTS messages;

create table public.messages
(
    id        uuid         not null
        constraint messages_pk
            primary key,
    from_user uuid         not null
        constraint messages_user_data_id_fk
            references public.user_data,
    to_user   uuid         not null
        constraint messages_user_data_id_fk_2
            references public.user_data,
    s3_key    varchar(100) not null,
    title     varchar,
    read     boolean

);




