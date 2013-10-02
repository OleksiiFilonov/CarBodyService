package oleksii.filonov.settings;

public class Settings {

    private String campaignColumnNumberCampaignTitle;  //"Номер кампании"

    private String campaignColumnDescriptionTitle;  //"Описание"

    private String clientColumnBodyNumber; //"Номер кузова"

    private String campaignColumnVinListIdTitle; //"VIN"

    public String getCampaignColumnNumberCampaignTitle() {
        return campaignColumnNumberCampaignTitle;
    }

    public void setCampaignColumnNumberCampaignTitle(final String campaignColumnNumberCampaignTitle) {
        this.campaignColumnNumberCampaignTitle = campaignColumnNumberCampaignTitle;
    }

    public String getCampaignColumnDescriptionTitle() {
        return campaignColumnDescriptionTitle;
    }

    public void setCampaignColumnDescriptionTitle(final String campaignColumnDescriptionTitle) {
        this.campaignColumnDescriptionTitle = campaignColumnDescriptionTitle;
    }

    public String getClientColumnBodyNumber() {
        return clientColumnBodyNumber;
    }

    public void setClientColumnBodyNumber(final String clientColumnBodyNumber) {
        this.clientColumnBodyNumber = clientColumnBodyNumber;
    }

    public String getCampaignColumnVinListIdTitle() {
        return campaignColumnVinListIdTitle;
    }

    public void setCampaignColumnVinListIdTitle(final String campaignColumnVinListIdTitle) {
        this.campaignColumnVinListIdTitle = campaignColumnVinListIdTitle;
    }
}
