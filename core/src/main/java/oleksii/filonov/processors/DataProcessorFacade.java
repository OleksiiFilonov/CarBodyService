package oleksii.filonov.processors;

import java.io.File;

public interface DataProcessorFacade {

    public void createResultFile(File clientsFile, File campaignFile, File resultFile);
}
