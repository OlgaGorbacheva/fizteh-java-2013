package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.StringReader;
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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

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

      public static String writeToString(Table table, Storeable value) throws XMLStreamException, IOException,
                  IllegalArgumentException {
            StringWriter str = new StringWriter();
            try {
                  XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(str);
                  try {
                        writer.writeStartElement("row");
                        for (int i = 0; i < table.getColumnsCount(); i++) {
                              writer.writeStartElement("col");
                              if (value.getColumnAt(i) == null) {
                                    writer.writeStartElement("null");
                              } else {
                                    writer.writeCharacters(StorableTypes.getStringAt(value, i));
                              }
                              writer.writeEndElement();
                        }
                        writer.writeEndElement();
                  } finally {
                        writer.close();
                  }
            } finally {
                  str.close();
            }
            return str.toString();
      }

      public static Storeable writeToStorable(Table table, String value) throws XMLStreamException {
            StringReader str = new StringReader(value);
            Storeable returnValue;
            try {
                  XMLStreamReader reader = XMLInputFactory.newFactory().createXMLStreamReader(str);
                  try {
                        if (!reader.hasNext()) {
                              throw new ParseException("Входная cтрока пуста", 0);
                        }
                        if (!reader.getLocalName().equals("row")) {
                              throw new ParseException("Формат таблицы задан неверно", reader.getLocation()
                                          .getCharacterOffset());
                        }
                        returnValue = new Storable(table);
                        int index = 0;
                        while (reader.hasNext()) {
                              reader.nextTag();
                              if (!reader.getLocalName().equals("col")) {
                                    if (!reader.isEndElement()) {
                                          if (!reader.getLocalName().equals("null")) {
                                                throw new ParseException("Формат таблицы задан неверно", reader
                                                            .getLocation().getCharacterOffset());
                                          } 
                                          index++;
                                          continue;
                                    }
                                    if (reader.getLocalName().equals("row")) {
                                          break;
                                    }
                                    continue;
                              }
                              if (reader.next() == XMLStreamConstants.CHARACTERS) {
                                    String columnValue = reader.getText();
                                    returnValue.setColumnAt(index, StorableTypes.getValueAt(columnValue, table, index));
                              } else {
                                    reader.nextTag();
                                    if (!reader.getLocalName().equals("null")) {
                                          throw new ParseException("Формат таблицы задан неверно", reader.getLocation().getCharacterOffset());
                                    }
                              }
                        }
                  } finally {
                        reader.close();
                  }
            } finally {
                  str.close();
            }
            return returnValue;
      }

      public static void setSignature(StorableTable table) throws IOException {
            File signFile = new File(table.getWorkingDirectory(), SIGNATURE_FILE_NAME);
            if (!signFile.exists()) {
                  if (!signFile.createNewFile()) {
                        throw new IOException("Невозможно создать файл для сигнатуры");
                  }
            }
            DataOutputStream output = new DataOutputStream(new FileOutputStream(signFile));
            try {
                  int length = table.getColumnsCount();
                  for (int i = 0; i < length; i++) {
                        output.writeUTF(StorableTypes.getName(table.getColumnType(i)));
                  }
            } finally {
                  output.close();
            }
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
            List<Class<?>> typesList = new ArrayList<>();
            DataInputStream input = new DataInputStream(new FileInputStream(signFile));
            try {
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
            } finally {
                  input.close();
            }
            return typesList;
      }
}
