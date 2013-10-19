package oleksii.filonov.processors;

import java.io.File;

public class FilesToProcess {

    private File clientsFile;

    private File campaignFile;

    private File resultFile;

    public File getClientsFile() {
        return clientsFile;
    }

    public void setClientsFile(final File clientsFile) {
        this.clientsFile = clientsFile;
    }

    public File getCampaignFile() {
        return campaignFile;
    }

    public void setCampaignFile(final File campaignFile) {
        this.campaignFile = campaignFile;
    }

    public File getResultFile() {
        return resultFile;
    }

    public void setResultFile(final File resultFile) {
        this.resultFile = resultFile;
    }
}
