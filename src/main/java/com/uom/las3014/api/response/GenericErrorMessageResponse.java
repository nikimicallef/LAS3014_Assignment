package com.uom.las3014.api.response;

public class GenericErrorMessageResponse {
    private String error;

    public GenericErrorMessageResponse(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenericErrorMessageResponse that = (GenericErrorMessageResponse) o;

        return error != null ? error.equalsIgnoreCase(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        return error != null ? error.hashCode() : 0;
    }
}
