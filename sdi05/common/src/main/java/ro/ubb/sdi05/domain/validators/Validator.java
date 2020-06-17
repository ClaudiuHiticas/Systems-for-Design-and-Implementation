package ro.ubb.sdi05.domain.validators;

public interface Validator<E> {
    void validate(E entity) throws ValidatorException;
}
