package ru.fizteh.fivt.students.olgagorbacheva.shell;

import java.io.IOException;

public class ShellException extends IOException {

      private static final long serialVersionUID = -103335737416020285L;

      private final String message;
      
      public ShellException(String message) {
            this.message = message;
      }
      
      public String getLocalizedMessage() {
            return message;
      }
}
