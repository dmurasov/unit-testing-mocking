package com.endava.internship.mocking.repository;

import com.endava.internship.mocking.model.Payment;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemPaymentRepositoryTest {

    private InMemPaymentRepository inMemPaymentRepository = new InMemPaymentRepository();

    private Payment payment1 = new Payment(1, 100.0, "Payment 1");

    private Payment payment2 = new Payment(2, 20.0, "Payment 2");

    @Test
    void findById_shouldThrowAnExceptionIfIDIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> inMemPaymentRepository.findById(null), "Payment id must not be null");
    }

    @Test
    void findByID_ShouldReturnEmptyOptionalIfItIsNotValidID() {
        Optional<Payment> current = inMemPaymentRepository.findById(payment1.getPaymentId());
        assertEquals(Optional.empty(), current);
    }

    @Test
    void findByID_ShouldReturnPaymentIfIDIsValid() {
        inMemPaymentRepository.save(payment1);
        assertEquals(Optional.of(payment1), inMemPaymentRepository.findById(payment1.getPaymentId()));
    }

    @Test
    void findAll_ShouldReturnAListWithPayments() {
        List<Payment> payments = new ArrayList<>();

        payments.add(payment1);
        payments.add(payment2);

        inMemPaymentRepository.save(payment1);
        inMemPaymentRepository.save(payment2);

        assertThat(inMemPaymentRepository.findAll()).containsExactlyInAnyOrderElementsOf(payments);
    }

    @Test
    void save_ShouldThrowAnExceptionIfPaymentIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> inMemPaymentRepository.save(null), "Payment must not be null");
    }

    @Test
    void savePayment_ShouldThrowAnExceptionIfIsAnExistingOne() {
        inMemPaymentRepository.save(payment1);
        assertThrows(IllegalArgumentException.class,
                () -> inMemPaymentRepository.save(payment1),
                "Payment with id " + payment1.getPaymentId() + "already saved");
    }

    @Test
    void payment_ShouldBeSavedIfItIsValid() {
        assertEquals(payment1,  inMemPaymentRepository.save(payment1));
    }

    @Test
    void editMessageOfNotExistingPayment_ShouldThrowAnException() {
        UUID uuid = payment1.getPaymentId();

        assertThrows(NoSuchElementException.class,
                () -> inMemPaymentRepository.editMessage(uuid, "Message"),
                "Payment with id " + uuid + " not found");
    }

    @Test
    void editMessageOfExistingPayment_ShouldReturnAPaymentWithNewMessage() {
        inMemPaymentRepository.save(payment1);

        Payment changedPayment = inMemPaymentRepository
                .editMessage(payment1.getPaymentId(), "New Message");
        assertEquals(changedPayment.getMessage(), "New Message");
    }
}