package com.adenon.sp.services.service.info;

public enum ServiceState {

    INITIALIZED(true),
    ACTIVE(true),
    FINISHED(true);

    private boolean tracable;

    private ServiceState(boolean tracable) {
        this.tracable = tracable;
    }

    /**
     * Status tracing type.
     * 
     * @return Whether if status is tracable by listeners or not.
     */
    public boolean changeTo(ServiceState newState) {
        if ((this == newState) || !this.tracable) {
            return false;
        }
        return true;
    }

}
