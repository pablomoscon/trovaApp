package com.trovaApp.exception;

public class ErrorResponse {
    private String error;
    private String mensaje;
    private int status;

    public ErrorResponse(String error, String mensaje, int status) {
        this.error = error;
        this.mensaje = mensaje;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
