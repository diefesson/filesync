package com.diefesson.filesync.file;

import java.util.HashSet;

public class DiffSet extends HashSet<Diff> {

	private static final long serialVersionUID = 1L;

	public DiffSet(FileStructure local, FileStructure remote) {
		var pairs = local.get(null).subFilesPairs(remote.get(null));
		for(var p : pairs) {
			compare(p.left, p.right);
		}
	}
	
	private void compare(VirtualFile vf1, VirtualFile vf2) {
		if(vf1 == null) {
			createDiffs(vf2, DiffType.REMOTE);
		} else if(vf2 == null) {
			createDiffs(vf1, DiffType.LOCAL);
		} else if(vf1.getType() != vf2.getType()) {
			add(new Diff(vf1.getPath(), null, DiffType.CONFLICT));
		} else if(vf1.getType() == FileType.FOLDER) {
			for(var p : vf1.subFilesPairs(vf2)) {
				compare(p.left, p.right);
			}
		}
	}
	
	private void createDiffs(VirtualFile virtualFile, DiffType diffType) {
		virtualFile.recurse().forEach((vf)->{
			add(new Diff(vf.getPath(), virtualFile.getType(), diffType));
		});
	}
	
}
