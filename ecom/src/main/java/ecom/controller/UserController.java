package ecom.controller;

import ecom.domain.Product;
import ecom.domain.User;
import ecom.dto.UserDto;
import ecom.service.AddressService;
import ecom.service.CustomerService;
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
import java.beans.PropertyEditorSupport;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private final AddressService addressService;

    private final CustomerService customerService;
    private final ProductService productService;

    public UserController(UserService userService, AddressService addressService, CustomerService customerService, ProductService productService) {
        this.userService = userService;
        this.addressService = addressService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        webDataBinder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException
            {
                LocalDate localDate = LocalDate.parse(text, dateFormatter);
                setValue(localDate);
            }
        });
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("users", userService.list());
        return "user/list";
    }

    @RequestMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new User());
        return "user/create";
    }

    @RequestMapping("/create_dto")
    public String createDto(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/create-dto";
    }

    @RequestMapping("/store_dto")
    public String store(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "user/create-dto";
        }
        userService.create(userDto);
        return "redirect:/users/list";
    }

    @RequestMapping("/store")
    public String store(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "user/create";
        }
        user.setEnabled(true);
        user.setCreatedOn(LocalDate.now());
        /*userService.create(user);*/
        return "redirect:/users/list";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam("userId") Long userId, Model model) throws SQLException {
        model.addAttribute("user", userService.get(userId));
        return "user/edit";
    }

    @RequestMapping("/update")
    public String update(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        userService.update(user);
        return "redirect:/users/list";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("userId") Long userId) {
        userService.delete(userId);
        return "redirect:/users/list";
    }

    @RequestMapping("/order_create")
    public String createOrder(Model model) {
        model.addAttribute("products", productService.list());
        model.addAttribute("customers", customerService.list());
        return "user/order/create";
    }
    @RequestMapping("/store_order")
    public String store(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "user/order/create";
        }
        productService.create(product);
        return "redirect:/user/list";
    }
}
