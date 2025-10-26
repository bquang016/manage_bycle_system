package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Payment;
import com.example.managebyclesystem.model.Payment.PaymentStatus;
import com.example.managebyclesystem.model.RentalOrder;
import com.example.managebyclesystem.model.RentalOrder.ActiveStatus;
import com.example.managebyclesystem.repository.PaymentRepository;
import com.example.managebyclesystem.repository.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalOrderRepository rentalOrderRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RentalOrderRepository rentalOrderRepository) {
        this.paymentRepository = paymentRepository;
        this.rentalOrderRepository = rentalOrderRepository;
    }

    // thêm
    public void addPayment(Payment payment) {
        if (payment.getRentalOrder() == null || payment.getRentalOrder().getRentalOrderId() == 0) {
            throw new IllegalArgumentException("Vui lòng nhập mã đơn thuê hợp lệ.");
        }

        // check db
        RentalOrder rentalOrder = rentalOrderRepository.findById(payment.getRentalOrder().getRentalOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn thuê với ID: "
                        + payment.getRentalOrder().getRentalOrderId()));

        // check status
        if (rentalOrder.getRentalOrderActiveStatus() == ActiveStatus.DISABLE) {
            throw new IllegalArgumentException("Đơn thuê này đã bị vô hiệu hóa. Không thể thêm thanh toán.");
        }

        if (payment.getPaymentAmount() <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0.");
        }

        if (payment.getPaymentMethod() == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống.");
        }

        // set able
        payment.setRentalOrder(rentalOrder);
        payment.setPaymentStatus(PaymentStatus.Able);

        paymentRepository.save(payment);
        System.out.println("Thêm thanh toán thành công cho đơn ID: " + rentalOrder.getRentalOrderId());
    }


    // list able
    public List<Payment> getAllActivePayments() {
        return paymentRepository.findByPaymentStatus(Payment.PaymentStatus.Able);
    }

    // xóa
    public void deletePayment(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thanh toán với ID: " + paymentId));
        // check disable
        if (payment.getPaymentStatus() == Payment.PaymentStatus.Disable) {
            throw new IllegalArgumentException("Thanh toán đã bị vô hiệu hóa trước đó.");
        }
        // set disable
        payment.setPaymentStatus(Payment.PaymentStatus.Disable);
        paymentRepository.save(payment);

        System.out.println("Đã vô hiệu hóa thanh toán ID = " + paymentId);
    }


}
