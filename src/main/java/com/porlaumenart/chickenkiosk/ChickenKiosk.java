package com.porlaumenart.chickenkiosk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * Java Swing 기반 치킨 주문 키오스크 예제.
 * 메뉴 선택, 수량 조절, 주문 내역 확인, 초기화 기능을 제공합니다.
 */
public class ChickenKiosk extends JFrame {

    private static final String[] MENU_NAMES = {
            "후라이드", "(H)후라이드", "뿌링클", "(H)뿌링클",
            "골드킹", "포테킹", "양념치킨", "맛초킹"
    };

    private static final int[] PRICES = {
            20000, 21000, 21000, 22000,
            20000, 22000, 21000, 21000
    };

    private static final String[] IMAGE_FILES = {
            "fried.jpg", "hot-fried.jpg", "bburinkle.jpg", "hot-bburinkle.jpg",
            "goldking.jpg", "poteking.jpg", "yangnyeom.jpg", "matchoking.jpg"
    };

    private final int[] quantities = new int[MENU_NAMES.length];
    private final JTextField[] quantityFields = new JTextField[MENU_NAMES.length];
    private final JTextArea orderArea = new JTextArea();

    public ChickenKiosk() {
        super("치킨 키오스크");
        configureFrame();
        setJMenuBar(createMenuBar());
        add(createMenuPanel(), BorderLayout.NORTH);
        add(createOrderPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
        refreshOrderArea();
    }

    private void configureFrame() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(760, 920);
        setMinimumSize(new Dimension(720, 760));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(255, 255, 225));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("메뉴");
        JMenuItem chicken = new JMenuItem("치킨");
        JMenuItem side = new JMenuItem("사이드");
        JMenuItem drink = new JMenuItem("음료");
        menu.add(chicken);
        menu.add(side);
        menu.add(drink);

        JMenu info = new JMenu("정보");
        JMenuItem about = new JMenuItem("프로젝트 정보");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Java Swing 기반 치킨 주문 키오스크\n학교 Java 프로그래밍 프로젝트",
                "프로젝트 정보",
                JOptionPane.INFORMATION_MESSAGE
        ));
        info.add(about);

        menuBar.add(menu);
        menuBar.add(info);
        return menuBar;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 14, 18));
        panel.setBackground(new Color(255, 255, 225));
        panel.setBorder(new EmptyBorder(18, 18, 12, 18));

        for (int i = 0; i < MENU_NAMES.length; i++) {
            panel.add(createMenuCard(i));
        }
        return panel;
    }

    private JPanel createMenuCard(int index) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 225));

        JButton menuButton = new JButton(MENU_NAMES[index]);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setFocusable(false);
        menuButton.setPreferredSize(new Dimension(130, 120));
        menuButton.setMaximumSize(new Dimension(140, 120));

        ImageIcon icon = loadOptionalMenuIcon(index);
        if (icon != null) {
            menuButton.setText("");
            menuButton.setIcon(icon);
            menuButton.setToolTipText(MENU_NAMES[index]);
        }

        JLabel nameLabel = new JLabel(MENU_NAMES[index]);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 15));

        JLabel priceLabel = new JLabel(String.format("%,d원", PRICES[index]));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        quantityPanel.setOpaque(false);

        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        JTextField quantityField = new JTextField("0", 3);
        quantityField.setHorizontalAlignment(JTextField.CENTER);
        quantityField.setEditable(false);
        quantityFields[index] = quantityField;

        minusButton.addActionListener(e -> changeQuantity(index, -1));
        plusButton.addActionListener(e -> changeQuantity(index, 1));
        menuButton.addActionListener(e -> changeQuantity(index, 1));

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityField);
        quantityPanel.add(plusButton);

        JButton confirmButton = new JButton("확인");
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(e -> refreshOrderArea());

        card.add(menuButton);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(quantityPanel);
        card.add(Box.createVerticalStrut(5));
        card.add(confirmButton);

        return card;
    }

    private ImageIcon loadOptionalMenuIcon(int index) {
        if (index < 0 || index >= IMAGE_FILES.length) {
            return null;
        }

        URL imageUrl = getClass().getResource("/images/" + IMAGE_FILES[index]);
        if (imageUrl == null) {
            return null;
        }

        ImageIcon original = new ImageIcon(imageUrl);
        Image scaled = original.getImage().getScaledInstance(120, 95, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 18, 0, 18));

        orderArea.setEditable(false);
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        orderArea.setMargin(new Insets(10, 10, 10, 10));
        orderArea.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(orderArea);
        scrollPane.setPreferredSize(new Dimension(680, 250));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 12));
        panel.setBackground(new Color(255, 255, 225));

        JButton orderButton = new JButton("주문");
        JButton resetButton = new JButton("초기화");
        JButton closeButton = new JButton("닫기");

        orderButton.addActionListener(e -> placeOrder());
        resetButton.addActionListener(e -> resetOrder());
        closeButton.addActionListener(e -> closeApplication());

        panel.add(orderButton);
        panel.add(resetButton);
        panel.add(closeButton);
        return panel;
    }

    private void changeQuantity(int index, int delta) {
        quantities[index] = Math.max(0, quantities[index] + delta);
        quantityFields[index].setText(String.valueOf(quantities[index]));
        refreshOrderArea();
    }

    private void refreshOrderArea() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%-14s %10s %8s %12s%n", "상품명", "단가", "수량", "합계"));
        builder.append("----------------------------------------------------\n");

        int grandTotal = 0;
        boolean hasItem = false;

        for (int i = 0; i < MENU_NAMES.length; i++) {
            if (quantities[i] <= 0) {
                continue;
            }

            hasItem = true;
            int subtotal = PRICES[i] * quantities[i];
            grandTotal += subtotal;
            builder.append(String.format(
                    "%-14s %,10d %8d %,10d원%n",
                    MENU_NAMES[i], PRICES[i], quantities[i], subtotal
            ));
        }

        if (!hasItem) {
            builder.append("선택된 메뉴가 없습니다.\n");
        }

        builder.append("----------------------------------------------------\n");
        builder.append(String.format("총 금액: %,d원%n", grandTotal));
        orderArea.setText(builder.toString());
    }

    private void placeOrder() {
        int total = calculateTotal();
        if (total == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "주문할 메뉴를 선택해 주세요.",
                    "주문 안내",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                orderArea.getText() + "\n주문되었습니다.\n이용해주셔서 감사합니다.",
                "주문 완료",
                JOptionPane.INFORMATION_MESSAGE
        );
        resetOrder();
    }

    private int calculateTotal() {
        int total = 0;
        for (int i = 0; i < MENU_NAMES.length; i++) {
            total += PRICES[i] * quantities[i];
        }
        return total;
    }

    private void resetOrder() {
        for (int i = 0; i < quantities.length; i++) {
            quantities[i] = 0;
            quantityFields[i].setText("0");
        }
        refreshOrderArea();
    }

    private void closeApplication() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "프로그램을 종료하시겠습니까?",
                "종료",
                JOptionPane.YES_NO_OPTION
        );
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // 기본 Look & Feel 사용
            }

            new ChickenKiosk().setVisible(true);
        });
    }
}
