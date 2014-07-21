package xmlParser;

import java.io.File;

import javax.swing.JFileChooser;

public class OpenFolder {
	public static File pickFolderPath() {
		  File folder;
			JFileChooser file_chooser = new JFileChooser();
			file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int opt = file_chooser.showSaveDialog(file_chooser);
			if (opt == JFileChooser.APPROVE_OPTION) {
	            folder = file_chooser.getSelectedFile();
	        return folder;
	        }
			return null;
	}

}
