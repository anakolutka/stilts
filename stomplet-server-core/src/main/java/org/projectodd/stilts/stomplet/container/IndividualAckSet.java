/*
 * Copyright 2011 Red Hat, Inc, and individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projectodd.stilts.stomplet.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.projectodd.stilts.stomp.Acknowledger;

public class IndividualAckSet implements AckSet {

    public IndividualAckSet() {
        
    }

    @Override
    public void ack(String messageId) throws Exception {
        Acknowledger ack = this.acknowledgers.remove( messageId );
        if ( ack != null ) {
            ack.ack();
        }
    }

    @Override
    public void nack(String messageId) throws Exception {
        Acknowledger ack = this.acknowledgers.remove( messageId );
        if ( ack != null ) {
            ack.nack();
        }
    }

    @Override
    public void addAcknowledger(String messageId, Acknowledger acknowledger) {
        this.acknowledgers.put( messageId, acknowledger );
    }
    
    @Override
    public void close() {
        for ( Acknowledger each : acknowledgers.values() ) {
            try {
                each.nack();
            } catch (Exception e) {
                // ignore
            }
        }
    }
    
    private Map<String, Acknowledger> acknowledgers = new ConcurrentHashMap<String, Acknowledger>();
}
