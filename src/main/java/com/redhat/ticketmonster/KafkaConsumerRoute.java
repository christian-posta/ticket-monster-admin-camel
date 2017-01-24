/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.ticketmonster;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@Component
public class KafkaConsumerRoute extends RouteBuilder {

    private String[] topics = {
            "tm-orders.ticketmonster.Booking",
            "tm-orders.ticketmonster.Ticket",
    };

    @Override
    public void configure() throws Exception {
        from("kafka:kafka:9092?groupId=admin-camel&autoOffsetReset=earliest&consumersCount=1&topic="
                + String.join(",", topics))
                .log("new message.. about to process it from ${header[kafka.TOPIC]}")
                .recipientList().method(TopicRouter.class, "route");

        from("direct:Booking").log("got a message on Booking ").to("log:foo?showAll=true")
                .bean("payloadMapper").log("SQL statement: ${body}")
                .to("sql:insert into Booking values (:#id, :#cancellationCode, :#contactEmail, :#createdOn, :#performance_id)?dataSource=#dataSource").log("yay, success");
        from("direct:Ticket").log("got a message on Ticket ").to("log:foo?showAll=true")
            .bean("payloadMapper").log("SQL statement: ${body}")
            .to("sql:insert into Ticket values (:#id, :#price, :#number, :#rowNumber, :#section_id, :#ticketCategory_id, :#tickets_id)?dataSource=#dataSource").log("yay, success!");

    }
}
