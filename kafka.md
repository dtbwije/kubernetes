# Kafka stuff

## Sending events to Kafka Using kafka scripts

```sh
/opt/kafka $ ./bin/kafka-console-producer.sh --bootstrap-server kafka:29092 --topic mobile-data
>"{"deviceId":"device-test-1","value":42}

```