package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.SOS;

public class NewSosReportEvent {
    private SOS newSosReport;


    public NewSosReportEvent(SOS newSosReport) {
        this.newSosReport = newSosReport;
    }

    public SOS getNewSosReport() {
        return newSosReport;
    }

    public void setNewSosReport(SOS newSosReport) {
        this.newSosReport = newSosReport;
    }
}

