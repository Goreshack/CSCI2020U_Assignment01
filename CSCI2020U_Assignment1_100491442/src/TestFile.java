import java.text.DecimalFormat;
// Given TestFile
// Modification made where instead of double for spamProb,
// use string to modify decimal format.
public class TestFile {
    private String filename;
    private String spamProbability;
    private String actualClass;
    DecimalFormat df = new DecimalFormat("0.00000");
    public TestFile(String filename,
                    String spamProbability,
                    String actualClass) {

        this.filename = filename;
        this.spamProbability = (spamProbability);
        this.actualClass = actualClass;
    }

    public String getFilename() { return this.filename; }
    public String getSpamProbability() {

        return this.spamProbability;
    }

    public String getActualClass() { return this.actualClass; }
    public void setFilename(String value) { this.filename = value; }
    public void setSpamProbability(String val) { this.spamProbability = val; }
    public void setActualClass(String value) { this.actualClass = value; }
}