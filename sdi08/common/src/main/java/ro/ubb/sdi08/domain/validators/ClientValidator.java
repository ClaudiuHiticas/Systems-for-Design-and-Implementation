package ro.ubb.sdi08.domain.validators;

import ro.ubb.sdi08.domain.Client;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ClientValidator implements Validator<Client> {
    private final static Predicate<Client> okClientName = (c) -> c.getName().length() > 0 &&
            c.getName().length() <= 255 &&
            !Objects.equals(c.getName(), "N/A");

    @Override
    public void validate(Client client) throws ValidatorException {
        Optional.ofNullable(client).orElseThrow(() -> new IllegalArgumentException("Given client data was null!"));
        Optional.of(client).filter(okClientName).orElseThrow(() -> new ValidatorException("Client name is invalid!"));
    }
}
