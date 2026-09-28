package dk.perfumeshop.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgument(IllegalArgumentException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error";
    }
}
