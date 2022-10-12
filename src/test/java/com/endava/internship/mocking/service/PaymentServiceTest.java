package com.endava.internship.mocking.service;

import com.endava.internship.mocking.model.Payment;
import com.endava.internship.mocking.model.Status;
import com.endava.internship.mocking.model.User;
import com.endava.internship.mocking.repository.PaymentRepository;
import com.endava.internship.mocking.repository.UserRepository;
import com.endava.internship.mocking.service.PaymentService;
import com.endava.internship.mocking.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<Payment> captor;

    private Double amount = 100.0;

    private Integer id = 1;

    private User user = new User(id, "Dmitry", Status.ACTIVE);

    private Payment payment = new Payment(user.getId(), amount, "Payment from user " + user.getName());

    private String message = "New Message";

    private UUID uuid = UUID.randomUUID();

    @Test
    void createPayment() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        final Payment paymentResult = paymentService.createPayment(user.getId(), amount);

        verify(validationService).validateUserId(user.getId());
        verify(validationService).validateAmount(amount);
        verify(userRepository).findById(id);
        verify(validationService).validateUser(user);
        verify(paymentRepository).save(captor.capture());

        final Payment captorResult = captor.getValue();

        assertAll(
                () -> assertThat(paymentResult).isNotNull(),
                () -> assertEquals(captorResult.getMessage(), paymentResult.getMessage()),
                () -> assertEquals(captorResult.getAmount(), paymentResult.getAmount()),
                () -> assertEquals(captorResult.getUserId(), paymentResult.getUserId())
        );
    }

    @Test
    void editMessage() {
        when(paymentRepository.editMessage(uuid, message)).thenReturn(payment);

        final Payment editPayment = paymentService.editPaymentMessage(uuid, message);

        assertEquals(payment, editPayment);

        verify(validationService).validatePaymentId(uuid);
        verify(validationService).validateMessage(message);
        verify(paymentRepository).editMessage(uuid, message);
    }

    @Test
    void getAllByAmountExceeding() {
        final List<Payment> payments = Arrays.asList(
                new Payment(1, 95.2, "Payment 1"),
                new Payment(2, 100.1, "Payment 2"),
                new Payment(3, 10.3, "Payment 3")
        );

        when(paymentRepository.findAll()).thenReturn(payments);

        final List<Payment> findedPayments = paymentService.getAllByAmountExceeding(amount);
        assertEquals(findedPayments, Arrays.asList(payments.get(1)));

        verify(paymentRepository).findAll();
    }
}
