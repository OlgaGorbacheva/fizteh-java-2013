package ru.fizteh.fivt.students.olgagorbacheva.commands;

import java.io.IOException;

public interface ProviderCommand {
      
      public void execute(String args[], ProviderState state) throws IOException, IllegalArgumentException;            
      public String getName();           
      public int getArgNumber();   
}
