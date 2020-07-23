package pt.isel.daw.exceptions

abstract class BaseException(val name: String, val error: String) : RuntimeException(name)

/**
 * Controller Exceptions
 */
abstract class ControllerException(title: String, error: String) : BaseException(title, error)

class JsonPatchException(error: String) : ControllerException("JSON patch exception.", error)

/**
 * Service Exceptions
 */
abstract class ServiceException(title: String, error: String) : BaseException(title, error)

class AuthorizationException(error: String) : ServiceException("Authorization exception.", error)

class AuthenticationException(error: String) : ServiceException("Authentication exception.", error)


/**
 * Repository Exceptions
 */
abstract class RepositoryException(title: String, error: String) : BaseException(title, error)

class DatabaseAccessException(error: String) : RepositoryException("Database access exception.", error)

class UniqueAttributeException(error: String) : RepositoryException("Unique attribute exception.", error)

class EntityNotFoundException(error: String) : RepositoryException("Entity not found exception.", error)

class InvalidOperationException(error: String) : RepositoryException("Invalid operation exception.", error)

class InvalidTransitionException(error: String) : RepositoryException("Invalid transition exception.", error)

class ArchivedCommentException(error: String) : RepositoryException("Archived comment exception.", error)

class NoStateForProjectException(error: String) : RepositoryException("The project has no state provided", error);