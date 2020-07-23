package pt.isel.daw.exceptions.exception_handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.isel.daw.exceptions.AuthenticationException
import pt.isel.daw.exceptions.AuthorizationException
import pt.isel.daw.exceptions.representation.ProblemJson

@RestControllerAdvice
class ServiceExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(AuthorizationException::class)
    @ResponseBody
    fun handleAuthorizationException(ex: AuthorizationException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .of(ProblemJson("AuthorizationException", ex.name, ex.error, 403))

    @ExceptionHandler(AuthenticationException::class)
    @ResponseBody
    fun handleAuthenticationException(ex: AuthenticationException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .of(ProblemJson("AuthenticationException", ex.name, ex.error, 401))
}