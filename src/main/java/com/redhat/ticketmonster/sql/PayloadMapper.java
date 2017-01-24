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
package com.redhat.ticketmonster.sql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by ceposta 
 * <a href="http://christianposta.com/blog>http://christianposta.com/blog</a>.
 */
@Component
public class PayloadMapper {

    public static final SimpleDateFormat mysqlDateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Map<String,Object> mapToSql(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = null;
        try {
            payloadMap = mapper.readValue(json, new TypeReference<Map<String,Object>>() {});
            Map<String,Object> payload =  (Map<String, Object>) payloadMap.get("payload");
            Map<String,Object> rc =  null;

            if (payload.containsKey("after")) {
                rc = (Map<String, Object>) payload.get("after");
                // quick hack to make the demo work
                if (rc.containsKey("createdOn")) {
                    Long createdOn = (Long) rc.get("createdOn");
                    Date createdOnDate = new Date(createdOn);
                    rc.put("createdOn", mysqlDateTimeFormatter.format(createdOnDate));
                }
            }else {
                rc = payload;
            }
            return rc;
        } catch (IOException e) {
            System.out.println("Sorry, could not map to a payload entity with json:  "+ json + "\n");
            e.printStackTrace();

        }

        return null;
    }
}
