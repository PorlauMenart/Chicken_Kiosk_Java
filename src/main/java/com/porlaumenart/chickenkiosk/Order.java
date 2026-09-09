package com.porlaumenart.chickenkiosk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 완료된 한 건의 주문 정보를 보관합니다. */
public class Order {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String orderNumber;
    private final LocalDateTime orderedAt;
    private final List<OrderItem> items;
    private final int totalAmount;
    private final String paymentMethod;
    private final Integer receivedAmount;
    private final Integer changeAmount;

    public Order(
            String orderNumber,
            LocalDateTime orderedAt,
            List<OrderItem> items,
            int totalAmount,
            String paymentMethod,
            Integer receivedAmount,
            Integer changeAmount
    ) {
        this.orderNumber = orderNumber;
        this.orderedAt = orderedAt;
        this.items = new ArrayList<>(items);
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.receivedAmount = receivedAmount;
        this.changeAmount = changeAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public String toReceiptText() {
        StringBuilder builder = new StringBuilder();
        builder.append("주문번호: ").append(orderNumber).append('\n');
        builder.append("주문시각: ").append(orderedAt.format(FORMATTER)).append('\n');
        builder.append("결제방법: ").append(paymentMethod).append('\n');
        builder.append("----------------------------------------\n");

        for (OrderItem item : items) {
            builder.append(String.format(
                    "%s x %d  %,d원%n",
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getSubtotal()
            ));
        }

        builder.append("----------------------------------------\n");
        builder.append(String.format("총 결제금액: %,d원%n", totalAmount));

        if (receivedAmount != null && changeAmount != null) {
            builder.append(String.format("받은 금액: %,d원%n", receivedAmount));
            builder.append(String.format("거스름돈: %,d원%n", changeAmount));
        }

        return builder.toString();
    }

    public String toHistoryText() {
        return toReceiptText() + "\n";
    }
}
