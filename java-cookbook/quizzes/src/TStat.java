public class TStat {
	int nq;
	int n[];
	int complete;
	int incomplete;
	int noAnswer;
	int noObjective;

	public String toString() {
		StringBuffer sb = 
			new StringBuffer("TestEdit Exam Statistics:            \n");
		sb.append("Number of questions: ");
		sb.append(nq);
		sb.append("\n");
		for (int i=0; i<n.length; i++) {
			sb.append("Number of ");
			sb.append((char)(i + 'A'));
			sb.append(" answers = ");
			sb.append(n[i]);
			sb.append("\n");
		}
		if (incomplete > 0) {
			sb.append("Warning: ");
			sb.append(incomplete);
			sb.append(" questions incomplete!\n");
		}
		if (noAnswer > 0) {
			sb.append("Warning: ");
			sb.append(noAnswer);
			sb.append(" questions have no answer!\n");
		}
		if (noObjective > 0) {
			sb.append("Warning: ");
			sb.append(noAnswer);
			sb.append(" questions have no Objective!\n");
		}
		return sb.toString();
	}
}
