package fr.enac.smartdring.modele;


/**
 * Cette classe est le modèle représentant un profil audio du téléphone.
 * Un profil audio possède un nom, une icone, et un état d'activation. Ainsi qu'une valeur de volume pour chaque stream du téléphone.
 * Created by chevalier on 02/10/14.
 */
public class Profil {
    private String name;
    private int iconeId;
    private boolean isActive;

    private int streamAlarmValue;
    private int streamMusicValue;
    private int streamNotificationValue;
    private int streamRingValue;
    private int streamSystemValue;
    private int streamVoiceValue;


    /**
     * Constructeur d'un profil audio.
     *
     * @param name                    Le nom du profil.
     * @param iconeId                 L'identifiant de l'icone associée au profil.
     * @param streamNotificationValue Le volume des notifications.
     * @param streamSystemValue       Le volume des bruits systèmes.
     * @param streamRingValue         Le volume de la sonnerie du téléphone.
     * @param streamVoiceValue        Le volume de la voix d'un appelant.
     * @param streamAlarmValue        Le volme du réveil.
     * @param streamMusicValue        Le volume de la musique.
     */
    public Profil(String name, int iconeId, int streamNotificationValue, int streamSystemValue,
                  int streamRingValue, int streamVoiceValue, int streamAlarmValue,
                  int streamMusicValue) {
        this.name = name;
        this.iconeId = iconeId;
        this.streamNotificationValue = streamNotificationValue;
        this.streamSystemValue = streamSystemValue;
        this.streamRingValue = streamRingValue;
        this.streamVoiceValue = streamVoiceValue;
        this.streamAlarmValue = streamAlarmValue;
        this.streamMusicValue = streamMusicValue;
        this.isActive = false;
    }

    /**
     * Constructeur d'un profil audio.
     *
     * @param name                    Le nom du profil.
     * @param iconeId                 L'identifiant de l'icone associée au profil.
     * @param streamNotificationValue Le volume des notifications.
     * @param streamSystemValue       Le volume des bruits systèmes.
     * @param streamRingValue         Le volume de la sonnerie du téléphone.
     * @param streamVoiceValue        Le volume de la voix d'un appelant.
     * @param streamAlarmValue        Le volme du réveil.
     * @param streamMusicValue        Le volume de la musique.
     */
    public Profil(String name, int iconeId, int streamNotificationValue, int streamSystemValue,
                  int streamRingValue, int streamVoiceValue, int streamAlarmValue,
                  int streamMusicValue, int isActive) {
        this.name = name;
        this.iconeId = iconeId;
        this.streamNotificationValue = streamNotificationValue;
        this.streamSystemValue = streamSystemValue;
        this.streamRingValue = streamRingValue;
        this.streamVoiceValue = streamVoiceValue;
        this.streamAlarmValue = streamAlarmValue;
        this.streamMusicValue = streamMusicValue;
        if (isActive == 0)
            this.isActive = false;
        else
            this.isActive = true;
    }

    /* ---- Getter et setter ---- */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconeId() {
        return iconeId;
    }

    public void setIconeId(int iconeId) {
        this.iconeId = iconeId;
    }

    public int getStreamAlarmValue() {
        return streamAlarmValue;
    }

    public void setStreamAlarmValue(int streamAlarmValue) {
        this.streamAlarmValue = streamAlarmValue;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getStreamMusicValue() {
        return streamMusicValue;
    }

    public void setStreamMusicValue(int streamMusicValue) {
        this.streamMusicValue = streamMusicValue;
    }

    public int getStreamNotificationValue() {
        return streamNotificationValue;
    }

    public void setStreamNotificationValue(int streamNotificationValue) {
        this.streamNotificationValue = streamNotificationValue;
    }

    public int getStreamRingValue() {
        return streamRingValue;
    }

    public void setStreamRingValue(int streamRingValue) {
        this.streamRingValue = streamRingValue;
    }

    public int getStreamVoiceValue() {
        return streamVoiceValue;
    }

    public void setStreamVoiceValue(int streamVoiceValue) {
        this.streamVoiceValue = streamVoiceValue;
    }

    public int getStreamSystemValue() {
        return streamSystemValue;
    }

    public void setStreamSystemValue(int streamSystemValue) {
        this.streamSystemValue = streamSystemValue;
    }
    /* ---- ---- */
}
