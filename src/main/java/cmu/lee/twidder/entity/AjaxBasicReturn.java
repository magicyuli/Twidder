package cmu.lee.twidder.entity;

/**
 * Created by lee on 4/5/16.
 */

/**
 * Response body indicating if requests are successful
 * for ajax requests that don't require specific returns.
 */
public class AjaxBasicReturn {
    private boolean success;
    private String message;

    public AjaxBasicReturn(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
