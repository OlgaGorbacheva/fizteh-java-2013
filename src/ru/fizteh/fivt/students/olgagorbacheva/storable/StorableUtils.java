package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.EOFException;
import java.io.RandomAccessFile;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;

public class StorableUtils {

      static final String SIGNATURE_FILE_NAME = "signature.tsv";

      private static void typeCheck(Class<?> value1, Class<?> value2) throws ColumnFormatException {
            if (!value1.equals(value1)) {
                  throw new ColumnFormatException("Несовпадение типов столбца");
            }
      }

      public static void equalFormat(Storeable struct, List<Class<?>> list) throws ColumnFormatException {
            for (int i = 0; i < list.size(); i++) {
                  if (struct.getColumnAt(i) != null) {
                        typeCheck(list.get(i), struct.getColumnAt(i).getClass());
                  }
            }
      }

      public static String writeToString(Table table, Storeable value) throws XMLStreamException, IOException,
                  IllegalArgumentException {
            StringWriter str = new StringWriter();
            try {
                  XMLStreamWriter writer = XMLOutputFactory.newFactory().createXMLStreamWriter(str);
                  try {
                        writer.writeStartElement("row");
                        for (int i = 0; i < table.getColumnsCount(); i++) {
                              if (value.getColumnAt(i) == null) {
                                    writer.writeEmptyElement("null");
                              } else {
                                    writer.writeStartElement("col");
                                    writer.writeCharacters(StorableTypes.getStringAt(value, i));
                                    writer.writeEndElement();
                              }
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
                        reader.nextTag();
                        if (!reader.getLocalName().equals("row")) {
                              throw new ParseException("Формат таблицы задан неверно", reader.getLocation()
                                          .getCharacterOffset());
                        }
                        returnValue = new Storable(table);
                        int index = 0;
                        while (reader.hasNext()) {
                              reader.nextTag();
                              if (!reader.getLocalName().equals("col")) {
                                    if (reader.isEndElement() && reader.getLocalName().equals("row")) {
                                          break;
                                    }
                                    if (reader.getLocalName().equals("null")) {
                                          returnValue.setColumnAt(index, null);
                                          index++;
                                          reader.nextTag();
                                    } else {
                                          throw new ParseException("Неверно задан формат таблицы", reader.getLocation()
                                                      .getCharacterOffset());
                                    }
                              } else {
                                    if (reader.next() == XMLStreamConstants.CHARACTERS) {
                                          String columnValue = reader.getText();
                                          returnValue.setColumnAt(index,
                                                      StorableTypes.getValueAt(columnValue, table, index));
                                          index++;
                                    }
                                    if (!reader.hasNext()) {
                                          throw new ParseException(
                                                      "Не соблюден баланс открывающих и закрывающих тегов", reader
                                                                  .getLocation().getCharacterOffset());
                                    }
                                    reader.nextTag();
                                    if (!reader.isEndElement()) {
                                          throw new ParseException(
                                                      "Не соблюден баланс открывающих и закрывающих тегов", reader
                                                                  .getLocation().getCharacterOffset());
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
                        output.write((StorableTypes.getName(table.getColumnType(i)) + " ")
                                    .getBytes(StandardCharsets.UTF_8));
                  }
            } finally {
                  output.close();
            }
      }

      public static List<Class<?>> getSignature(File tableDir) throws IllegalArgumentException, IOException {
            File signFile = new File(tableDir, SIGNATURE_FILE_NAME);
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
                              if (currentByte != ' ' && currentByte != 0) {
                                    typeNameByte.add(currentByte);
                              }
                        } while (position < fileLength && currentByte != ' ');
                        if (typeNameByte.size() > 0 && typeNameByte.get(0).byteValue() != 0) {
                              typeName = new byte[typeNameByte.size()];
                              for (int i = 0; i < typeNameByte.size(); i++) {
                                    typeName[i] = typeNameByte.get(i);
                              }
                              typeNameString = new String(typeName, StandardCharsets.UTF_8);
                              typesList.add(StorableTypes.getClass(typeNameString));
                              typeNameByte.clear();
                        }
                  }
            } finally {
                  input.close();
            }
            return typesList;
      }

      public static void readTable(StorableTableProvider provider, StorableTable table) throws IOException,
                  java.text.ParseException {
            for (int i = 0; i < 16; i++) {
                  File currentDir = new File(table.getWorkingDirectory(), String.valueOf(i) + ".dir");
                  if (currentDir.exists() && currentDir.isDirectory() && currentDir.canRead()) {
                        for (int j = 0; j < 16; j++) {
                              File currentFile = new File(currentDir, String.valueOf(j) + ".dat");
                              if (currentFile.exists() && currentFile.isFile() && currentDir.canRead()) {
                                    read(provider, table, currentFile);
                              } else {
                                    if (currentFile.exists() && !(currentFile.isFile() && currentDir.canRead())) {
                                          throw new IOException("База данных неподходящего формата");
                                    }
                              }
                        }
                  } else {
                        if (currentDir.exists() && !(currentDir.isDirectory() && currentDir.canRead())) {
                              throw new IOException("База данных неподходящего формата");
                        }
                  }
            }
      }

      private static void read(StorableTableProvider provider, StorableTable table, File dataBaseFile)
                  throws IOException, java.text.ParseException {
            RandomAccessFile reader = new RandomAccessFile(dataBaseFile, "r");
            try {
                  if (reader.length() == 0) {
                        return;
                  }

                  List<Integer> offsets = new ArrayList<Integer>();
                  List<String> keys = new ArrayList<String>();
                  List<Storeable> values = new ArrayList<>();

                  try {
                        do {
                              List<Byte> keySymbols = new ArrayList<Byte>();
                              byte b = reader.readByte();
                              while (b != 0) {
                                    keySymbols.add(b);
                                    b = reader.readByte();
                              }
                              byte[] bytes = new byte[keySymbols.size()];
                              for (int i = 0; i < bytes.length; ++i) {
                                    bytes[i] = keySymbols.get(i);
                              }
                              keys.add(new String(bytes, "UTF-8"));

                              int offset = reader.readInt();
                              if ((offset <= 0) || (!offsets.isEmpty() && offset <= offsets.get(offsets.size() - 1))
                                          || (offset >= reader.length())) {
                                    throw new IOException("Неверное значение сдвигов");
                              }
                              offsets.add(offset);

                        } while (reader.getFilePointer() != offsets.get(0));
                  } catch (EOFException e) {
                        throw new IOException("Файл законнчен раньше времени");
                  }

                  offsets.add((int) reader.length());

                  for (int i = 0; i < keys.size(); ++i) {
                        byte[] bytes = new byte[offsets.get(i + 1) - offsets.get(i)];
                        reader.readFully(bytes);
                        values.add(provider.deserialize(table, new String(bytes, "UTF-8")));
                  }

                  for (int i = 0; i < keys.size(); ++i) {
                        if (!table.getStorage().put(keys.get(i), values.get(i))) {
                              throw new IOException("Значение ключа не уникально");
                        }
                  }
            } finally {
                  reader.close();
            }
      }

      public static void writeTable(StorableTableProvider provider, StorableTable table) throws IOException,
                  FileMapException {
            if (table.getStorage().getSize() == 0) {
                  return;
            }

            Map<Integer, Map<String, Storeable>> dataBase = new HashMap<>();
            Iterator<Entry<String, Storeable>> it = table.getStorage().getMap().entrySet().iterator();

            for (int i = 0; i < 256; i++) {
                  dataBase.put(i, new HashMap<String, Storeable>());
            }
            while (it.hasNext()) {
                  Entry<String, Storeable> elem = it.next();
                  int a = Math.abs(elem.getKey().hashCode() % 16);
                  int b = Math.abs(elem.getKey().hashCode() / 16 % 16);
                  dataBase.get(a * 16 + b).put(elem.getKey(), elem.getValue());
            }
            for (int i = 0; i < 256; i++) {
                  if (dataBase.get(i).size() != 0) {
                        File dir = new File(table.getWorkingDirectory(), String.valueOf(i / 16) + ".dir");
                        File file;
                        if (dir.exists()) {
                              if (!dir.isDirectory()) {
                                    throw new FileMapException("База данных неподходящего формата");
                              }
                              file = new File(dir, String.valueOf(i % 16) + ".dat");
                              if (file.exists()) {
                                    if (!file.isFile()) {
                                          throw new FileMapException("База данных неподходящего формата");
                                    }
                              } else {
                                    file.createNewFile();
                              }
                        } else {
                              dir.mkdir();
                              if (!dir.exists()) {
                                    throw new IOException("Проблема записи в базу данных");
                              }
                              file = new File(dir, String.valueOf(i % 16) + ".dat");
                              file.createNewFile();
                        }
                        write(provider, table, file, dataBase.get(i));
                  }
            }
            table.getStorage().clear();
      }

      private static void write(StorableTableProvider provider, StorableTable table, File dataBaseFile,
                  Map<String, Storeable> map) throws IOException {

            RandomAccessFile writer = new RandomAccessFile(dataBaseFile, "rw");
            try {
                  writer.setLength(0);

                  Integer offset = countOffset(map);
                  List<String> values = new ArrayList<String>();

                  Iterator<Entry<String, Storeable>> it = map.entrySet().iterator();

                  while (it.hasNext()) {
                        Entry<String, Storeable> elem = it.next();
                        String value = provider.serialize(table, elem.getValue());
                        writer.write(elem.getKey().getBytes("UTF-8"));
                        writer.write("\0".getBytes("UTF-8"));
                        writer.writeInt(offset);
                        offset += value.getBytes("UTF-8").length;
                        values.add(value);
                  }
                  for (int i = 0; i < values.size(); i++) {
                        writer.write(values.get(i).getBytes("UTF-8"));
                  }
            } catch (IOException e) {
                  writer.close();
                  throw new IOException("Ошибка записи файла", e);
            }

            writer.close();
      }

      private static Integer countOffset(Map<String, Storeable> storage) throws IOException {
            Integer curOffset = 0;
            Iterator<Entry<String, Storeable>> it = storage.entrySet().iterator();
            while (it.hasNext()) {
                  curOffset += it.next().getKey().getBytes("UTF-8").length + 1 + 4;
            }

            return curOffset;
      }

      public static String join(String[] args, String separator, Integer beg, Integer end) {
            StringBuilder str = new StringBuilder();
            boolean first = true;
            for (int i = beg; i < end; i++) {
                  if (!first) {
                        str.append(separator);
                  } else {
                        first = false;
                  }
                  str.append(args[i].toString());
            }
            return str.toString();

      }

}
