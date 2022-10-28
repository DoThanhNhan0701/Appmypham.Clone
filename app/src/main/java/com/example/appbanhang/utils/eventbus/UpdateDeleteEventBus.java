package com.example.appbanhang.utils.eventbus;

import com.example.appbanhang.model.Advertisement;

public class UpdateDeleteEventBus {
    Advertisement advertisement;

    public UpdateDeleteEventBus(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
