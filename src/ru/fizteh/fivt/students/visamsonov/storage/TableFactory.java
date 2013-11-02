package ru.fizteh.fivt.students.visamsonov.storage;

public class TableFactory implements TableProviderFactoryInterface {

	public TableProviderInterface create (String dir) {
		if (dir == null) {
			throw new IllegalArgumentException();
		}
		return new TableDirectory(dir);
	}
}