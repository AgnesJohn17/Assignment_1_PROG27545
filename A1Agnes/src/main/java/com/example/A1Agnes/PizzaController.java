// Student ID: 991783138
// Name: Agnes Sania John

package com.example.A1Agnes;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PizzaController {

    private final PizzaOrderService service;

    public PizzaController(PizzaOrderService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String form(Model model) {
        model.addAttribute("order", new PizzaOrder());

        // enums for dropdowns / checkboxes
        model.addAttribute("sizes", PizzaSize.values());
        model.addAttribute("crusts", CrustType.values());
        model.addAttribute("toppings", Topping.values());

        return "OrderFormat";
    }

    @PostMapping("/submit")
    public String submit(@Valid @ModelAttribute("order") PizzaOrder order,
                         BindingResult result,
                         Model model) {

        // if delivery selected, require address
        if (order.isDelivery()) {
            if (order.getDeliveryAddress() == null || order.getDeliveryAddress().trim().isEmpty()) {
                result.rejectValue("deliveryAddress", "deliveryAddress.required",
                        "Delivery address is required when delivery is selected");
            }
        }

        if (result.hasErrors()) {
            // must re-add enums when returning form
            model.addAttribute("sizes", PizzaSize.values());
            model.addAttribute("crusts", CrustType.values());
            model.addAttribute("toppings", Topping.values());
            return "OrderFormat";
        }

        service.calculate(order);
        model.addAttribute("order", order);
        return "OrderDetails";
    }

    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("orders", service.getOrders());
        return "OrderRepeated";
    }
}
