package com.citysystem.cmdapplicationdemo.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.repositories.PartnerRepository;
import com.citysystem.cmdapplicationdemo.repositories.TariffRepository;

import java.util.Date;
import java.util.List;

import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_TRUE;

public class PartnerViewModel extends AndroidViewModel {

    private TariffRepository tariffRepository;
    private PartnerRepository partnerRepository;
    private long defaultTariff = -1;

    public PartnerViewModel(@NonNull Application application) {
        super(application);
        partnerRepository = new PartnerRepository(application);
        tariffRepository = new TariffRepository(application);

    }

    public void insertPartnerViewModel(Partner partner) {
        defaultTariff = tariffRepository.getDefaultTariff();
        if (defaultTariff != -1) {
            partner.setTariffId(defaultTariff);
            partner.setTempo(IS_TRUE);
            partner.setDateCreation(new Date());
            partnerRepository.insertPartnerRepo(partner);
        }
    }

    public void updatePartner(Partner partner) {

        partnerRepository.updatePartner(partner);

    }

    public void deletePartner(Partner partner) {
        if (partner.isTempo()) {
            partnerRepository.deletePartner(partner);
        }
    }

    public long getDefaultTariff() {
        return defaultTariff;
    }

    public LiveData<List<Partner>> getAllPartners() {
        return partnerRepository.getAllPartners();
    }

    public LiveData<List<Partner>> getPartnersTempo() {
        return partnerRepository.getPartnerTempo();
    }

    public Partner getPartnerViaScanner(String external) {
        return partnerRepository.getPartnerViaScanner(external);
    }


}
