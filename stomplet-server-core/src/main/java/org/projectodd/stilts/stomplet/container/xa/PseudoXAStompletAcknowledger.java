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

package org.projectodd.stilts.stomplet.container.xa;

import org.jboss.logging.Logger;
import org.projectodd.stilts.stomp.Acknowledger;

public class PseudoXAStompletAcknowledger implements Acknowledger {

    private static Logger log = Logger.getLogger(PseudoXAStompletAcknowledger.class);

    public PseudoXAStompletAcknowledger(PseudoXAStompletResourceManager resourceManager, Acknowledger acknowledger) {
        //log.errorf( "PXA ctor: " + resourceManager );
        this.resourceManager = resourceManager;
        this.acknowledger = acknowledger;
    }

    @Override
    public void ack() throws Exception {
        //log.errorf( "PXA ack()" );
        PseudoXAStompletTransaction tx = this.resourceManager.currentTransaction();
        if (tx != null) {
            tx.addAck( this.acknowledger );
        } else {
            this.acknowledger.ack();
        }
    }

    @Override
    public void nack() throws Exception {
        //log.errorf( "PXA nack()" );
        PseudoXAStompletTransaction tx = this.resourceManager.currentTransaction();
        if (tx != null) {
            tx.addNack(  this.acknowledger );
        } else {
            this.acknowledger.nack();
        }
    }

    private PseudoXAStompletResourceManager resourceManager;
    private Acknowledger acknowledger;


}
