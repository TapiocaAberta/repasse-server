package org.jugvale.transfgov.resource.config.jsonview;
import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Irá configurar o formato de data em campos JSON.
 * 
 * @author william
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {

	final String FORMATO_DATA = "dd.MM.yyyy HH:mm:ss";

	private final ObjectMapper objectMapper;

	public JacksonConfig() {
		objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat(FORMATO_DATA));
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true); 
	}

	public ObjectMapper getContext(Class<?> type) {
		return objectMapper;
	}
}