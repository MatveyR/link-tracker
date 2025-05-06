package backend.academy.scrapper.Controllers;

import backend.academy.scrapper.Data.DTO.Errors.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            HttpStatus status = responseStatus.value();
            return ResponseEntity.status(status)
                .body(ApiErrorResponse.fromException(ex, status));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiErrorResponse.fromException(ex, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
