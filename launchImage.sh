# run the schema into the current twinpeaks db container
cat /home/darin/miles/twinpeaks/src/main/resources/postgres/dump_schema.sql | docker exec -i twinpeaks--postgres psql -U postgres

# build the jar
./gradlew build buildDocker

# remove the old container
docker rm twinpeaks

# run the docker
docker run -d --name twinpeaks -p 8090:8090 -p 8091:8091 com.miles/twinpeaks:0.0.1-SNAPSHOT

# populate the database
docker cp /home/darin/miles/twinpeaks/src/main/resources/postgres/populate_faux_receipt.sql twinpeaks--postgres:/populate_faux_receipt.sql
docker exec -it twinpeaks--postgres psql -h twinpeaks--postgres -U twinpeaks -d twinpeaks -a -f /populate_faux_receipt.sql
