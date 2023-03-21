package com.startup.comexcase_api.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.startup.comexcase_api.domain.exceptions.EntityConflictException;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.exceptions.InvalidArgumentException;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundExceptionHandler(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildApiException(exception));
    }

    @ExceptionHandler(EntityConflictException.class)
    public ResponseEntity<?> entityConflictExceptionHandler(EntityConflictException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildApiException(exception));
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<?> noPermissionExceptionHandler(NoPermissionException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildApiException(exception));
    }

    @ExceptionHandler({AuthenticationServiceException.class})
    public ResponseEntity<?> authenticationServiceExceptionHandler(AuthenticationServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildApiException(exception));
    }

    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<?> entityConflictExceptionHandler(InvalidArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildApiException(exception));
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> httpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeException exception) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(buildApiException(exception));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildApiException(exception));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(buildApiException(exception));
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String path = ex.getPath().stream().map(Reference::getFieldName).collect(Collectors.joining("."));

        ApiException apiException = ApiException.builder()
                .time(LocalDateTime.now())
                .message(String.format("Error in %s, because the property %s can't be assigned to %s.", path, ex.getTargetType(), ex.getTargetType().getSimpleName()))
                .build();

        return ResponseEntity
                .status(status)
                .body(apiException);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        }

        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return ResponseEntity
                .status(statusCode)
                .body(buildApiException(ex));
    }

    private ApiException buildApiException(Exception exception) {
        return ApiException.builder()
                .time(LocalDateTime.now())
                .message(exception.getMessage())
                .build();
    }
}
