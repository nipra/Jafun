public class Finalizers {
	public static void main(String[] args) {
		while (true) {
			for (int i = 0; i < 10; i++) {
				@SuppressWarnings("unused")
				DataBearerHierarchic db = new DataBearerHierarchic(0, 50,
						"DataBearer" + i, "");
			}
		}
	}
}
