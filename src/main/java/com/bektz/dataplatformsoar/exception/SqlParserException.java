package com.bektz.dataplatformsoar.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SqlParserException extends RuntimeException{

    private static final long serialVersionUID = -1099039459759769980L;

    private Throwable cause = null;

    public SqlParserException() {
    }

    public SqlParserException(String arg0) {
        super(arg0);
    }

    public SqlParserException(Throwable arg0) {
        this.cause = arg0;
    }

    public SqlParserException(String arg0, Throwable arg1) {
        super(arg0);
        this.cause = arg1;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
    @Override
    public void printStackTrace() {
        this.printStackTrace(System.err);
    }
    @Override
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (this.cause != null) {
            pw.println("Caused by:");
            this.cause.printStackTrace(pw);
        }

    }

    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (this.cause != null) {
            ps.println("Caused by:");
            this.cause.printStackTrace(ps);
        }

    }


}
