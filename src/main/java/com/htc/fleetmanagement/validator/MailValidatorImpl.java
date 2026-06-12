package com.htc.fleetmanagement.validator;

public class MailValidatorImpl implements jakarta.validation.ConstraintValidator<MailValidator, String> {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

	@Override
	public void initialize(MailValidator constraintAnnotation) {
}

	// Validates the email format using a regular expression
	@Override
	public boolean isValid(String email, jakarta.validation.ConstraintValidatorContext context) {
		if (email == null || email.isEmpty()) {
			return true; 
		}
		return email.matches(EMAIL_REGEX);
	}

}
