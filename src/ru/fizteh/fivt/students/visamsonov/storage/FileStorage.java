package ru.fizteh.fivt.students.visamsonov.storage;

import java.util.TreeMap;
import java.math.BigInteger;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileStorage implements Table {

	private final File dbFilePath;
	private final TreeMap<String, String> memoryStore;

	public FileStorage (String directory, String fileName) throws IOException {
		dbFilePath = new File(directory, fileName);
		memoryStore = new TreeMap<String, String>();
		dbFilePath.createNewFile();
		loadDataToMemory();
	}

	private void loadDataToMemory () throws IOException {
		DataInputStream dbFile = new DataInputStream(new BufferedInputStream(new FileInputStream(dbFilePath)));
		for (;;) {
			int keyLength, valueLength;
			try {
				keyLength = dbFile.readInt();
			}
			catch (EOFException e) { // It's okay
				break;
			}
			valueLength = dbFile.readInt();
			byte[] keyRaw = new byte[keyLength];
			dbFile.readFully(keyRaw);
			byte[] valueRaw = new byte[valueLength];
			dbFile.readFully(valueRaw);
			put(new String(keyRaw, StandardCharsets.UTF_8), new String(valueRaw, StandardCharsets.UTF_8));
		}
	}

	public String getName () {
		throw new UnsupportedOperationException();
	}

	public String get (String key) {
		return memoryStore.get(key);
	}

	public String put (String key, String value) {
		return memoryStore.put(key, value);
	}

	public String remove (String key) {
		return memoryStore.remove(key);
	}

	public int rollback () {
		throw new UnsupportedOperationException();
	}

	public int size () {
		throw new UnsupportedOperationException();
	}

	public int commit () {
		int saved = 0;
		try {
			DataOutputStream dbFile = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dbFilePath)));
			while (memoryStore.firstEntry() != null) {
				byte[] key = memoryStore.firstEntry().getKey().getBytes(StandardCharsets.UTF_8);
				byte[] value = memoryStore.firstEntry().getValue().getBytes(StandardCharsets.UTF_8);
				memoryStore.pollFirstEntry();
				dbFile.writeInt(key.length);
				dbFile.writeInt(value.length);
				dbFile.write(key);
				dbFile.write(value);
				++saved;
			}
			dbFile.close();
		}
		catch (IOException e) {}
		return saved;
	}
}