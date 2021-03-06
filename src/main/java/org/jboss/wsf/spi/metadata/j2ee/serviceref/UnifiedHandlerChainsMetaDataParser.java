/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.wsf.spi.metadata.j2ee.serviceref;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static org.jboss.wsf.spi.metadata.ParserConstants.HANDLER_CHAINS;
import static org.jboss.wsf.spi.metadata.ParserConstants.J2EE_NS;
import static org.jboss.wsf.spi.metadata.ParserConstants.JAVAEE_NS;
import static org.jboss.wsf.spi.util.StAXUtils.match;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.wsf.spi.Messages;
import org.jboss.wsf.spi.metadata.AbstractHandlerChainsMetaDataParser;
import org.jboss.wsf.spi.util.StAXUtils;

/**
 * The parser for the unified metadata handler chains element
 * 
 * @author alessio.soldano@jboss.com
 * @since 26-Nov-2010
 */
public class UnifiedHandlerChainsMetaDataParser extends AbstractHandlerChainsMetaDataParser
{
   private UnifiedHandlerChainsMetaDataParser()
   {
      super();
   }
   
   public static UnifiedHandlerChainsMetaData parse(InputStream is) throws IOException
   {
      // http://java.sun.com/xml/ns/javaee/javaee_web_services_1_2.xsd
      try
      {
         XMLStreamReader xmlr = StAXUtils.createXMLStreamReader(is);
         return parse(xmlr);
      }
      catch (XMLStreamException e)
      {
         throw new IOException(e);
      }
   }
   
   public static UnifiedHandlerChainsMetaData parse(XMLStreamReader reader) throws XMLStreamException
   {
      int iterate;
      try
      {
         iterate = reader.nextTag();
      }
      catch (XMLStreamException e)
      {
         // skip non-tag elements
         iterate = reader.nextTag();
      }
      UnifiedHandlerChainsMetaData handlerChains = null;
      switch (iterate)
      {
         case END_ELEMENT : {
            // we're done
            break;
         }
         case START_ELEMENT : {

            if (match(reader, JAVAEE_NS, HANDLER_CHAINS) || match(reader, J2EE_NS, HANDLER_CHAINS))
            {
               UnifiedHandlerChainsMetaDataParser parser = new UnifiedHandlerChainsMetaDataParser();
               handlerChains = parser.parseHandlerChains(reader, reader.getNamespaceURI());
            }
            else
            {
               throw Messages.MESSAGES.unexpectedElement("handlers", reader.getLocalName());
            }
         }
      }
      return handlerChains;
   }

   @Override
   protected String getDescriptorForLogs()
   {
      return "handlers";
   }
}
