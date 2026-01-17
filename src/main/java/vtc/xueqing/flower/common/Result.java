package vtc.xueqing.flower.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Unified response wrapper.
 * @param <T> payload type
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Status code: 200 = success; others = failure.
     */
    private Integer code;

    /**
     * Response message.
     */
    private String message;

    /**
     * Response data.
     */
    private T data;

    /**
     * Success response (no data).
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "Operation succeeded";
        return result;
    }

    /**
     * Success response (with data).
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "Operation succeeded";
        result.data = data;
        return result;
    }

    /**
     * Success response with custom message and data.
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = message;
        result.data = data;
        return result;
    }

    /**
     * Failure response.
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.code = 500;
        result.message = message;
        return result;
    }

    /**
     * Failure response with custom status code.
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }
}
