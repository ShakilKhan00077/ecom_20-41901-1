package ecom.controller;

import ecom.domain.Customer;
import ecom.domain.User;
import ecom.dto.OrderDto;
import ecom.service.CustomerService;
import ecom.service.OrderService;
import ecom.service.ProductService;
import ecom.service.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.SQLException;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    public CustomerController(CustomerService customerService, OrderService orderService, ProductService productService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("customers", customerService.list());
        return "customer/list";
    }

    @RequestMapping("/create")
    public String create(Model model) {
        model.addAttribute("customer", new User());
        return "customer/create";
    }

    @RequestMapping("/store")
    public String store(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "customer/create";
        }
        customerService.create(customer);
        return "redirect:/customers/list";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("customerId") Long customerId, Model model) throws SQLException {
        model.addAttribute("customer", customerService.get(customerId));
        return "customer/edit";
    }

    @RequestMapping("/update")
    public String update(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "customer/edit";
        }
        customerService.update(customer);
        return "redirect:/customers/list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("customerId") Long customerId) {
        customerService.delete(customerId);
        return "redirect:/customers/list";
    }

    @RequestMapping("/order_create")
    public String createOrder(Model model) {
        model.addAttribute("orderDto", new OrderDto());
        model.addAttribute("products", productService.list());
        model.addAttribute("customers", customerService.list());
        return "orderbycus/create";
    }

    @RequestMapping("/store_order")
    public String store(@Valid @ModelAttribute("orderDto") OrderDto orderDto, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "orderbycus/create";
        }
        orderService.createOrder(orderDto);
        return "redirect:/users/list";
    }
}
