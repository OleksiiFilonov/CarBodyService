package oleksii.filonov.processors;

import oleksii.filonov.settings.Settings;

public interface DataProcessorFacade {

    public void createResultFile(Settings settings, FilesToProcess filesToProcess);

}
