package ru.neostudy.creditbank.dossier.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {

    try (InputStream bodyIs = response.body()
        .asInputStream()) {

      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());

      ErrorResponse errorResponse = mapper.readValue(bodyIs, ErrorResponse.class);

      return new DefaultException(
          errorResponse.getTimestamp(),
          errorResponse.getCode(),
          errorResponse.getMessage(),
          errorResponse.getDetails()
      );

    } catch (IOException e) {
      return new IOException(e.getMessage());
    }
  }
}