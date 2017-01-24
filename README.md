# Spring-Boot Camel QuickStart

This example demonstrates how you can use Apache Camel with Spring Boot
based on a [fabric8 Java base image](https://github.com/fabric8io/base-images#java-base-images)

The quickstart uses Spring Boot to configure a little application that includes a Camel
route that triggeres a message every 5th second, and routes the message to a log.


### Building

The example can be built with

    mvn clean install


### Running the example locally

The example can be run locally using the following Maven goal:

    mvn spring-boot:run

Note, you'll need to be able to connect to Kafka (we use the new-consumer impl, so don't need to directly access Zookeeper) so if you run locally make sure kafka is available at `kafka:9092` or change the `from("kafka:xxxx)"` part of the route.  


### Build and run with Docker

```
mvn clean package -Pf8-build docker:build
docker run -it --rm fabric8/ticket-monster-admin-camel:2.0-SNAPSHOT
```


## Drop constraints
Since we're relying on eventual consistency to make the booking/ticket from the Orders context and the Admin context consistent, we will need to relax the assumption that all bookings and tickets originate within a single database and thus relax any foreign key constraints around ticket/bookings. To do this, log into the admin mysql instance and:


```
mysql> show create table Ticket;
mysql> alter table Ticket drop foreign key FKolbt9u28gyshci6ek9ep0rl5d;
```

This will allow us to insert Ticket record without there being a Booking record yet as the CDC process will eventually update the rows for Booking. 

### More details

You can find more details about running this [quickstart](http://fabric8.io/guide/quickstarts/running.html) on the website. This also includes instructions how to change the Docker image user and registry.

