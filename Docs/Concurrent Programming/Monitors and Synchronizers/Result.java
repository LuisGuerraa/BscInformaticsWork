import java.util.Optional;

public interface Result<T> {
    boolean isComplete();
    boolean tryCancel();
    Optional<T> get(int timeout) throws Exception;
}

