FROM postgres:13

# Install pg_cron
RUN apt-get update && \
    apt-get install -y postgresql-13-cron && \
    apt-get clean

# Copy custom configuration file
COPY postgresql.conf /etc/postgresql/postgresql.conf

# Include custom configuration file in default configuration
RUN echo "include '/etc/postgresql/postgresql.conf'" >> /usr/share/postgresql/postgresql.conf.sample

# Create pg_cron extension during initialization
COPY create_pg_cron_extension.sql /docker-entrypoint-initdb.d/
