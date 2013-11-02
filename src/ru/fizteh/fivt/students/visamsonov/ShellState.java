package ru.fizteh.fivt.students.visamsonov;

import ru.fizteh.fivt.storage.strings.*;
import ru.fizteh.fivt.students.visamsonov.storage.*;
import java.io.*;

public class ShellState {

	public TableInterface database;
	public final TableProviderInterface tableProvider;

	public ShellState () throws IOException {
		TableProviderFactoryInterface factory = new TableFactory();
		String directory = System.getProperty("fizteh.db.dir");
		if (!(new File(directory).isDirectory())) {
			throw new IOException("no such directory: \"" + directory + "\"");
		}
		tableProvider = factory.create(directory);
	}
}