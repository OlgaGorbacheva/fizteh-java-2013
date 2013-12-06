package ru.fizteh.fivt.students.olgagorbacheva.commands;

import java.io.IOException;

public interface TableCommand{
      
      public void execute(String args[], TableState state) throws IOException, IllegalArgumentException;            
      public String getName();           
      public int getArgNumber();                   

}
