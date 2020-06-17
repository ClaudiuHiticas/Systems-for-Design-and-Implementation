package ro.ubb.sdi08.domain.validators;

import java.math.BigInteger;
import java.util.Optional;

public class IdValidatorImpl implements IdValidator<BigInteger> {
    @Override
    public void validate(final BigInteger id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new IllegalArgumentException("Given id was null!"));
    }
}
