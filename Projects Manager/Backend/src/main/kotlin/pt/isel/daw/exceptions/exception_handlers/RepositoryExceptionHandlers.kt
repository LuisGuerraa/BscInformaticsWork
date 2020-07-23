package pt.isel.daw.exceptions.exception_handlers

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.postgresql.util.PSQLException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pt.isel.daw.exceptions.*
import pt.isel.daw.exceptions.representation.ProblemJson

@RestControllerAdvice
class RepositoryExceptionHandlers : ResponseEntityExceptionHandler() {

    @ExceptionHandler(InvalidTransitionException::class)
    @ResponseBody
    fun handleInvalidTransitionException(ex: InvalidTransitionException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .of(ProblemJson("InvalidTransitionException", ex.name, ex.error, 406))

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseBody
    fun handleEntityNotFoundException(ex: EntityNotFoundException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .of(ProblemJson("EntityNotFoundException", ex.name, ex.error, 404))

    @ExceptionHandler(InvalidOperationException::class)
    @ResponseBody
    fun handleInvalidOperationException(ex: InvalidOperationException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .of(ProblemJson("InvalidOperationException", ex.name, ex.error, 400))

    @ExceptionHandler(ArchivedCommentException::class)
    @ResponseBody
    fun handleArchivedCommentException(ex: ArchivedCommentException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .of(ProblemJson("ArchivedCommentException", ex.name, ex.error, 409))

    @ExceptionHandler(UniqueAttributeException::class)
    @ResponseBody
    fun handleUniqueProjectException(ex: UniqueAttributeException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .of(ProblemJson("UniqueAttributeException", ex.name, ex.error, 409))

    @ExceptionHandler(NoStateForProjectException::class)
    @ResponseBody
    fun handleNoStateForProjectException(ex: NoStateForProjectException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .of(ProblemJson("NoStateForProjectException", ex.name, ex.error, 400))

    @ExceptionHandler(DatabaseAccessException::class)
    @ResponseBody
    fun handleDatabaseAccessException(ex: DatabaseAccessException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .of(ProblemJson("DatabaseAccessException", ex.name, ex.error, 500))


    @ExceptionHandler(MissingKotlinParameterException::class)
    @ResponseBody
    fun handleMissingKotlinParameterException(ex: MissingKotlinParameterException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .of(ProblemJson("MissingKotlinParameterException", "Bad parameter", ex.msg, 400))


    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException,
                                              headers: HttpHeaders,
                                              status: HttpStatus,
                                              request: WebRequest): ResponseEntity<Any> =
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .of(ProblemJson("HttpMessageNotReadableException", "Bad parameter type", ex.toString(), status.value())) as ResponseEntity<Any>

    @ExceptionHandler(PSQLException::class)
    @ResponseBody
    fun handlePSQLException(ex: PSQLException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .of(ProblemJson("PSQLException", "SQL Exception", ex.toString(), 500))

    @ExceptionHandler(ArrayIndexOutOfBoundsException::class)
    @ResponseBody
    fun handleArrayIndexOutOfBoundsException(ex: ArrayIndexOutOfBoundsException, request: WebRequest) =
            ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .of(ProblemJson("ArrayIndexOutOfBoundsException", "Error on SQL query format", ex.localizedMessage, 500))
}

fun ResponseEntity.BodyBuilder.of(problemJson: ProblemJson) =
        this.contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problemJson)