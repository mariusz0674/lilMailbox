DROP TABLE IF EXISTS messages;

CREATE TABLE public.messages
(
    id        UUID         NOT NULL,
    from_user UUID         NOT NULL
        CONSTRAINT messages_user_data_id_fk
            REFERENCES public.user_data,
    to_user   UUID         NOT NULL
        CONSTRAINT messages_user_data_id_fk_2
            REFERENCES public.user_data,
    s3_key    VARCHAR(100) NOT NULL,
    title     VARCHAR,
    read      BOOLEAN,
    sent_date DATE         NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT messages_pk PRIMARY KEY (id, sent_date)
) PARTITION BY RANGE (sent_date);

-- Tworzenie partycji inbox dla wiadomości z ostatnich 30 dni
CREATE TABLE public.messages_inbox PARTITION OF public.messages
    FOR VALUES FROM (CURRENT_DATE - INTERVAL '30 days') TO (CURRENT_DATE + INTERVAL '1 day');

-- Tworzenie partycji archive dla starszych wiadomości
CREATE TABLE public.messages_archive PARTITION OF public.messages
    FOR VALUES FROM (MINVALUE) TO (CURRENT_DATE - INTERVAL '30 days');

CREATE OR REPLACE FUNCTION route_message_to_partition() RETURNS TRIGGER AS $$
BEGIN
    IF TG_TABLE_NAME = 'messages' THEN
        IF NEW.sent_date >= CURRENT_DATE - INTERVAL '30 days' THEN
            EXECUTE 'INSERT INTO public.messages_inbox (id, from_user, to_user, s3_key, title, read, sent_date) VALUES ($1, $2, $3, $4, $5, $6, $7)'
                USING NEW.id, NEW.from_user, NEW.to_user, NEW.s3_key, NEW.title, NEW.read, NEW.sent_date;
        ELSE
            EXECUTE 'INSERT INTO public.messages_archive (id, from_user, to_user, s3_key, title, read, sent_date) VALUES ($1, $2, $3, $4, $5, $6, $7)'
                USING NEW.id, NEW.from_user, NEW.to_user, NEW.s3_key, NEW.title, NEW.read, NEW.sent_date;
        END IF;
        RETURN NULL; -- Record already inserted into appropriate partition, do not proceed with default insert
    END IF;
    RETURN NEW; -- Default case, should not be reached
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_route_message
    BEFORE INSERT ON public.messages
    FOR EACH ROW EXECUTE FUNCTION route_message_to_partition();

-- dodanie crona
CREATE EXTENSION IF NOT EXISTS pg_cron;

-- Utworzenie funkcji do przenoszenia wiadomości
CREATE OR REPLACE FUNCTION move_old_messages() RETURNS VOID AS $$
BEGIN
    -- Przenoszenie wiadomości starszych niż 30 dni z inbox do archive
    INSERT INTO public.messages_archive (id, from_user, to_user, s3_key, title, read, sent_date)
    SELECT id, from_user, to_user, s3_key, title, read, sent_date
    FROM public.messages_inbox
    WHERE sent_date < CURRENT_DATE - INTERVAL '30 days';

    -- Usuwanie przeniesionych wiadomości z inbox
    DELETE FROM public.messages_inbox
    WHERE sent_date < CURRENT_DATE - INTERVAL '30 days';
END;
$$ LANGUAGE plpgsql;

-- Utworzenie zadania cron do uruchamiania funkcji o północy
SELECT cron.schedule('move_old_messages', '0 0 * * *', 'SELECT move_old_messages();');

