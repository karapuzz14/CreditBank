package ru.neostudy.creditbank.deal.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder errorDecoder = new Default();

  @Override
  public Exception decode(String methodKey, Response response) {
    switch (response.status()) {
      case 400: {

        ErrorResponse errorResponse;
        try (InputStream bodyIs = response.body()
            .asInputStream()) {
          ObjectMapper mapper = new ObjectMapper();
          errorResponse = mapper.readValue(bodyIs, ErrorResponse.class);

          switch (errorResponse.getCode()) {
            case "minor_user": {
              return new LaterBirthdateException(
                  errorResponse.getMessage(),
                  errorResponse.getTimestamp());
            }
            case "cc_denied": {
              return new DeniedException(
                  errorResponse.getMessage(),
                  errorResponse.getTimestamp());
            }
          }
        } catch (IOException e) {
          return new Exception(e.getMessage());
        }
      }
      default:
        return errorDecoder.decode(methodKey, response);
    }
  }
}