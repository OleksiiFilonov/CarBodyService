package oleksii.filonov.processors;

import oleksii.filonov.settings.Settings;

import java.io.File;

public interface DataProcessorFacade {

    public void createResultFile(File clientsFile, File campaignFile, File resultFile);

    public void setSettings(Settings settings);
}
