package net.sh.rgface.exception;

/**
 * Created by DESTINY on 2017/10/22.
 */
public class ControllerException extends Exception {

    private String message;
    private String status = "ERROR";

    public ControllerException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

}
