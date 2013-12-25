package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;

public class DataTable extends AbstractTable<String, String> implements Table{

      public DataTable(String name, File directory) {
            super(name, directory);
      }
      
      public void readFile() throws IOException {
            for (int i = 0; i < 16; i++) {
                  File currentDir = new File(dataBaseDir, String.valueOf(i) + ".dir");
                  if (currentDir.exists() && currentDir.isDirectory() && currentDir.canRead()) {
                        for (int j = 0; j < 16; j++) {
                              File currentFile = new File(currentDir, String.valueOf(j) + ".dat");
                              if (currentFile.exists() && currentFile.isFile() && currentDir.canRead()) {
                                    read(currentFile);
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

      @SuppressWarnings("resource")
      public void read(File dataBaseFile) throws IOException {
            RandomAccessFile reader = new RandomAccessFile(dataBaseFile, "r");
            try {
                  if (reader.length() == 0) {
                        return;
                  }

                  List<Integer> offsets = new ArrayList<Integer>();
                  List<String> keys = new ArrayList<String>();
                  List<String> values = new ArrayList<String>();

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
                        values.add(new String(bytes, "UTF-8"));
                  }

                  for (int i = 0; i < keys.size(); ++i) {
                        if (!dataStorage.put(keys.get(i), values.get(i))) {
                              throw new IOException("Значение ключа не уникально");
                        }
                  }
            } catch (IOException e) {
                  reader.close();
                  throw new IOException("Ошибка чтения файла");
            }
            reader.close();
      }

      public void writeFile() throws FileNotFoundException, IOException, FileMapException {

            if (dataStorage.getSize() == 0) {
                  return;
            }

            Map<Integer, Map<String, String>> dataBase = new HashMap<Integer, Map<String, String>>();
            Iterator<Map.Entry<String, String>> it = dataStorage.getMap().entrySet().iterator();

            for (int i = 0; i < 256; i++) {
                  dataBase.put(i, new HashMap<String, String>());
            }
            while (it.hasNext()) {
                  Map.Entry<String, String> elem = it.next();
                  int a = Math.abs(elem.getKey().hashCode() % 16);
                  int b = Math.abs(elem.getKey().hashCode() / 16 % 16);
                  dataBase.get(a * 16 + b).put(elem.getKey(), elem.getValue());
            }
            for (int i = 0; i < 256; i++) {
                  if (dataBase.get(i).size() != 0) {
                        File dir = new File(dataBaseDir, String.valueOf(i / 16) + ".dir");
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
                        write(file, dataBase.get(i));
                  }
            }
            dataStorage.clear();
      }

      public void write(File dataBaseFile, Map<String, String> storage) throws IOException {

            RandomAccessFile writer = new RandomAccessFile(dataBaseFile, "rw");
            try {
                  writer.setLength(0);

                  Integer offset = countOffset(storage);
                  List<String> values = new ArrayList<String>();

                  Iterator<Map.Entry<String, String>> it = storage.entrySet().iterator();

                  while (it.hasNext()) {
                        Map.Entry<String, String> elem = it.next();
                        writer.write(elem.getKey().getBytes("UTF-8"));
                        writer.write("\0".getBytes("UTF-8"));
                        writer.writeInt(offset);
                        offset += elem.getValue().getBytes("UTF-8").length;
                        values.add(elem.getValue());
                  }
                  for (int i = 0; i < values.size(); i++) {
                        writer.write(values.get(i).getBytes("UTF-8"));
                  }
            } catch (IOException e) {
                  writer.close();
                  throw new IOException("Ошибка записи файла");
            }

            writer.close();
      }

      private Integer countOffset(Map<String, String> storage) throws IOException {
            Integer curOffset = 0;
            Iterator<Map.Entry<String, String>> it = storage.entrySet().iterator();
            while (it.hasNext()) {
                  curOffset += it.next().getKey().getBytes("UTF-8").length + 1 + 4;
            }

            return curOffset;
      }
}
