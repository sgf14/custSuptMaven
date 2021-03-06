package com.prod.custSuptMaven.site.chat;
//class to encode and decode ChatMessage's(.java) to Binary stream and JSON utilizing Jackson Data Processor- see POM.XML dependency additions
//binary to json from server to browser.  reverse for incoming to server
//required to support WebSocket api so app can send and receive messages
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatMessageCodec implements Encoder.BinaryStream<ChatMessage>, 
	Decoder.BinaryStream<ChatMessage>{
	private static final Logger log = LogManager.getLogger();
	private static final ObjectMapper MAPPER = new ObjectMapper();
	static {
		MAPPER.findAndRegisterModules();
		MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		MAPPER.addMixInAnnotations(ChatMessage.class,
                ChatMessage.MixInForWebSocket.class);
	}
	
	@Override
	public void encode(ChatMessage chatMessage, OutputStream outputStream) throws EncodeException, IOException {
		log.entry();
		try {
			ChatMessageCodec.MAPPER.writeValue(outputStream, chatMessage);
		} catch(JsonGenerationException | JsonMappingException e) {
			throw new EncodeException(chatMessage, e.getMessage(), e);
		} finally {
			log.exit();
		}
	}
	
	public ChatMessage decode(InputStream inputStream) throws DecodeException, IOException {
		log.entry();
		try {
			return ChatMessageCodec.MAPPER.readValue(inputStream, ChatMessage.class);
		} catch(JsonParseException | JsonMappingException e) {
			throw new DecodeException((ByteBuffer)null, e.getMessage(), e);
		} finally {
			log.exit();
		}
	}
			
	@Override
	public void init(EndpointConfig endpointConfig) {
		
	}
	
	@Override
	public void destroy() {
		
	}

}
