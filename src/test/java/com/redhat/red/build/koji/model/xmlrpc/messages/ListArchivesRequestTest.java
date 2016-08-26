/**
 * Copyright (C) 2015 Red Hat, Inc. (jcasey@redhat.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.red.build.koji.model.xmlrpc.messages;

import com.redhat.red.build.koji.model.xmlrpc.KojiTagQuery;
import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;
import org.commonjava.maven.atlas.ident.ref.SimpleProjectVersionRef;
import org.commonjava.rwx.estream.model.Event;
import org.commonjava.rwx.impl.estream.EventStreamGeneratorImpl;
import org.commonjava.rwx.impl.estream.EventStreamParserImpl;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by jdcasey on 8/9/16.
 */
public class ListArchivesRequestTest
        extends AbstractKojiMessageTest
{
    @Test
    public void verifyVsCapturedHttpRequest()
            throws Exception
    {
        EventStreamParserImpl eventParser = new EventStreamParserImpl();
        bindery.render( eventParser, new ListArchivesRequest( new SimpleProjectVersionRef( "org.foo", "bar", "1.1" ) ) );

        List<Event<?>> objectEvents = eventParser.getEvents();
        eventParser.clearEvents();

        List<Event<?>> capturedEvents = parseEvents( "listArchives-request-0.xml" );

        assertEquals( objectEvents, capturedEvents );
    }

    @Test
    public void roundTrip()
            throws Exception
    {
        EventStreamParserImpl eventParser = new EventStreamParserImpl();

        int buildId = 422953;
        ProjectVersionRef gav = new SimpleProjectVersionRef( "org.foo", "bar", "1.1" );
        bindery.render( eventParser, new ListArchivesRequest( gav ) );

        List<Event<?>> objectEvents = eventParser.getEvents();
        EventStreamGeneratorImpl generator = new EventStreamGeneratorImpl( objectEvents );

        ListArchivesRequest parsed = bindery.parse( generator, ListArchivesRequest.class );
        assertNotNull( parsed );

        assertThat( parsed.getQuery().getGav(), equalTo( gav ) );
    }

    @Test
    public void renderXML()
            throws Exception
    {
        int buildId = 422953;
        String xml = bindery.renderString( new ListArchivesRequest( new SimpleProjectVersionRef( "org.foo", "bar", "1.1" ) ) );
        System.out.println( xml );
    }
}
