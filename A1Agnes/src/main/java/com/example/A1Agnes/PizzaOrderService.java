// Student ID: 991783138
// Name: Agnes Sania John

package com.example.A1Agnes;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaOrderService {

    private final List<PizzaOrder> orders = new ArrayList<>();

    // Business rule constants
    private static final double TOPPING_PRICE = 1.25;
    private static final double DELIVERY_FEE = 3.99;
    private static final double TAX_RATE = 0.13;
    private static final double DISCOUNT_RATE = 0.10;

    public void calculate(PizzaOrder order) {

        // 1) Base price from size
        double basePrice = switch (order.getSize()) {
            case SMALL -> 8.00;
            case MEDIUM -> 10.00;
            case LARGE -> 12.00;
        };

        // 2) Toppings cost (can be null if none selected)
        int toppingCount = (order.getToppings() == null) ? 0 : order.getToppings().size();
        double toppingCostPerPizza = toppingCount * TOPPING_PRICE;

        // 3) Pizza subtotal (before discount/tax/delivery)
        double pizzaSubtotal = (basePrice + toppingCostPerPizza) * order.getQuantity();
        order.setSubtotal(pizzaSubtotal);

        // 4) Discount (only on pizza subtotal, only if quantity > 3)
        double discount = (order.getQuantity() > 3) ? pizzaSubtotal * DISCOUNT_RATE : 0.0;
        order.setDiscountAmount(discount);

        // 5) Tax applies to (pizza subtotal - discount) AND delivery fee (if delivery)
        double taxableAmount = pizzaSubtotal - discount;
        if (order.isDelivery()) {
            taxableAmount += DELIVERY_FEE;
        }

        double tax = taxableAmount * TAX_RATE;
        order.setTax(tax);

        // 6) Total
        double total = (pizzaSubtotal - discount) + tax;
        if (order.isDelivery()) {
            total += DELIVERY_FEE;
        }
        order.setTotal(total);

        // Save order in memory
        orders.add(order);
    }

    public List<PizzaOrder> getOrders() {
        return orders;
    }
}
