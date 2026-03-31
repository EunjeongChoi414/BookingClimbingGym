package com.project.services.payment;

import com.project.domain.exception.DomainException;
import com.project.domain.exception.DuplicatePendingOrderException;
import com.project.domain.exception.PaymentFailedException;
import com.project.domain.exception.UnusedPassExistsException;
import com.project.domain.gym.*;
import com.project.domain.user.UserRepository;
import com.project.services.gym.model.ConfirmedPassModel;
import com.project.services.payment.model.PrepareOrderModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final UserPassRepository userPassRepository;
    private final OrderRepository orderRepository;
    private final PaymentClient tossPaymentClient;
    private final Clock clock;

    public PaymentService(
            GymRepository gymRepository,
            UserRepository userRepository,
            UserPassRepository userPassRepository,
            OrderRepository orderRepository,
            PaymentClient tossPaymentClient,
            Clock clock) {
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.userPassRepository = userPassRepository;
        this.orderRepository = orderRepository;
        this.tossPaymentClient = tossPaymentClient;
        this.clock = clock;
    }

    /**
     * 1단계: 결제 준비
     */
    public PrepareOrderModel prepareOrder(String userId, String gymId, String passId) {

        Gym gym = gymRepository.getById(gymId);
        Pass pass = gym.getPassById(passId);

        if (orderRepository.existsPending(userId, passId)) {
            throw new DuplicatePendingOrderException();
        }

        if (!userPassRepository.isFullyUsed(userId, passId)) {
            throw new UnusedPassExistsException();
        }

        Order order = new Order(userId, passId, pass.getPrice(), LocalDateTime.now(clock));
        orderRepository.save(order);

        return new PrepareOrderModel(order.getId(), pass.getPrice(), pass.getName());
    }

    /**
     * 2단계: 결제 확인 & UserPass 발급
     * - 위변조 방지 검증 (userId, amount)
     * - 토스 confirm API 호출 (트랜잭션 외부)
     * - Order 완료 처리 & UserPass 생성
     */
    public ConfirmedPassModel confirmOrder(
            String userId, String gymId, String passId,
            String paymentKey, String orderId, BigDecimal clientAmount) {

        // 1. 위변조 방지 검증 (userId, amount, status, 만료 여부)
        Order order = orderRepository.getById(orderId);
        try {
            order.validateForConfirm(userId, clientAmount, LocalDateTime.now(clock));
        } catch (DomainException e) {
            fail(order);
            throw e;
        }

        //2. 토스 confirm API 호출 — DB 저장값(order.getAmount()) 사용 (클라이언트값 미사용)
        PaymentConfirmResult result = tossPaymentClient.confirm(paymentKey, orderId, order.getAmount());

        if (!result.isSuccess()) {
            fail(order);
            throw new PaymentFailedException(result.getErrorMessage());
        }

        // 3. 결제 완료 처리
        order.complete(paymentKey, result.getReceiptUrl());
        orderRepository.save(order);

        // 4. UserPass 발급
        Gym gym = gymRepository.getById(gymId);
        Pass pass = gym.getPassById(passId);

        UserPass userPass = new UserPass(pass, userId, LocalDate.now(clock));
        userPassRepository.add(userPass);

        return new ConfirmedPassModel(
                userPass.getId(),
                pass.getName(),
                userPass.getValidUntil(),
                userPass.getRemainingUses());
    }

    public void confirmFailOrder(String orderId) {
        Order order = orderRepository.getById(orderId);
        order.fail();
        orderRepository.save(order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void fail(Order order) {
        order.fail();
        orderRepository.save(order);
    }
}
