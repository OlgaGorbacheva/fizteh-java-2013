package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.StringWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;

public class StorableUtils {

      static private String SIGNATURE_FILE_NAME = "signature.tsv";

      private static void typeCheck(Class<?> value1, Class<?> value2) throws ColumnFormatException {
            if (!value1.equals(value1)) {
                  throw new ColumnFormatException("Несовпадение типов столбца");
            }
      }

      public static void equalFormat(Storeable struct, List<Class<?>> list) throws ColumnFormatException {
            for (int i = 0; i < list.size(); i++) {
                  try {
                        typeCheck(list.get(i), struct.getColumnAt(i).getClass());
                  } catch (IndexOutOfBoundsException e) {
                        throw new ColumnFormatException("Несовпадение типов столбца");
                  }
            }
      }

      // public static void columnListCheck(List<Class<?>> list) throws
      // IllegalArgumentException{
      // for (int i = 0; i < list.size(); i++) {
      // if (list.get(i) == null) {
      // throw new
      // IllegalArgumentException("Недопустимые типы колонок таблицы");
      // }
      // }
      // }

      public static String writeToString(Table table, Storeable value) {
            XMLOutputFactory outFactory = XMLOutputFactory.newFactory();
            StringWriter str = new StringWriter();
            return null;
      }

      public static void setSignature(StorableTable table) throws IOException {
            File signFile = new File(table.getWorkingDirectory(), SIGNATURE_FILE_NAME);
            if (!signFile.exists()) {
                  if (!signFile.createNewFile()) {
                        throw new IOException("Невозможно создать файл для сигнатуры");
                  }
            }
            DataOutputStream output = new DataOutputStream(new FileOutputStream(signFile));
            int length = table.getColumnsCount();
            for (int i = 0; i < length; i++) {
                  output.writeUTF(StorableTypes.getName(table.getColumnType(i)));
            }
            output.close();
      }

      public static List<Class<?>> getSignature(StorableTable table) throws IllegalArgumentException, IOException {
            File signFile = new File(table.getWorkingDirectory(), SIGNATURE_FILE_NAME);
            if (!signFile.exists()) {
                  throw new IllegalArgumentException("Некорректный формат таблицы: файл с сигнатурой отсутствует");
            }
            if (signFile.length() == 0) {
                  throw new IllegalArgumentException("Некорректный формат таблицы: файл с сигнатурой пуст");
            }
            long fileLength = signFile.length();
            long position = 0;
            byte currentByte;
            List<Byte> typeNameByte = new ArrayList<>();
            byte[] typeName;
            String typeNameString;
            DataInputStream input = new DataInputStream(new FileInputStream(signFile));
            List<Class<?>> typesList = new ArrayList<>();
            while (position < fileLength) {
                  do {
                        currentByte = input.readByte();
                        position++;
                        if (currentByte != ' ') {
                              typeNameByte.add(currentByte);
                        }
                  } while (position < fileLength && currentByte != ' ');
                  typeName = new byte[typeNameByte.size()];
                  for (int i = 0; i < typeNameByte.size(); i++) {
                        typeName[i] = typeNameByte.get(i);
                  }
                  typeNameString = new String(typeName, StandardCharsets.UTF_8);
                  typesList.add(StorableTypes.getClass(typeNameString));
                  typeNameByte.clear();
            }
            input.close();
            return typesList;
      }
}
