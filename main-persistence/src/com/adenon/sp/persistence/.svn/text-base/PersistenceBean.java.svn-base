package com.adenon.sp.persistence;

import com.adenon.sp.administration.ConfigLocation;
import com.adenon.sp.administration.annotate.MBean;
import com.adenon.sp.administration.annotate.Operation;
import com.adenon.sp.administration.annotate.Parameter;

@MBean(location = ConfigLocation.SYSTEM, subLocation = "persistence=Persistence")
public class PersistenceBean {

    private PersistenceService service;

    public PersistenceBean() {
    }

    public PersistenceBean(PersistenceService service) {
        this.service = service;
    }

    @Operation(name = "Delete configuration")
    public String deleteConfiguration(@Parameter(name = "Pool Name") String poolName) {
        boolean deletePool = this.service.deletePool(poolName);
        if (deletePool) {
            return "DB Pool [" + poolName + "] has been deleted successfully.";
        }
        return "No pool could be found named [" + poolName + "].";
    }

}
