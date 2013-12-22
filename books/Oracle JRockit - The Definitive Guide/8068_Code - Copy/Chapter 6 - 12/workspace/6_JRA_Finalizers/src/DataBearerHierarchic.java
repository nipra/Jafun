public class DataBearerHierarchic {
	private String myName;

	private DataBearerHierarchic myChild;

	public DataBearerHierarchic(int level, int levelsToGo, String FamilyName,
			String ParentName) {
		if (level == 0) {
			myName = FamilyName;
		} else {
			myName = FamilyName + level + " son of " + ParentName;
		}

		if (level < levelsToGo) {
			myChild = new DataBearerHierarchic(level + 1, levelsToGo,
					FamilyName, myName);
		} else {
			// This is not the bug, but it's intentionally used to provoke
			// garbage collects
			byte[] allocator = new byte[1024];
			allocator[12] = 0;
		}
	}

	public DataBearerHierarchic getChild() {
		return myChild;
	}

	protected void finalize() throws Throwable {
		// "Help" the garbage collector by setting all parameters to null
		myName = null;
	}
}
