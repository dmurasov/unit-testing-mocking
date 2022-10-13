package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BasicValidationServiceTest {

    private BasicValidationService basicValidation = new BasicValidationService();

    @Test
    void validateAmountIsGreaterThanZero_shouldNotThrowAnException() {
        assertDoesNotThrow(
                () -> basicValidation.validateAmount(0.1));
    }

    @Test
    void amount_ShouldThrowAnExceptionIfIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateAmount(null), "Amount must not be null");
    }

    @Test
    void amount_ShouldThrowAnExceptionIfIsSmallerThanZero() {
        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateAmount(-0.1), "Amount must be greater than 0");
    }

    @Test
    void paymentId_ShouldThrowAnExceptionIfIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validatePaymentId(null), "Payment id must not be null");
    }

    @Test
    void paymentId_ShouldNotThrowAnExceptionIfIsNotNull() {
        assertDoesNotThrow(
                () -> basicValidation.validatePaymentId(UUID.randomUUID()));
    }

    @Test
    void userId_ShouldThrowAnExceptionIfIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateUserId(null), "User id must not be null");
    }

    @Test
    void userId_ShouldNotThrowAnExceptionIfIsNotNull() {
        assertDoesNotThrow(() -> basicValidation.validateUserId(3));
    }

    @Test
    void userStatusThrowsIllegalArgumentExceptionIfIsNotActive() {
        final User user = new User(1, "John", Status.INACTIVE);

        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateUser(user),
                "User with id " + user.getId() + " not in ACTIVE status");
    }

    @Test
    void user_ShouldNotThrowAnExceptionIfStatusIsActive() {
        final User user = new User(1, "John", Status.ACTIVE);

        assertDoesNotThrow(() -> basicValidation.validateUser(user));
    }

    @Test
    void validateMessageIsNotNull_shouldNotThrowAnException() {
        assertDoesNotThrow(
                () -> basicValidation.validateMessage("Simple message"));
    }

    @Test
    void validateMessageIsNull_shouldThrowAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> basicValidation.validateMessage(null), "Payment message must not be null");
    }
}
