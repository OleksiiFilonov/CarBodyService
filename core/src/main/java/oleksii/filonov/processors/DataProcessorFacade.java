package oleksii.filonov.processors;

import oleksii.filonov.settings.Settings;

public interface DataProcessorFacade {

    public void createResultFile(FilesToProcess filesToProcess);

    public void setSettings(Settings settings);
}
