package org.jboss.threads;


public class ExampleWithSealedArchive {
    private String oneField;
    private JBossThread jBossThread;

    public JBossThread getjBossThread() {
        return jBossThread;
    }

    public void setjBossThread(JBossThread jBossThread) {
        this.jBossThread = jBossThread;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!" + new JBossThread(null));
    }

    public String getOneField() {
        return oneField;
    }

    public void setOneField(String oneField) {
        this.oneField = oneField;
    }
}
