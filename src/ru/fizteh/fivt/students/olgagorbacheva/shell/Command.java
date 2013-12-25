package ru.fizteh.fivt.students.olgagorbacheva.shell;

import java.io.IOException;

public interface Command{
      
      void execute(String[]   args, State state) throws IOException, IllegalArgumentException;            
      String getName();           
      int getArgNumber();                   

}
