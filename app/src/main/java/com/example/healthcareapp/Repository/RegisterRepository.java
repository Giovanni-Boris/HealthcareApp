package com.example.healthcareapp.Repository;


import com.example.healthcareapp.Entity.Register;
import com.example.healthcareapp.Room.RegisterDAO;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class RegisterRepository {

    private final RegisterDAO registerDAO;

    public RegisterRepository(RegisterDAO registerDAO) {
        this.registerDAO = registerDAO;
    }

    public Completable insertRegisters(List<Register> registers) {
        return Completable.fromAction(() -> registerDAO.insertAll(registers))
                .subscribeOn(Schedulers.io());
    }
    public Flowable<List<Register>> getAllRegisters() {
        return Flowable.fromCallable(() -> registerDAO.readAllData())
                .subscribeOn(Schedulers.io());
    }
}
