package com.porlaumenart.chickenkiosk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Java Swing 기반 치킨 주문 키오스크.
 * 메뉴 선택, 장바구니, 결제, 주문번호, 주문내역 기능을 제공합니다.
 */
public class ChickenKiosk extends JFrame {

    private final List<MenuItem> menuItems = Arrays.asList(
            new MenuItem("후라이드", 20000, "fried.jpg", "후라이드"),
            new MenuItem("핫후라이드", 21000, "hot-fried.jpg", "후라이드"),
            new MenuItem("뿌링클", 21000, "bburinkle.jpg", "시즈닝"),
            new MenuItem("핫뿌링클", 22000, "hot-bburinkle.jpg", "시즈닝"),
            new MenuItem("골드킹", 20000, "goldking.jpg", "양념"),
            new MenuItem("포테킹", 22000, "poteking.jpg", "시즈닝"),
            new MenuItem("양념치킨", 21000, "yangnyeom.jpg", "양념"),
            new MenuItem("맛초킹", 21000, "matchoking.jpg", "양념")
    );

    private final int[] quantities = new int[menuItems.size()];
    private final List<Order> orderHistory = new ArrayList<>();

    private final JPanel menuGrid = new JPanel(new GridLayout(0, 2, 12, 12));
    private final JTextArea cartArea = new JTextArea();
    private final JLabel totalLabel = new JLabel("총 금액  0원");
    private final JLabel nextOrderLabel = new JLabel("다음 주문번호  A-001");
    private final JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"전체", "후라이드", "시즈닝", "양념"});

    private int nextOrderSequence = 1;

    public ChickenKiosk() {
        super("Chicken Kiosk");
        configureFrame();
        setContentPane(createMainPanel());
        refreshMenuGrid();
        refreshCart();
    }

    private void configureFrame() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1060, 760);
        setMinimumSize(new Dimension(960, 700));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.setBackground(new Color(250, 248, 240));

        root.add(createHeaderPanel(), BorderLayout.NORTH);
        root.add(createMenuSection(), BorderLayout.CENTER);
        root.add(createCartSection(), BorderLayout.EAST);
        return root;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 4, 4, 4));

        JLabel title = new JLabel("CHICKEN KIOSK");
        title.setFont(new Font("Dialog", Font.BOLD, 27));

        JLabel subtitle = new JLabel("메뉴를 선택하고 장바구니에서 주문을 완료하세요.");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 13));

        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(3));
        titleBox.add(subtitle);

        nextOrderLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        panel.add(titleBox, BorderLayout.WEST);
        panel.add(nextOrderLabel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createMenuSection() {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setOpaque(false);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("카테고리"));
        categoryCombo.addActionListener(e -> refreshMenuGrid());
        filterPanel.add(categoryCombo);

        menuGrid.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(menuGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        section.add(filterPanel, BorderLayout.NORTH);
        section.add(scrollPane, BorderLayout.CENTER);
        return section;
    }

    private void refreshMenuGrid() {
        menuGrid.removeAll();
        String selectedCategory = String.valueOf(categoryCombo.getSelectedItem());

        for (int i = 0; i < menuItems.size(); i++) {
            MenuItem item = menuItems.get(i);
            if (!"전체".equals(selectedCategory) && !selectedCategory.equals(item.getCategory())) {
                continue;
            }
            menuGrid.add(createMenuCard(i, item));
        }

        menuGrid.revalidate();
        menuGrid.repaint();
    }

    private JPanel createMenuCard(int index, MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel imageLabel = new JLabel(loadMenuIcon(item), SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(190, 118));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel categoryLabel = new JLabel(item.getCategory());
        categoryLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        categoryLabel.setForeground(Color.GRAY);
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel(String.format("%,d원", item.getPrice()));
        priceLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        quantityPanel.setOpaque(false);
        JButton minusButton = new JButton("−");
        JLabel quantityLabel = new JLabel(String.valueOf(quantities[index]));
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        quantityLabel.setPreferredSize(new Dimension(30, 28));
        JButton plusButton = new JButton("+");

        minusButton.addActionListener(e -> {
            changeQuantity(index, -1);
            quantityLabel.setText(String.valueOf(quantities[index]));
        });
        plusButton.addActionListener(e -> {
            changeQuantity(index, 1);
            quantityLabel.setText(String.valueOf(quantities[index]));
        });

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(categoryLabel);
        infoPanel.add(Box.createVerticalStrut(4));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(quantityPanel);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        return card;
    }

    private Icon loadMenuIcon(MenuItem item) {
        URL imageUrl = getClass().getResource("/images/" + item.getImageFile());
        if (imageUrl == null) {
            return UIManager.getIcon("OptionPane.informationIcon");
        }

        ImageIcon original = new ImageIcon(imageUrl);
        Image scaled = original.getImage().getScaledInstance(190, 118, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JPanel createCartSection() {
        JPanel cartPanel = new JPanel(new BorderLayout(8, 8));
        cartPanel.setPreferredSize(new Dimension(355, 0));
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(215, 215, 215), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel cartTitle = new JLabel("장바구니");
        cartTitle.setFont(new Font("Dialog", Font.BOLD, 20));

        cartArea.setEditable(false);
        cartArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        cartArea.setBackground(new Color(250, 250, 250));
        cartArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane cartScroll = new JScrollPane(cartArea);
        cartScroll.setBorder(new LineBorder(new Color(230, 230, 230)));

        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Dialog", Font.BOLD, 18));

        JButton resetButton = new JButton("전체 초기화");
        JButton historyButton = new JButton("주문 내역");
        JButton paymentButton = new JButton("결제 / 주문");

        resetButton.addActionListener(e -> resetCart());
        historyButton.addActionListener(e -> showOrderHistory());
        paymentButton.addActionListener(e -> processPayment());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 7));
        buttonPanel.setOpaque(false);
        buttonPanel.add(paymentButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(resetButton);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.add(totalLabel, BorderLayout.NORTH);
        bottom.add(buttonPanel, BorderLayout.CENTER);

        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        cartPanel.add(bottom, BorderLayout.SOUTH);
        return cartPanel;
    }

    private void changeQuantity(int index, int delta) {
        quantities[index] = Math.max(0, quantities[index] + delta);
        refreshCart();
    }

    private void refreshCart() {
        StringBuilder builder = new StringBuilder();
        boolean hasItem = false;

        for (int i = 0; i < menuItems.size(); i++) {
            if (quantities[i] == 0) {
                continue;
            }
            hasItem = true;
            MenuItem item = menuItems.get(i);
            int subtotal = item.getPrice() * quantities[i];
            builder.append(String.format("%-10s x %d%n", item.getName(), quantities[i]));
            builder.append(String.format("  %,d원%n%n", subtotal));
        }

        if (!hasItem) {
            builder.append("장바구니가 비어 있습니다.\n\n");
            builder.append("왼쪽 메뉴의 + 버튼을 눌러\n메뉴를 추가해 주세요.");
        }

        cartArea.setText(builder.toString());
        totalLabel.setText(String.format("총 금액  %,d원", calculateTotal()));
    }

    private int calculateTotal() {
        int total = 0;
        for (int i = 0; i < menuItems.size(); i++) {
            total += menuItems.get(i).getPrice() * quantities[i];
        }
        return total;
    }

    private List<OrderItem> createCurrentOrderItems() {
        List<OrderItem> result = new ArrayList<>();
        for (int i = 0; i < menuItems.size(); i++) {
            if (quantities[i] > 0) {
                result.add(new OrderItem(menuItems.get(i), quantities[i]));
            }
        }
        return result;
    }

    private void processPayment() {
        int total = calculateTotal();
        if (total <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "장바구니에 메뉴를 먼저 추가해 주세요.",
                    "주문 안내",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Object[] options = {"카드 결제", "현금 결제", "취소"};
        int choice = JOptionPane.showOptionDialog(
                this,
                String.format("결제 금액은 %,d원입니다.\n결제 방법을 선택해 주세요.", total),
                "결제 방법",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            completeOrder("카드", null, null);
        } else if (choice == 1) {
            processCashPayment(total);
        }
    }

    private void processCashPayment(int total) {
        while (true) {
            String input = JOptionPane.showInputDialog(
                    this,
                    String.format("결제 금액: %,d원\n받은 금액을 입력해 주세요.", total),
                    "현금 결제",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                return;
            }

            try {
                int received = Integer.parseInt(input.replace(",", "").trim());
                if (received < total) {
                    JOptionPane.showMessageDialog(
                            this,
                            "받은 금액이 결제 금액보다 적습니다.",
                            "결제 오류",
                            JOptionPane.WARNING_MESSAGE
                    );
                    continue;
                }

                completeOrder("현금", received, received - total);
                return;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "금액은 숫자로 입력해 주세요.",
                        "입력 오류",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private void completeOrder(String paymentMethod, Integer receivedAmount, Integer changeAmount) {
        String orderNumber = String.format("A-%03d", nextOrderSequence);
        Order order = new Order(
                orderNumber,
                LocalDateTime.now(),
                createCurrentOrderItems(),
                calculateTotal(),
                paymentMethod,
                receivedAmount,
                changeAmount
        );

        orderHistory.add(order);
        nextOrderSequence++;

        JTextArea receipt = new JTextArea(order.toReceiptText() + "\n주문이 완료되었습니다.");
        receipt.setEditable(false);
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receipt.setBackground(UIManager.getColor("Panel.background"));

        JOptionPane.showMessageDialog(
                this,
                receipt,
                "주문 완료 - " + orderNumber,
                JOptionPane.INFORMATION_MESSAGE
        );

        resetCart();
        nextOrderLabel.setText("다음 주문번호  " + String.format("A-%03d", nextOrderSequence));
    }

    private void showOrderHistory() {
        if (orderHistory.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "완료된 주문 내역이 없습니다.",
                    "주문 내역",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder historyText = new StringBuilder();
        for (int i = orderHistory.size() - 1; i >= 0; i--) {
            historyText.append(orderHistory.get(i).toHistoryText());
            if (i > 0) {
                historyText.append("========================================\n");
            }
        }

        JTextArea historyArea = new JTextArea(historyText.toString(), 24, 46);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(historyArea),
                "주문 내역 (" + orderHistory.size() + "건)",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void resetCart() {
        Arrays.fill(quantities, 0);
        refreshCart();
        refreshMenuGrid();
    }

    private void closeApplication() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "키오스크를 종료하시겠습니까?",
                "프로그램 종료",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // 시스템 Look & Feel을 적용할 수 없는 경우 기본값을 사용합니다.
            }

            new ChickenKiosk().setVisible(true);
        });
    }
}
