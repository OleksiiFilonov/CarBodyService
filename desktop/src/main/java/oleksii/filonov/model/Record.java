package oleksii.filonov.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Record {

    private String bodyId;

    private List<String> references = Lists.newArrayList();

    private RecordStatus status = RecordStatus.UNDEFINED;

    public String getBodyId() {
        return this.bodyId;
    }

    public void setBodyId(final String bodyId) {
        this.bodyId = bodyId;
    }

    public List<String> getReferences() {
        return this.references;
    }

    public void setReferences(final List<String> references) {
        this.references = references;
    }

    public RecordStatus getStatus() {
        return this.status;
    }

    public void setStatus(final RecordStatus status) {
        this.status = status;
    }

    public String findReference(final int index) {
        if(this.references.size() <= index) {
            return null;
        } else {
            return this.references.get(index);
        }
    }

    public void addReference(final String reference) {
        this.references.add(reference);
    }

}
