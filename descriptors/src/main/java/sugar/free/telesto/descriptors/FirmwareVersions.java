package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class FirmwareVersions implements Parcelable {

    private String releaseSWVersion;
    private String uiProcSWVersion;
    private String pcProcSWVersion;
    private String mdTelProcSWVersion;
    private String btInfoPageVersion;
    private String safetyProcSWVersion;
    private int configIndex;
    private int historyIndex;
    private int stateIndex;
    private int vocabularyIndex;

    protected FirmwareVersions(Parcel in) {
        releaseSWVersion = in.readString();
        uiProcSWVersion = in.readString();
        pcProcSWVersion = in.readString();
        mdTelProcSWVersion = in.readString();
        btInfoPageVersion = in.readString();
        safetyProcSWVersion = in.readString();
        configIndex = in.readInt();
        historyIndex = in.readInt();
        stateIndex = in.readInt();
        vocabularyIndex = in.readInt();
    }

    public static final Creator<FirmwareVersions> CREATOR = new Creator<FirmwareVersions>() {
        @Override
        public FirmwareVersions createFromParcel(Parcel in) {
            return new FirmwareVersions(in);
        }

        @Override
        public FirmwareVersions[] newArray(int size) {
            return new FirmwareVersions[size];
        }
    };

    public FirmwareVersions() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(releaseSWVersion);
        parcel.writeString(uiProcSWVersion);
        parcel.writeString(pcProcSWVersion);
        parcel.writeString(mdTelProcSWVersion);
        parcel.writeString(btInfoPageVersion);
        parcel.writeString(safetyProcSWVersion);
        parcel.writeInt(configIndex);
        parcel.writeInt(historyIndex);
        parcel.writeInt(stateIndex);
        parcel.writeInt(vocabularyIndex);
    }

    public String getReleaseSWVersion() {
        return this.releaseSWVersion;
    }

    public String getUiProcSWVersion() {
        return this.uiProcSWVersion;
    }

    public String getPcProcSWVersion() {
        return this.pcProcSWVersion;
    }

    public String getMdTelProcSWVersion() {
        return this.mdTelProcSWVersion;
    }

    public String getBtInfoPageVersion() {
        return this.btInfoPageVersion;
    }

    public String getSafetyProcSWVersion() {
        return this.safetyProcSWVersion;
    }

    public int getConfigIndex() {
        return this.configIndex;
    }

    public int getHistoryIndex() {
        return this.historyIndex;
    }

    public int getStateIndex() {
        return this.stateIndex;
    }

    public int getVocabularyIndex() {
        return this.vocabularyIndex;
    }

    public void setReleaseSWVersion(String releaseSWVersion) {
        this.releaseSWVersion = releaseSWVersion;
    }

    public void setUiProcSWVersion(String uiProcSWVersion) {
        this.uiProcSWVersion = uiProcSWVersion;
    }

    public void setPcProcSWVersion(String pcProcSWVersion) {
        this.pcProcSWVersion = pcProcSWVersion;
    }

    public void setMdTelProcSWVersion(String mdTelProcSWVersion) {
        this.mdTelProcSWVersion = mdTelProcSWVersion;
    }

    public void setBtInfoPageVersion(String btInfoPageVersion) {
        this.btInfoPageVersion = btInfoPageVersion;
    }

    public void setSafetyProcSWVersion(String safetyProcSWVersion) {
        this.safetyProcSWVersion = safetyProcSWVersion;
    }

    public void setConfigIndex(int configIndex) {
        this.configIndex = configIndex;
    }

    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
    }

    public void setStateIndex(int stateIndex) {
        this.stateIndex = stateIndex;
    }

    public void setVocabularyIndex(int vocabularyIndex) {
        this.vocabularyIndex = vocabularyIndex;
    }
}
