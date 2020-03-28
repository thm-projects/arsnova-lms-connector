package de.thm.arsnova.connector.binding;

import java.time.Instant;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class InstantAdapter extends XmlAdapter<String, Instant> {
    @Override
    public Instant unmarshal(final String s) throws Exception {
        return Instant.parse(s);
    }

    @Override
    public String marshal(final Instant instant) throws Exception {
        return instant.toString();
    }
}
