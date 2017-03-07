package com.prod.custSuptMaven.site.converters;

import java.sql.Timestamp;
import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/* class notes, chap 24, pg 695-8. this package and class added by chap 24 to allow for timestamp conversion
 * within ticket entity.  and allow project to merge site/entity/TicketEntity and site/Ticket into single
 * site/entity/Ticket the same as other entities.  couldnt have been done before because of Instant not compaitible
 * w/ db.
 * 
 */
@Converter
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {
	@Override
    public Timestamp convertToDatabaseColumn(Instant instant)
    {
        return new Timestamp(instant.toEpochMilli());
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp)
    {
        return Instant.ofEpochMilli(timestamp.getTime());
    }

}
