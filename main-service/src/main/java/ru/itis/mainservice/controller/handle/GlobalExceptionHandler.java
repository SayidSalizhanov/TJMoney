package ru.itis.mainservice.controller.handle;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.itis.mainservice.dto.response.exception.ExceptionMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String FILE_SIZE_EXCEEDED = "Размер файла превышает допустимый предел в 5MB";

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Страница не найдена");
        return modelAndView;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ModelAndView handleHttpClientErrorException(HttpClientErrorException exception) {
        if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || 
            exception.getStatusCode() == HttpStatus.FORBIDDEN) {
            return new ModelAndView("redirect:/login");
        }
        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("message", "Страница не найдена");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Произошла ошибка при обработке запроса");
        return modelAndView;
    }

    // обработчик для исключений, когда не можем преобразовать значение аргумента метода контроллера в ожидаемый тип
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", exception.getMessage());
        return modelAndView;
    }

    // обработчик для исключений, когда картинка слишком большая
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ModelAndView handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", FILE_SIZE_EXCEEDED);
        return modelAndView;
    }

    // обработчик для исключений, когда происходит ошибка валидации методов или параметров контроллера
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodValidationException(HandlerMethodValidationException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        List<ParameterValidationResult> ex = exception.getParameterValidationResults();
        for (ParameterValidationResult result : ex) {
            String parameterName = result.getMethodParameter().getParameterName();
            errors.put(parameterName, result.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList());
        }
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Ошибка валидации: " + errors);
        return modelAndView;
    }

    // обработчик для исключений, когда не проходит валидация объекта DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        });
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Ошибка валидации: " + errors);
        return modelAndView;
    }

    // обработчик для всех остальных исключений, которых не должно было быть
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView onAllExceptions(Exception exception) {
        exception.printStackTrace();
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", "Внутренняя ошибка сервера");
        return modelAndView;
    }
}
