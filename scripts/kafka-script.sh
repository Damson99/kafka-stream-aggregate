#==========WORDS STREAM==========
#netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=<your-ip>
#
#confluent local services start
#
#kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic streaming-words-topic
#
#kafka-console-producer --topic streaming-words-topic --broker-list localhost:9092
#
#confluent local services stop
#confluent local destroy


#==========LOYALTY==========
#netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=<your-ip>
#
#netsh interface portproxy add v4tov4 listenport=8081 listenaddress=0.0.0.0 connectport=8081 connectaddress=<your-ip>
#
#
#confluent local services start
#
#kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic loyalty-topic
#
#kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic loyalty-topic --from-beginning --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property print.key=true --property key.separator=":"
#
#confluent local destroy

#==========EMPLOYEE==========
#netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=<your-ip>
#
#netsh interface portproxy add v4tov4 listenport=8081 listenaddress=0.0.0.0 connectport=8081 connectaddress=<your-ip>
#
#confluent local services start
#
#kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic employees-topic
#
#kafka-avro-console-producer --broker-list localhost:9092 --topic employees-topic \
#--property value.schema='{"namespace": "com.cloud.kafkastream.web.v1.model","type": "record","name": "Employee","fields": [{"name": "id","type": "string"},{"name": "name","type": "string"},{"name": "department","type": "string"},{"name": "salary","type":"int"}]}'
#
#confluent local destroy

#==========SIMPLE INVOICE==========
#confluent local services start
#
#kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic simple-invoice-topic
#
#kafka-console-producer --broker-list localhost:9092 --topic simple-invoice-topic --property parse.key=true --property key.separator=":"
#
#confluent local destroy

#==========USER CLICK==========
netsh interface portproxy add v4tov4 listenport=9092 listenaddress=0.0.0.0 connectport=9092 connectaddress=<your-ip>

confluent local services start

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-click-topic

kafka-console-producer --broker-list localhost:9092 --topic user-click-topic \
--property parse.key=true --property key.separator=":"

confluent local destroy