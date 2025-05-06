package backend.academy.scrapper.Data.DTO.Errors;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
    public static ApiErrorResponse fromException(Exception ex, HttpStatus status) {
        return new ApiErrorResponse(
            status.getReasonPhrase(),
            String.valueOf(status.value()),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).toList()
        );
    }
}
