package ru.fizteh.fivt.students.podoltseva.shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandCp implements Command {

	@Override
	public String getName() {
		return "cp";
	}

	@Override
	public int getArgsCount() {
		return 2;
	}

	@Override
	public void execute(State state, String[] args)
			throws FileNotFoundException, IOException {
		Path sourcePath = Paths.get(args[0]);
		Path sourceAbsolutePath;
		if (!sourcePath.isAbsolute()) {
			sourceAbsolutePath = state.getState().resolve(sourcePath);
		} else {
			sourceAbsolutePath = sourcePath;
		}
		File source = new File(sourceAbsolutePath.toString());
		if (!source.exists()) {
			throw new FileNotFoundException("cp: '" + args[0] + "': No such file or directory");
		}
		Path destinationPath = Paths.get(args[1]);
		Path destinationAbsolutePath;
		if (!destinationPath.isAbsolute()) {
			destinationAbsolutePath = state.getState().resolve(destinationPath);
		} else {
			destinationAbsolutePath = destinationPath;
		}
		File destination = new File(destinationAbsolutePath.toString());
		if (!destination.exists()) {
			throw new FileNotFoundException("cp: '" + args[1] + "': No such file or directory");
		}
		if (sourceAbsolutePath.toString().equals(destinationAbsolutePath.toString())) {
			if (source.isFile()) {
				throw new IOException("cp: '" + args[0] + "' and '" + args[1] + "' - are the same file");
			} else { 
				throw new IOException("cp: It is impossible to copy the directory '" + args[0] + "' into itself");
			}
		}
		if (source.isFile() && destination.isFile()) {
			throw new IOException("cp: The file with name '" + args[1] + "' alresdy exists.");
		} else if (source.isFile() && destination.isDirectory()) {
			Files.copy(sourceAbsolutePath, sourceAbsolutePath.resolve(source.getName()));
		} else if (source.isDirectory() && destination.isFile()) {
			throw new IOException("cp: Can't overwrite file '" + args[1] + "' with directory '" + args[0] + "'.");
		} else if (source.isDirectory() && destination.isDirectory()) {
			recursiveCopyFile(source, destination);
		}
	}
	
	private void recursiveCopyFile(File source, File destination) throws IOException {
		Files.copy(source.toPath(), destination.toPath().resolve(source.getName()));
		if (source.isDirectory()) {
			File[] sourceFileList = source.listFiles();
			destination = new File(destination.toPath().resolve(source.getName()).toString());
			for (File i : sourceFileList) {
				recursiveCopyFile(i, destination);
			}
		}
	}

}
