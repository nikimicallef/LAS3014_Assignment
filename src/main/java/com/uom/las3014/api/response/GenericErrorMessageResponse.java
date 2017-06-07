package com.uom.las3014.api.response;

/**
 * Similar to the {@link GenericMessageResponse} but used specifically in a failure case.
 * Contains 1 parameter;
 * - {@link GenericErrorMessageResponse#error}: Generic placeholder which describes the error occurred
 */
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
