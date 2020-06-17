package ro.ubb.sdi08.domain.validators;

public interface Validator<E> {
    void validate(E entity) throws ValidatorException;
}
