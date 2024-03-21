package map.lab11.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
