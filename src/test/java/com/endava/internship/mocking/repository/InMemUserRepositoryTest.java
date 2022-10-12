package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InMemUserRepositoryTest {
    private InMemUserRepository userRepository = new InMemUserRepository();

    @Test
    void InMemUserRepositoryFindByIdWithNull_shouldThrowAnException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.findById(null),
                "User id must not be null");
    }

    @Test
    void findById_ShouldNotThrowAnExceptionAndReturnUser() {
        assertAll(
                () -> assertDoesNotThrow(() -> userRepository.findById(1)),
                () -> assertEquals(userRepository.findById(1),
                        Optional.of(new User(1, "John", Status.ACTIVE)))
        );
    }
}
